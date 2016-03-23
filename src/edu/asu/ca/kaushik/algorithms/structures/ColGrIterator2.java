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

package edu.asu.ca.kaushik.algorithms.structures;

import java.util.Date;

import org.apache.commons.math3.util.CombinatoricsUtils;

public class ColGrIterator2 implements ColGrIterator{
	long numGroups;
	long count;
	int[] cols;
	int t;
	int k;
	
	/**
	 * Memory efficient ColGrIterator. Generates the column groups on the fly.
	 * Hence does not require huge memory. But takes a lot of time.
	 * @param t
	 * @param k
	 */
	public ColGrIterator2(int t, int k) {
		assert(t <= k);
		
		this.t = t;
		this.k = k;
		this.numGroups = (long)CombinatoricsUtils.binomialCoefficientDouble(k, t);
		this.count = 1;
		this.cols = new int[t];
		this.initiateCols();
	}
	
	private void initiateCols() {
		for (int i = 0 ; i < this.t; i++) {
			this.cols[i] = i;
		}
		this.cols[this.t - 1]--;
	}

	@Override
	public boolean hasNext() {
		return (this.count <= this.numGroups) ? true : false;
	}

	@Override
	public ColGroup next() {
		do {
			this.incrementCounter();
		} while(!this.validCombination());
		
		this.count++;
		return new ColGroup(this.cols);
	}

	private void incrementCounter() {
		int ind = t-1;
		while (this.cols[ind] == this.k - 1) {
			this.cols[ind] = 0;
			ind--;
		}
		this.cols[ind]++;
	}
	
	private boolean validCombination() {
		for (int i = 0; i < this.t; i++) {
			for (int j = i-1; j >= 0; j--) {
				if (this.cols[i] <= this.cols[j]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void rewind() {
		this.count = 1;
		this.initiateCols();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int t = 6;
		int k = 52;
		
		System.out.println("Time efficient:");
		System.out.println(new Date());
		ColGrIterator ci = new ColGrIterator1(t,k);
		long tot = 0;
		while(ci.hasNext()) {
			//System.out.println(ci.next());
			ci.next();
			tot++;
		}
		System.out.println(new Date());
		System.out.println("total = " + tot);
		
		System.out.println();
		
		System.out.println("Memory efficient:");
		System.out.println(new Date());
		ci = new ColGrIterator2(t,k);
		tot = 0;
		while(ci.hasNext()) {
			//System.out.println(ci.next());
			ci.next();
			tot++;
		}
		System.out.println(new Date());
		System.out.println("total = " + tot);

	}

	

}
