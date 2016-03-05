package edu.asu.ca.kaushik.algorithms.derandomized.pe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.pe.structures.ExtInteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;


/* Implements the pessimistic estimate version of the 
 * conditional expectation based algorithm for interaction 
 * selection using pessimistic estimates of expected coverage
 */

public class CondExpIterPE implements CAGenAlgo {
	
	private String name = "Interaction Selection with Pessimistic Estimate";
	private Random rand;
	
	public CondExpIterPE() {
		super();
		this.rand = new Random(1234L);
		this.name = new String("Interaction Selection with Pessimistic Estimate");
	}

	@Override
	public void setNumSamples(int numSamples) {
		// dummy method

	}

	@Override
	public CA generateCA(int t, int k, int v) {
		ListCA ca = new ListCA(t, k, v);
		ExtInteractionGraph uncovISet = new ExtInteractionGraph(t, k, v);
		
		while(!uncovISet.isEmpty()) {			
			Integer[] newRow = this.selectRow(uncovISet, k, v);
			uncovISet.deleteInteractions(newRow);
			ca.addRow(newRow);	
		}
		
		return ca;
	}

	private Integer[] selectRow(ExtInteractionGraph uncovISet, int k, int v) {
		Integer[] newRow = this.makeStarredRow(k, v);
		
		Interaction firstInteraction = uncovISet.selectBestInteraction(newRow);
		
		newRow = this.updateRow(newRow, firstInteraction);
		return this.fillRow(newRow, uncovISet, v);	
	}

	private Integer[] makeStarredRow(int k, int v) {
		Integer[] newRow = new Integer[k];
		for (int i = 0; i < k; i++){
			newRow[i] = new Integer(v);
		}
		
		return newRow;
	}	

	private Integer[] updateRow(Integer[] newRow, Interaction firstInteraction) {
		int[] cols = firstInteraction.getCols().getCols();
		int[] syms = firstInteraction.getSyms().getSyms();
		int len = cols.length;
		for (int i = 0; i < len; i++){
			newRow[cols[i]] = new Integer(syms[i]);
		}
		return newRow;
	}
	
	private Integer[] fillRow(Integer[] newRow, ExtInteractionGraph uncovISet, int v) {
		List<Integer> indexList = this.makeIndexList(newRow, v);
		
		int l = indexList.size();
		for (int i = 0; i < l; i++){
			int index = this.selectIndexUniRandWORep(indexList);
			newRow[index] = this.chooseSymbol(newRow, index, v, uncovISet);
		}
		
		return newRow;
	}

	private List<Integer> makeIndexList(Integer[] newRow, int v) {
		List<Integer> li = new ArrayList<Integer>();
		int k = newRow.length;
		for (int i = 0; i < k; i++) {
			if (newRow[i].intValue() == v) {
				li.add(new Integer(i));
			}
		}
		return li;
	}
	
	private int selectIndexUniRandWORep(List<Integer> indexList) {
		int len = indexList.size();
		int randInd = this.rand.nextInt(len);
		int index = indexList.remove(randInd).intValue();
		return index;
	}
	
	private Integer chooseSymbol(Integer[] newRow, int index, int v,
			ExtInteractionGraph uncovISet) {
		Integer[] row = Arrays.copyOf(newRow, newRow.length);
		double maxCoverage = 0;
		int optSymb = 0;
		for (int symb = 0; symb < v; symb++){
			row[index] = new Integer(symb);
			double coverage = this.computeExpCoverageSymb(row, index, v, uncovISet);
			if (coverage > maxCoverage){
				maxCoverage = coverage;
				optSymb = symb;
			}
		}
		return new Integer(optSymb);
	}

	private double computeExpCoverageSymb(Integer[] row, int index, int v,
			ExtInteractionGraph uncovISet) {
		int t = uncovISet.gett();
		int k = uncovISet.getk();
		List<ColGroup> colCombsWIndex = this.getAllColCombsWIndex(k, t, index);
		double expCoverage = 0;
		
		for (ColGroup colGr : colCombsWIndex){
				expCoverage = expCoverage + this.computeExpectedCoverage(colGr, row, v, uncovISet);
		}
	
		return expCoverage;
	}

	private List<ColGroup> getAllColCombsWIndex(int k, int t, int index) {
		List<ColGroup> colCombWOIndex = Helper.createAllColGroups(t-1, k-1);
		List<ColGroup> colCombWIndex = new ArrayList<ColGroup>();
		for (ColGroup colGr : colCombWOIndex) {
			colCombWIndex.add(colGr.addCol(index));
		}
		return colCombWIndex;
	}
	
	private double computeExpectedCoverage(ColGroup colGr, Integer[] row,
			int v, ExtInteractionGraph uncovISet) {
		List<Integer> starredCols = new ArrayList<Integer>();
		int t = colGr.getLen();
		int[] syms = new int[t];
		int[] cols = colGr.getCols();
		
		for (int i = 0; i < t; i++){
			int s = row[cols[i]].intValue();
			if ( s == v){
				starredCols.add(new Integer(i));
			}
			else {
				syms[i] = s;
			}
		}
		
		List<SymTuple> allBlankEntries = Helper.createAllSymTuples(starredCols.size(), v);
		
		double expCoverage = 0;
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			for (int i = 0; i < entries.length; i++){
				syms[starredCols.get(i).intValue()] = entries[i];
			}
			
			if (uncovISet.contains(new Interaction(colGr, new SymTuple(syms)))){
				expCoverage = expCoverage + 1.0d;
			}
		}

		return (expCoverage / allBlankEntries.size());
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public static void main(String[] args) throws IOException {
		int t = 4;
		int v = 3;
		
		int k1 = 10;
		int k2 = 25;
		
		//List<String> algoList = getAllDetAlgos();
		
		List<String> algoList = new ArrayList<String>();
		
		algoList.add("CondExpEntries");
		algoList.add("CondExpIterPE");
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\");
		
		Runner runner = new Runner(formatter);
		runner.setAlgo(algoList);
		
		runner.setParam(t, v, k1, k2);
		runner.run();
		
		runner.setParam(5, 3, 10, 20);
		runner.run();
		
		runner.setParam(6, 3, 10, 20);
		runner.run();
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		int t = partialCA.getT();
		int k = partialCA.getK();
		int v = partialCA.getV();
		
		ExtInteractionGraph uncovISet = new ExtInteractionGraph(t, k, v);
		uncovISet.deleteCoveredInteractions(partialCA);
		
		while(!uncovISet.isEmpty()) {			
			Integer[] newRow = this.selectRow(uncovISet, k, v);
			uncovISet.deleteInteractions(newRow);
			partialCA.addRow(newRow);	 
		}
		
		return partialCA;
	}

}
