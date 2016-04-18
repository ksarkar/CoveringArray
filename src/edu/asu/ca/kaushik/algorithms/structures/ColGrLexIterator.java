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

public class ColGrLexIterator implements ColGrIterator {	
	private int[] cols;
	private int t;
	private int k;
	private boolean isLastVal;
	private int lastGapInd;
	
	
	/**
	 * Memory efficient and fast implementation of Lexicographic iterator of column t-sets
	 * @param t
	 * @param k
	 */
	public ColGrLexIterator(int t, int k){
		assert(t <= k);
		
		this.cols = new int[t];
		this.k = k;
		this.t = t;
		this.rewind();
		
	}

	@Override
	public boolean hasNext() {
		if (!this.lastValInLastCol()) {
			this.isLastVal = false;
			return true;
		} else {
			this.isLastVal = true;
			this.lastGapInd = this.findLastGapInd();
			if (this.lastGapInd >= 0) {
				return true;
			} else {
				return false;
			}
			
		}
	}

	private boolean lastValInLastCol() {
		return (this.cols[this.t-1] == this.k - 1) ? true : false;
	}
	
	private int findLastGapInd() {
		int i;
		for (i = this.t - 2; i >= 0; i--) {
			if (this.cols[i+1] - this.cols[i] > 1) {
				break;
			}
		}
		
		return i;
	}


	@Override
	public ColGroup next() {
		if (!this.isLastVal) {
			this.cols[t-1]++;
		} else { //this.lastGapInd >= 0
			this.cols[this.lastGapInd]++;
			int v = this.cols[this.lastGapInd];
			for (int i = 1; i < this.t - this.lastGapInd; i++) {
				this.cols[this.lastGapInd + i] = v + i;
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
		int k = 17;
		
		System.out.println("Recursive Memory stored:");
		System.out.println(new Date());
		ColGrIterator ci = new ColGrIterator2(t,k);
		long tot = 0;
		while(ci.hasNext()) {
			//System.out.println(ci.next());
			ci.next();
			tot++;
		}
		System.out.println(new Date());
		System.out.println("total = " + tot);
		
		System.out.println();
		
		System.out.println("Lex order:");
		System.out.println(new Date());
		ci = new ColGrLexIterator(t,k);
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
