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

package edu.asu.ca.kaushik.algorithms.permvector.utils;

import java.util.Arrays;

public class BaseVIterator {
	private int t;
	private int v;
	private int max;
	private int count;
	private int[] beta;
	
	
	/**
	 * Iterator for all the t digit numbers in base v starting with the number v
	 * @param t
	 * @param v
	 */
	public BaseVIterator(int t, int v) {
		this.t = t;
		this.v = v;
		this.max = ((int)(Math.pow(this.v, this.t)) - this.v);
		this.count = 0;
		this.beta = new int[this.t];
		// discard the first v numbers, i.e. start from v-1
		this.beta[0] = this.v - 1;
	}
	
	public boolean hasNext() {
		return (this.count < this.max) ? true : false;
	}
	
	/**
	 * 
	 * @return do not modify the returned array! Use it for read only purpose
	 */
	public int[] next() {
		this.incrementCounter();
		
		this.count++;
		return this.beta;
	}

	private void incrementCounter() {
		int ind = 0;
		while (this.beta[ind] == this.v - 1) {
			this.beta[ind] = 0;
			ind++;
		}
		this.beta[ind]++;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseVIterator it = new BaseVIterator(3,5);
		while(it.hasNext()) {
			System.out.println(Arrays.toString(it.next()));
		}

	}

}
