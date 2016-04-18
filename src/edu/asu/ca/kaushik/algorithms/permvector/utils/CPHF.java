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

import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.FiniteField;

public class CPHF {
	private Random rand;
	private int t;
	private int k;
	private int v;
	private int n;
	
	private FiniteField f;

	/* The array containing the CPHF.
	 * First two indices point to the entry in the array.
	 * Third index holds the base v representation.
	 */
	int[][][] c;
	
	/**
	 * A covering perfect hash family CPHF(n;k,v^(t-1),t).
	 * @param n
	 * @param k
	 * @param v
	 * @param t
	 */
	public CPHF(int n, int k, int v, int t) {
		this.rand = new Random();
		
		this.n = n;
		this.k = k;
		this.v = v;
		this.t = t;
		
		this.f = new FiniteField(v);
		
		this.c = new int[this.n][this.k][this.t - 1];
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.k; j++) {
				for (int l = 0; l < this.t - 1; l++) {
					this.c[i][j][l] = this.rand.nextInt(this.v);
				}
			}	
		}
		
	}
	
	public void reSample(ColGroup columns) {
		int[] cols = columns.getCols();
		for (int c : cols) {
			this.sample(c);
		}	
	}
	
	private void sample(int col) {
		for (int i = 0; i < this.n; i++) {
			for (int l = 0; l < this.t - 1; l++) {
				this.c[i][col][l] = this.rand.nextInt(this.v);
			}
		}
	}
	
	public boolean containsCoveringTuple(ColGroup colGr) {
		int[] cols = colGr.getCols();
		assert(cols.length == this.t);
		
		for (int i = 0; i < this.n; i++) {
			if (this.isCovering(i, cols)) {
				return true;
			}
		}
		
		return false;
	}

	private boolean isCovering(int row, int[] cols) {
		int[][] A = new int[this.t][this.t];
		
		for (int i = 0; i < this.t; i++) {
			A[i][0] = 1;
		}
		for (int i = 0; i < this.t; i++) {
			for (int j = 1; j < this.t; j++) {
				A[i][j] = this.c[row][cols[i]][j-1];
			}
		}
		
		return this.f.isFullRank(A);
	}
	
	public CA convertToCA() {
		int N = this.n * ((int)(Math.pow(this.v, this.t)) - this.v) + this.v;
		
		// replace entries with shortened permutation vector
		ArrayCA ca = new ArrayCA(this.t, this.k, this.v, N);
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.k; j++) {
				this.replaceEntry(i, j, this.c[i][j], ca);
			}
		}
		
		// add the constant rows
		int row = this.n * ((int)(Math.pow(this.v, this.t)) - this.v);
		for (int s = 0; s < this.v; s++) {
			for (int j = 0; j < this.k; j++) {	
				ca.setCA(row + s, j, s);
			}
		}
		
		return ca;
	}

	/**
	 * Replace the (i,j)-th entry of the CPHF by the permutation vector
	 * indexed by h in the covering array ca
	 * @param i
	 * @param j
	 * @param h
	 * @param ca
	 */
	private void replaceEntry(int i, int j, int[] h, ArrayCA ca) {
		int start = i * ((int)(Math.pow(this.v, this.t)) - this.v);
		BaseVIterator it = new BaseVIterator(t, v);
		int rowNum = 0;
		while(it.hasNext()) {
			int[] beta = it.next();
			int s = this.computeDotProd(beta, h);
			ca.setCA(start + rowNum, j, s);
			rowNum++;
		}
	}

	private int computeDotProd(int[] beta, int[] h) {
		int dotProd = beta[0];
		for (int i = 0; i < this.t - 1; i++) {
			dotProd = f.add(dotProd, this.f.multiply(beta[i + 1] , h[i]));
		}
		return dotProd;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

	

	

}
