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

public class MaxUncovColRow extends RandRowCAGenAlgo {
	private Random rand;
	public MaxUncovColRow() {
		super();
		super.name = new String("MaxUncovColRow");
		this.rand = new Random(1234L);
	}

	@Override
	protected boolean selectRandRow(InteractionGraph ig, 
			Integer[] nextRow) {
		int t = ig.getT();
		int k = ig.getK();
		int v = ig.getV();
		
		assert (k == nextRow.length);
		
		int avCoverage = getAverage(ig.getNumUncovInt(), t, v);
		int[] numUncovIntD = ig.getUncovDist();
		int maxUncov = Integer.MIN_VALUE;
		int maxColIdx = k;
		for (int i = 0; i < k; i++) {
			if (numUncovIntD[i] > maxUncov) {
				maxUncov = numUncovIntD[i];
				maxColIdx = i;
			}
		}
		int avMaxColCov = getAverage(maxUncov, t, v);	
		
		Integer[] maxRow = null;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < super.maxNumSamples; i++){
			makeRandRow(nextRow, v);
			int[] covD = new int[k];
			int coverage = ig.getCoverage(nextRow, covD);
			if ((covD[maxColIdx] >= avMaxColCov) 
				&& (coverage >= avCoverage)) {
				return true;
			}
			
			if (covD[maxColIdx] >= max) {
				max = covD[maxColIdx];
				maxRow = Arrays.copyOf(nextRow, nextRow.length);
			}
		}
		
		assert (maxRow != null);
		for (int i = 0; i < k; i++){
			nextRow[i] = maxRow[i];
		}
		return false;
	}

	private void makeRandRow(Integer[] nextRow, int v) {
		int k = nextRow.length;
		
		for (int i = 0; i < k; i++){
			nextRow[i] = new Integer(this.rand.nextInt(v));
		}
		
	}
	
	private int getAverage(long numUncovInt, int t, int v) {
		return (int)Math.ceil(numUncovInt / Math.pow(v, t));
	}

}
