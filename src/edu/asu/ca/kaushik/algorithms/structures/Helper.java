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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;

public class Helper {
	public static List<SymTuple> createAllSymTuples(int t, int v) {
		if (t == 0){
			List<SymTuple> list = new ArrayList<SymTuple>();
			list.add(new SymTuple(new int[0]));
			return list;
		}
		else {
			List<SymTuple> small = createAllSymTuples(t-1, v);
			
			List<SymTuple> big = new ArrayList<SymTuple>();
			for (SymTuple tuple : small) {
				for (int i = 0; i < v; i++) {
					big.add(new SymTuple(tuple, i));
				}
			}
			return big;
		}
	}
	
	public static List<int[]> createAllTuples(int t, int v) {
		if (t == 0){
			List<int[]> list = new ArrayList<int[]>();
			list.add(new int[0]);
			return list;
		}
		else {
			List<int[]> small = createAllTuples(t-1, v);
			
			List<int[]> big = new ArrayList<int[]>();
			for (int[] tuple : small) {
				for (int i = 0; i < v; i++) {
					big.add(stich(tuple, i));
				}
			}
			return big;
		}
	}
	
	private static int[] stich(int[] small, int i) {
		int t = small.length + 1;
		int[] big = Arrays.copyOfRange(small, 0, t);
		big[t-1] = i;
		return big;
	}

	public static List<ColGroup> createAllColGroups(int t, int k) {
		if (t == 0){
			List<ColGroup> list = new ArrayList<ColGroup>();
			list.add(new ColGroup(new int[0]));
			return list;
		}
		else {
			List<ColGroup> small = createAllColGroups(t-1, k);
			
			List<ColGroup> big = new ArrayList<ColGroup>();
			for (ColGroup tuple : small) {
				for (int i = 0; i < k; i++) {
					if (shouldInclude(tuple.getCols(), i)) {
						big.add(new ColGroup(tuple, i));
					}
				}
			}
			return big;
		}
	}
	
	private static boolean shouldInclude(int[] a, int i){
		return (a.length == 0)? true : a[a.length - 1] < i; 
		// the columns are stored in increasing order of index value
	}

	private static long[] overlapCountA = null;
	private static int st = 0;
	private static int sk = 0;
	private static int sv = 0;
	
	public static long[] getNewOverlapCountA(int t, int k, int v) {
		long[] a;
		if ((overlapCountA == null) || (st != t) || (sk != k) || (sv != v)) {
			overlapCountA = new long[t];
			for (int i = Math.max(0, 2*t - k); i < t; i++) {
				overlapCountA[i] = getOverlapCount(t, k, v, i);
			}
		}
		a = Arrays.copyOf(overlapCountA, t);
		return a;
	}

	private static long getOverlapCount(int t, int k, int v, int i) {
		return CombinatoricsUtils.binomialCoefficient(t, i) 
				* CombinatoricsUtils.binomialCoefficient(k - t, t - i)
				* ArithmeticUtils.pow(v, t - i);
	}
	
	public static double computeLLLBound(int t, int k, int v) {
		/*
		 * N >= (1 + log(choose(k,t)v^t - choose(k-t,t)v^t + 1)) / log(v^t/(v^t - 1))
		 */
		double vpowt = Math.pow(v, t);
		double bound = CombinatoricsUtils.binomialCoefficientDouble(k, t) * vpowt
				- CombinatoricsUtils.binomialCoefficientDouble(k - t, t) * vpowt
				+ 1;
		bound = Math.log(bound) + 1;
		bound = bound / (Math.log(vpowt/(vpowt - 1)));
		return bound;
	}

	public static double expectNumUncovInt(int t, int k, int v, int N) {
		/*
		 * expected number of uncovered interactions = choose(k,t) * v^t * (1 - 1/v^t)^N
		 */
		double vpowt = Math.pow(v, t);
		double probUncov = 1 - (1 / vpowt);
		return CombinatoricsUtils.binomialCoefficientDouble(k,t) * vpowt 
				* Math.pow(probUncov, N);
	}
	
	public static Integer[] constructRandRow(int k, int v, Random rand) {
		Integer[] row = new Integer[k];
		for (int i = 0 ; i < k; i++) {
			row[i] = rand.nextInt(v);
		}
		
		return row;
	}
	
	public static int[] constructRandRow1(int k, int v, Random rand) {
		int[] row = new int[k];
		for (int i = 0 ; i < k; i++) {
			row[i] = rand.nextInt(v);
		}
		
		return row;
	}

	public static int[] constructMixedRandRow(int k, int[] v, Random rand) {
		int[] row = new int[k];
		
		for (int i = 0 ; i < k; i++) {
			row[i] = rand.nextInt(v[i]);
		}
		
		return row;
	}
	
}
