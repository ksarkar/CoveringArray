/**
 * Copyright (C) 2013-2016 Kaushik Sarkar <ksarkar1@asu.edu>
 * 
 * This file is part of CoveringArray project.
 * 
 * CoveringArray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * CoveringArray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with CoveringArray.  If not, see <http://www.gnu.org/licenses/>.
 */ 

package edu.asu.ca.kaushik.algorithms.randomized.balrandcomp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.outputformatter.ComputeCoverageOutputFormatter;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;

/**
 * Generates a pair of CAs. The first one using the BalancedCovRow method,
 * the second using the random row selection algorithm where the randomly
 * chosen row covers at least as many interactions as the BalancedCovRow method.
 * Used for comparison, and controls for number of covered interactions.
 * 
 * @author ksarkar1
 *
 */

public class ControlledComp {
	private int maxNumSamples = 100;
	private Random rand;
	private double devFrac;
	private ListCA balancedCA;
	private ListCA randomCA;

	public ControlledComp() {
		this.rand = new Random(1234L);
		this.devFrac = 0.00d; 
	}
	
	public List<CA> generateCAs(int t, int k, int v) {
		this.balancedCA = new ListCA(t, k, v);
		this.randomCA = new ListCA(t, k, v);
		
		InteractionGraph igB = new InteractionGraph(t, k, v);
		InteractionGraph igR = new InteractionGraph(t, k, v);
		
		while((!igB.isEmpty()) || (!igR.isEmpty())) {
			Integer[] nextBRow = new Integer[k];
			Integer[] nextRRow = new Integer[k];
			List<Boolean> status = selectRandRows(igB, igR, nextBRow, nextRRow);
			
			if (isValidRow(nextBRow, v)){
				int[] covDB = new int[k];
				int coverageB = igB.deleteInteractions(nextBRow, covDB);
				
				this.balancedCA.addRow(nextBRow);		
				this.balancedCA.addCoverage(coverageB);
				this.balancedCA.addCovDist(covDB);
				this.balancedCA.addStatus(status.get(0));
			}
			
			if (isValidRow(nextRRow, v)){
				int[] covDR = new int[k];
				int coverageR = igR.deleteInteractions(nextRRow, covDR);
				
				this.randomCA.addRow(nextRRow);		
				this.randomCA.addCoverage(coverageR);
				this.randomCA.addCovDist(covDR);
				this.randomCA.addStatus(status.get(1));
			}		

		}
		
		List<CA> list = new ArrayList<CA>();
		list.add(this.balancedCA);
		list.add(this.randomCA);
		return list;
	}

	private boolean isValidRow(Integer[] nextRow, int v) {
		return (nextRow[0] == v) ? false : true;
	}
	

	private List<Boolean> selectRandRows(InteractionGraph igB, InteractionGraph igR,
			Integer[] nextBRow, Integer[] nextRRow) {
		int t = igB.getT();
		int k = igB.getK();
		int v = igB.getV();
		boolean successB = false;
		boolean successR = false;
		
		assert (k == nextBRow.length) && (k == nextRRow.length);
		
		// Build the balanced row
		if (igB.getNumUncovInt() == 0) {
			nextBRow[0] = v;
		} else {
			int[] numUncovIntD = igB.getUncovDist();
			int[] avCovCol = new int[k];
			for (int i = 0; i < k; i++) {
				avCovCol[i] = computeAverage(numUncovIntD[i], t, v);
				avCovCol[i] = (int)Math.ceil((1.0d - this.devFrac) * avCovCol[i]);
			}
			
			Integer[] maxRightRow = null;
			Integer[] maxRow = null;
			int maxRight = Integer.MIN_VALUE;
			int max = Integer.MIN_VALUE;
			int[] covD = new int[k];
			for (int i = 0; i < this.maxNumSamples; i++){
				makeRandRow(nextBRow, v);
				int coverage = igB.getCoverage(nextBRow, covD);
				
				if (isGoodRow(covD, avCovCol)) {
					if (coverage > maxRight) {
						maxRight = coverage;
						maxRightRow = Arrays.copyOf(nextBRow, nextBRow.length);
					}
				}
				
				if (coverage > max) {
					max = coverage;
					maxRow = Arrays.copyOf(nextBRow, nextBRow.length);
				}
			}
			
			if (maxRightRow != null) {
				for (int i = 0; i < k; i++){
					nextBRow[i] = maxRow[i];
				}
				successB = true;
			} else {
				assert (maxRow != null);
				for (int i = 0; i < k; i++){
					nextBRow[i] = maxRow[i];
				}
			}
		}
		
		// Build the random row
		if (igR.getNumUncovInt() == 0) {
			nextRRow[0] = v;
		} else {
			int avCoverage = computeAverage(igR.getNumUncovInt(), t, v);
			Integer[] maxRow = null;
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < this.maxNumSamples; i++){
				makeRandRow(nextRRow, v);
				int coverage = igR.getCoverage(nextRRow, null);
				
				if (coverage > max) {
					max = coverage;
					maxRow = Arrays.copyOf(nextRRow, nextRRow.length);
				}
			}
			
			assert (maxRow != null);
			for (int i = 0; i < k; i++){
				nextRRow[i] = maxRow[i];
			}
			
			if (max >= avCoverage) {
				successR = true;
			}		
		}
		
		List<Boolean> ret = new ArrayList<Boolean>();
		ret.add(successB);
		ret.add(successR);
		return ret;	
	}
	
	private boolean isGoodRow(int[] covD, int[] avCovCol) {
		int k = covD.length;
		for (int i = 0; i < k; i++) {
			if (covD[i] < avCovCol[i]) {
				return false;
			}
		}
		return true;
	}

	private void makeRandRow(Integer[] nextRow, int v) {
		int k = nextRow.length;
		
		for (int i = 0; i < k; i++){
			nextRow[i] = new Integer(this.rand.nextInt(v));
		}
		
	}
	
	private int computeAverage(long numUncovInt, int t, int v) {
		return (int)Math.ceil(numUncovInt / Math.pow(v, t));
	}
	
	public static void main(String[] args) throws IOException {
		int t = 6;
		int k = 15;
		int v = 3;
		
		OutputFormatter formatter = new ComputeCoverageOutputFormatter();
		
		List<String> algoList = new ArrayList<String>();
	
		algoList.add("BalancedCovRow1");
		algoList.add("RandomRow1");
		
		formatter.setAlgoNames(algoList);
		formatter.preprocess(t, v);
		
		ControlledComp comp = new ControlledComp();
		List<CA> result = comp.generateCAs(t, k, v);
		
		formatter.output(k, result);
	}
}
