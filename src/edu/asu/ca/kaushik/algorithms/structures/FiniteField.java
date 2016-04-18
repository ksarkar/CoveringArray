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

import java.util.Arrays;

public class FiniteField {
	private int v;
	private int[][] mTab;
	private int[] inv;

	public FiniteField(int v) {
		assert(this.isPrime(v)); // only works if v is a prime number
		
		this.v = v;
		this.mTab = new int[this.v][this.v];
		this.inv = new int[this.v];
		this.computeMultiplicationTable(); // constructs the multiplication table and inverse array
	}
	
	private boolean isPrime(int v) {
		// TODO code for checking prime or prime power
		return true;
	}

	private void computeMultiplicationTable() {
		for (int i = 0; i < this.v; i++) {
			for (int j = 0; j < this.v; j++) {
				this.mTab[i][j] = (i * j) % this.v;
				if (this.mTab[i][j] == 1) {
					this.inv[i] = j;
				}
			}
		}
		
	}
	
	/**
	 * Finite field addition
	 * @param i, 0<=i<=v-1
	 * @param j, 0<=j<=v-1
	 * @return (u + v) mod v
	 */
	public int add(int i, int j) {
		assert((i < this.v) && (i >= 0) && (j < this.v) && (j >= 0));
		//assert((i < this.v) && (j < this.v));
		return (i + j) % this.v;		
	}
	
	/**
	 * Finite field subtraction
	 * @param i, 0<=i<=v-1
	 * @param j, 0<=j<=v-1
	 * @return (i - j) mod v
	 */
	public int subtract(int i, int j) {
		assert((i < this.v) && (i >= 0) && (j < this.v) && (j >= 0));
		assert((i < this.v) && (j < this.v));
		return (i + this.v - j) % this.v;
	}
	
	/**
	 * Finite field multiplication
	 * @param i, 0<=i<=v-1
	 * @param j, 0<=j<=v-1
	 * @return (i * j) mod v
	 */
	public int multiply(int i, int j) {
		assert((i < this.v) && (i >= 0) && (j < this.v) && (j >= 0));
		//assert((i < this.v) && (j < this.v));
		return this.mTab[i][j];
	}
	
	/**
	 * Finite field division
	 * @param i, 0<=i<=v-1
	 * @param j, 0<=j<=v-1
	 * @return (i / j) mod v
	 */
	public int divide(int i, int j) {
		assert((i < this.v) && (i >= 0) && (j < this.v) && (j >= 0));
		//assert((i < this.v) && (j < this.v));
		return this.mTab[i][this.inv[j]];
	}
 
	/**
	 * Gaussian elimination to determine the rank of a
	 * @param a
	 * @return TRUE if a is full rank; else FALSE
	 */
	public boolean isFullRank(int[][] a) {
		int m = a.length;
		int n = a[0].length;
		assert(m == n);
		
		for (int i = 0; i < n; i++) {
			//System.out.println(printMatrix(a));
			if (a[i][i] == 0) {
				int j = this.canExchage(a, i);
				if (j < i) {
					return false; // row exchange cannot be done
				} else {
					this.rowExchange(a, i, j);
				}
			}
			// a[i][i] is the pivot (it is non-zero)
			this.eliminate(a, i); // eliminates all the numbers below a[i][i]
		}
		return true;
	}
	
	private void eliminate(int[][] a, int i) {
		int n = a.length;
		for (int j = i + 1; j < n; j++) {
			if (a[j][i] != 0) {
				this.eliminateRow(a, i, j);
			}
		}
	}

	private void eliminateRow(int[][] a, int i, int j) {
		int p = this.divide(a[j][i], a[i][i]);
		int n = a.length;
		for (int k = i; k < n; k++) {
			a[j][k] = this.subtract(a[j][k], this.multiply(p, a[i][k]));
		}
	}

	private void rowExchange(int[][] a, int i, int j) {
		int n = a.length;
		int temp;
		for (int k = 0; k < n; k++) {
			temp = a[i][k];
			a[i][k] = a[j][k];
			a[j][k] = temp;
		}
	}

	private int canExchage(int[][] a, int i) {
		int n = a.length;
		int fail = i - 1;
		for (int j = i + 1; j < n; j++) {
			if (a[j][i] != 0) {
				return j;
			}
		}
		return fail;
	}

	public static String printMatrix(int[][] a) {
		StringBuilder s = new StringBuilder();
		int m = a.length;
		int n = a[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				s.append(a[i][j] + " ");
			}
			s.append("\n");
		}
		return s.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FiniteField f = new FiniteField(5);
		System.out.println(printMatrix(f.mTab));
		System.out.println(Arrays.toString(f.inv));
		System.out.println(f.add(2, 1));
		//System.out.println(f.subtract(-3, 1));
		//System.out.println(f.subtract(-2, 3));
		System.out.println(f.divide(4, 3));
		
		int[][] a = {{1, 2, 3}, {2, 4, 2}, {3, 2, 4}};
		System.out.println(f.isFullRank(a));
	}

}
