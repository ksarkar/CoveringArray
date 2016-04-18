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

public class ColGrColexIterator implements ColGrIterator {
	private int[] cols;
	private int t;
	private int k;
	
	/**
	 * Memory efficient and fast implementation of Lexicographic iterator of column t-sets
	 * @param t
	 * @param k
	 */
	public ColGrColexIterator(int t, int k) {
		assert(t <= k);
		
		this.cols = new int[t];
		this.k = k;
		this.t = t;
		this.rewind();
	}
	
	@Override
	public boolean hasNext() {
		return this.cols[0] < this.k - this.t;
	}

	@Override
	public ColGroup next() {
		boolean rewrite = this.cols[0] != 0;
		int i = 0; 
		for (; i < this.t - 1; i++) {
			if (this.cols[i+1] - this.cols[i] > 1) {
				break;
			}
		}
		// i holds the first-gap-index, increment it
		this.cols[i]++;
		
		// rewrite if necessary
		if (rewrite) {
			for (int j = 0; j < i; j++) {
				this.cols[j] = j;
			}
		}
		
		return new ColGroup(this.cols);		
	}

	@Override
	public void rewind() {
		// reset to the first t-set
		for (int i = 0; i < this.t; i++) {
			this.cols[i] = i;
		}
		this.cols[t-1]--;	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int t = 6;
		int k = 100;
		
		System.out.println("Lex order:");
		System.out.println(new Date());
		ColGrIterator ci = new ColGrLexIterator(t,k);
		long tot = 0;
		while(ci.hasNext()) {
			//System.out.println(ci.next());
			ci.next();
			tot++;
		}
		System.out.println(new Date());
		System.out.println("total = " + tot);
		
		System.out.println();
		
		System.out.println("Colex order:");
		System.out.println(new Date());
		ci = new ColGrColexIterator(t,k);
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
