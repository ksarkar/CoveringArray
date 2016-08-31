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

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.FiniteField;

public class CPHF {
	protected Random rand;
	protected int t;
	protected int k;
	protected int v;
	protected int n;
	
	protected FiniteField f;

	/* The array containing the CPHF.
	 * First two indices point to the entry in the array.
	 * Third index holds the base v representation.
	 */
	protected int[][][] c;
	
	public CPHF() {}
	
	public CPHF(int t, int k, int v) {
		this(CPHF_LLL(t,k,v), k, v, t);
	}
	
	/**
	 * A covering perfect hash family CPHF(n;k,v^t,t).
	 * @param n
	 * @param k
	 * @param v
	 * @param t
	 */
	public static int CPHF_LLL(int t, int k, int v) {
		double vTot = Math.pow(v,t);
		double nume = 1.0d;
		for (int i = 1; i < t; i++) {
			nume = nume * (vTot - Math.pow(v,i));
		}
		
		double dnom = Math.pow(vTot - 1.0d, t-1);
		
		double q = 1.0d - nume / dnom;
		
		double d = CombinatoricsUtils.binomialCoefficientDouble(k, t) 
				- CombinatoricsUtils.binomialCoefficientDouble(k-t, t);
		
		return (int)Math.ceil((1 + Math.log(d)) / (-Math.log(q)));		
	}

	public CPHF(int n, int k, int v, int t) {
		this.rand = new Random();
		
		this.n = n;
		this.k = k;
		this.v = v;
		this.t = t;
		
		this.f = new FiniteField(v);
		
		this.c = new int[this.n][this.k][this.t];
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.k; j++) {
				boolean isAllZero = true; // to avoid the all-zero vector
				do {
					for (int l = 0; l < this.t; l++) {
						this.c[i][j][l] = this.rand.nextInt(this.v);
						if (this.c[i][j][l] != 0) {
							isAllZero = false;
						}
					}
				} while(isAllZero);
			}	
		}
		
	}
	
	public void reSample(ColGroup columns) {
		int[] cols = columns.getCols();
		for (int c : cols) {
			this.sample(c);
		}	
	}
	
	protected void sample(int col) {
		for (int i = 0; i < this.n; i++) {
			for (int l = 0; l < this.t; l++) {
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

	protected boolean isCovering(int row, int[] cols) {
		int[][] A = new int[this.t][this.t];
		
		for (int i = 0; i < this.t; i++) {
			for (int j = 0; j < this.t; j++) {
				A[i][j] = this.c[row][cols[i]][j];
			}
		}
		
		return this.f.isFullRank(A);
	}
	
	public CA convertToCA() {
		int N = this.coveringArraySize(this.n, this.t, this.k, this.v);
		
		// replace entries with shortened permutation vector
		ArrayCA ca = new ArrayCA(this.t, this.k, this.v, N);
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.k; j++) {
				this.replaceEntry(i, j, this.c[i][j], ca);
			}
		}
		
		this.addConstantRows(ca, this.n, this.t, this.k, this.v);
		
		return ca;
	}

	protected void addConstantRows(ArrayCA ca, int n, int t, int k, int v) {
		int row = n * ((int)(Math.pow(v, t)) - 1);
		for (int j = 0; j < this.k; j++) {	
			ca.setCA(row, j, 0);
		}	
	}

	protected int coveringArraySize(int n, int t, int k, int v) {
		return n * ((int)(Math.pow(v, t)) - 1) + 1;
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
		int start = i * ((int)(Math.pow(this.v, this.t)) - (this.getOrigin() - 1));
		int[] a = this.getVector(h);
		int origin = this.getOrigin();
		BaseVIterator it = new BaseVIterator(t, v, origin);
		int rowNum = 0;
		while(it.hasNext()) {
			int[] beta = it.next();
			int s = this.computeDotProd(beta, a);
			ca.setCA(start + rowNum, j, s);
			rowNum++;
		}
	}

	protected int getOrigin() {
		return 2;
	}
	
	protected int[] getVector(int[] h) {
		return h;
	}

	private int computeDotProd(int[] beta, int[] a) {
		int dotProd = 0;
		for (int i = 0; i < this.t; i++) {
			dotProd = this.f.add(dotProd, this.f.multiply(beta[i] , a[i]));
		}
		return dotProd;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(CPHF_LLL(6,100,5));
	}

	

	

	

}
