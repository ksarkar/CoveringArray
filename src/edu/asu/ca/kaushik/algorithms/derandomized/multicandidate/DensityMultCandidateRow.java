package edu.asu.ca.kaushik.algorithms.derandomized.multicandidate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.CondExpInterSampled;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;


public class DensityMultCandidateRow extends CondExpEntries {
	private int numCandidates;

	public DensityMultCandidateRow() {
		super();
		
		super.name = new String("DensityMultCandidateRow");
		this.numCandidates = 3;
	}
	
	@Override
	public Integer[] selectNextRow(InteractionSet ig, int k, int v) {
		Integer[] bestRow = null;
		int bestCoverage = 0;
		
		for (int i = 0; i < this.numCandidates; i++) {
			Integer[] newRow = super.fillRow(super.makeStarredRow(k, v), ig, v);
			int coverage = ig.getCoverage(newRow, null);
			if (coverage > bestCoverage) {
				bestCoverage = coverage;
				bestRow = newRow;
			}
		}
		
		return bestRow;
	}
	
	@Override 
	protected Integer chooseSymbol(Integer[] newRow, int index, int v, InteractionSet ig) {
		Integer[] row = Arrays.copyOf(newRow, newRow.length);
		
		List<Integer> symbList = super.makeIndexList(super.makeStarredRow(v, v), v);
		
		double maxCoverage = 0;
		int optSymb = 0;
		for (int i = 0; i < v; i++){
			int symb = super.selectIndexUniRandWORep(symbList);
			row[index] = new Integer(symb);
			double coverage = super.computeExpCoverageSymb(row, index, v, ig);
			if (coverage > maxCoverage){
				maxCoverage = coverage;
				optSymb = symb;
			}
		}
		return new Integer(optSymb);
	}
		
	public static void main(String[] args) throws IOException {
		int t = 6;
		int v = 3;
		
		int k1 = 17;
		int k2 = 19;
		
		//List<String> algoList = getAllDetAlgos();
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new DensityMultCandidateRow());
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\density-multi-cand-row\\");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}
	

}
