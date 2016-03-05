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

package edu.asu.ca.kaushik.algorithms.randomized;

import java.util.Arrays;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;

public class AllColAvCovRow extends RandRowCAGenAlgo {
	private Random rand;
	private double devFrac;
	
	public AllColAvCovRow() {
		super();
		super.name = new String("AllColAvCovRow");
		this.rand = new Random(1234L);
		this.devFrac = 0.00d; 
	}

	@Override
	protected boolean selectRandRow(InteractionGraph ig, 
			Integer[] nextRow) {
		int t = ig.getT();
		int k = ig.getK();
		int v = ig.getV();
		
		assert (k == nextRow.length);
		
		//int avCoverage = computeAverage(ig.getNumUncovInt(), t, v);
		int[] numUncovIntD = ig.getUncovDist();
		int[] avCovCol = new int[k];
		for (int i = 0; i < k; i++) {
			avCovCol[i] = computeAverage(numUncovIntD[i], t, v);
			avCovCol[i] = (int)Math.ceil((1.0d - this.devFrac) * avCovCol[i]);
		}
		
		Integer[] maxRow = null;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < super.maxNumSamples; i++){
			makeRandRow(nextRow, v);
			int[] covD = new int[k];
			int coverage = ig.getCoverage(nextRow, covD);
			
			if (isGoodRow(covD, avCovCol)) {
				return true;
			}
			
			if (coverage > max) {
				max = coverage;
				maxRow = Arrays.copyOf(nextRow, nextRow.length);
			}
		}
		
		assert (maxRow != null);
		for (int i = 0; i < k; i++){
			nextRow[i] = maxRow[i];
		}
		return false;
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

}
