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

import java.util.Random;

public class LLLCA extends ArrayCA {
	private Random rand;
	
	public LLLCA(int t, int k, int v, int N, Random rand) {	
		super(t,k,v,N);
		this.rand = rand;
		
		for (int i = 0; i < super.N; i++) {
			for (int j = 0; j < this.k; j++) {
				super.ca[i][j] = this.rand.nextInt(this.v);
			}
		}
		/*
		for (int i = 0; i < k; i++) {
			this.sample(i);
		}
		*/
	}
	
	public LLLCA(int t, int k, int v, int N) {
		this(t,k,v,N, new Random(123456L));
	}

	public LLLCA(int t, int k, int v) {
		this(t, k, v, computeNumRows(t, k, v));
	}
	
	public LLLCA(int t, int k, int v, int n, int r, Random random) {
		super(t,k,v,n,r);
		this.rand = random;
		
		for (int i = 0; i < super.N; i++) {
			for (int j = 0; j < this.k; j++) {
				super.ca[i][j] = this.rand.nextInt(this.v);
			}
		}
	}

	private static int computeNumRows(int t, int k, int v) {
		double lllBound = Helper.computeLLLBound(t, k, v);
		return (int)Math.ceil(lllBound);
	}
	
	public void reSample(ColGroup columns) {
		int[] cols = columns.getCols();
		for (int c : cols) {
			this.sample(c);
		}
	}
	
	private void sample(int col) {
		for (int i = 0; i < this.N; i++) {
			super.ca[i][col] = this.rand.nextInt(this.v);
		}
	}

}
