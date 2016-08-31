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
import edu.asu.ca.kaushik.algorithms.structures.FiniteField;

public class SCPHF extends CPHF {

	public SCPHF(int t, int k, int v) {
		this(SCPHF_LLL(t,k,v), k, v, t);
	}
	
	public static int SCPHF_LLL(int t, int k, int v) {
		double covP = 1.0d;
		
		for (int i = 1; i < t; i++) {
			covP = covP * (1.0d - 1 / Math.pow(v, i));
		}
		
		double q = 1.0d - covP;
		
		double d = CombinatoricsUtils.binomialCoefficientDouble(k, t) 
				- CombinatoricsUtils.binomialCoefficientDouble(k-t, t);
		
		return (int)Math.ceil((1 + Math.log(d)) / (-Math.log(q)));	
	}
	
	/**
	 * A Sherwood covering perfect hash family CPHF(n;k,v^(t-1),t).
	 * @param n
	 * @param k
	 * @param v
	 * @param t
	 */
	public SCPHF(int n, int k, int v, int t) {
		super.rand = new Random();
		
		super.n = n;
		super.k = k;
		super.v = v;
		super.t = t;
		
		super.f = new FiniteField(v);
		
		super.c = new int[super.n][super.k][super.t - 1];
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.k; j++) {
				for (int l = 0; l < this.t - 1; l++) {
					super.c[i][j][l] = super.rand.nextInt(super.v);
				}
			}	
		}
	}
	
	@Override
	protected boolean isCovering(int row, int[] cols) {
		int[][] A = new int[super.t][super.t];
		
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
	
	@Override
	protected void sample(int col) {
		for (int i = 0; i < this.n; i++) {
			for (int l = 0; l < this.t - 1; l++) {
				this.c[i][col][l] = this.rand.nextInt(this.v);
			}
		}
	}
	
	@Override
	protected int coveringArraySize(int n, int t, int k, int v) {
		return n * ((int)(Math.pow(v, t)) - v) + v;
	}
	
	@Override
	protected void addConstantRows(ArrayCA ca, int n, int t, int k, int v) {
		int row = n * ((int)(Math.pow(v, t)) - v);
		for (int s = 0; s < this.v; s++) {
			for (int j = 0; j < this.k; j++) {	
				ca.setCA(row + s, j, s);
			}
		}		
	}
	
	protected int getOrigin() {
		return super.v + 1;
	}
	
	@Override
	protected int[] getVector(int[] h) {
		int[] a = new int[super.t];
		a[0] = 1;
		for (int i = 0 ; i < super.t - 1; i++) {
			a[i + 1] = h[i];
		}
		
		return a;
	}

}
