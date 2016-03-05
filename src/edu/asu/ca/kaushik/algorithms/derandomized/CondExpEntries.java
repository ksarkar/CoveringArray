package edu.asu.ca.kaushik.algorithms.derandomized;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;


public class CondExpEntries implements CAGenAlgo {
	
	private Random rand;
	protected String name;
	protected int comp;
	
	public CondExpEntries() {
		super();
		this.rand = new Random(1234L);
		this.name = new String("Density");
		this.comp = 0;
	}

	@Override
	public void setNumSamples(int numSamples) {
		// dummy method

	}
	
	public void setComp(int comp){
		this.comp = comp;
	}
	
	public int getComp(){
		return this.comp;
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		ListCA ca = new ListCA(t, k, v);
		InteractionSet ig = this.createIntersectionSet(t,k,v);
		this.constructCA(ca,  ig);
		return ca;	
	}
	
	public void constructCA(ListCA ca, InteractionSet ig) {
		int k = ca.getK();
		int v = ca.getV();
		
		while(!ig.isEmpty()) {
			// time complexity measurement
			this.comp = 0;
			
			Integer[] newRandRow = selectNextRow(ig, k, v);
			
			//System.out.println(Arrays.toString(newRandRow));
			
			int[] covD = new int[k];
			int coverage = ig.deleteInteractions(newRandRow, covD);
			ca.addRow(newRandRow);	
			
			// time complexity measurement
			ca.addComp(this.comp);
			
			ca.addCoverage(coverage);
			// coverage distribution over columns
			ca.addCovDist(covD);
		}
	}
	
	public InteractionSet createIntersectionSet(int t, int k, int v) {
		return new InteractionGraph(t, k, v);
	}

	public Integer[] selectNextRow(InteractionSet ig, int k, int v) {
		return this.fillRow(this.makeStarredRow(k, v), ig, v);
	}

	protected Integer[] fillRow(Integer[] newRow, InteractionSet ig, int v) {
		List<Integer> indexList = this.makeIndexList(newRow, v);
		
		int l = indexList.size();
		for (int i = 0; i < l; i++){
			int index = this.selectIndexUniRandWORep(indexList);
			newRow[index] = this.chooseSymbol(newRow, index, v, ig);
		}
		
		return newRow;
	}

	protected Integer chooseSymbol(Integer[] newRow, int index, int v, InteractionSet ig) {
		Integer[] row = Arrays.copyOf(newRow, newRow.length);
		double maxCoverage = 0;
		int optSymb = 0;
		for (int symb = 0; symb < v; symb++){
			row[index] = new Integer(symb);
			double coverage = this.computeExpCoverageSymb(row, index, v, ig);
			if (coverage > maxCoverage){
				maxCoverage = coverage;
				optSymb = symb;
			}
		}
		return new Integer(optSymb);
	}

	protected double computeExpCoverageSymb(Integer[] row, int index, int v, InteractionSet ig) {
		// TODO replace it by iterator
		List<ColGroup> allColGroups = ig.getAllColGroups();
		double expCoverage = 0;
		
		for (ColGroup colGr : allColGroups){
			if (colGr.contains(index)) {
				expCoverage = expCoverage + this.computeExpectedCoverage(colGr, row, v, ig);
			}
		}
		return expCoverage;
	}

	protected double computeExpectedCoverage(ColGroup colGr, Integer[] row, int v,
			InteractionSet ig) {
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
		
		double count = 0;
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			for (int i = 0; i < entries.length; i++){
				syms[starredCols.get(i).intValue()] = entries[i];
			}
			
			if (ig.contains(new Interaction(colGr, new SymTuple(syms)))){
				count = count + 1.0d;
			}
		}
		
		// time complexity measurement
		this.comp = this.comp + allBlankEntries.size();
		
		return (count / allBlankEntries.size());
	}

	protected int selectIndexUniRandWORep(List<Integer> indexList) {
		int len = indexList.size();
		int randInd = this.rand.nextInt(len);
		int index = indexList.remove(randInd).intValue();
		return index;
	}

	protected List<Integer> makeIndexList(Integer[] newRow, int v) {
		List<Integer> li = new ArrayList<Integer>();
		int k = newRow.length;
		for (int i = 0; i < k; i++) {
			if (newRow[i].intValue() == v) {
				li.add(new Integer(i));
			}
		}
		return li;
	}

	protected Integer[] makeStarredRow(int k, int v) {
		Integer[] newRow = new Integer[k];
		for (int i = 0; i < k; i++){
			newRow[i] = new Integer(v);
		}
		
		return newRow;
	}
	
	protected Integer[] updateRow(Integer[] newRow, Interaction firstInteraction) {
		int[] cols = firstInteraction.getCols().getCols();
		int[] syms = firstInteraction.getSyms().getSyms();
		int len = cols.length;
		for (int i = 0; i < len; i++){
			newRow[cols[i]] = new Integer(syms[i]);
		}
		return newRow;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		int t = partialCA.getT();
		int k = partialCA.getK();
		int v = partialCA.getV();
		
		InteractionGraph ig = new InteractionGraph(t, k, v);
		ig.deleteCoveredInteractions(partialCA);
		
		while(!ig.isEmpty()) {			
			Integer[] newRandRow = selectNextRow(ig, k, v);
			ig.deleteInteractions(newRandRow, null);
			partialCA.addRow(newRandRow);	 
		}
		
		return partialCA;
	}

}
