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

package edu.asu.ca.kaushik.da.algorithms;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.da.algorithms.density.DensityV1;
import edu.asu.ca.kaushik.da.scripts.Runner;
import edu.asu.ca.kaushik.da.structures.DAIface;
import edu.asu.ca.kaushik.da.structures.Misc;
import edu.asu.ca.kaushik.da.structures.RowDA;
import edu.asu.ca.kaushik.da.structures.dColGrIterator;
import edu.asu.ca.kaushik.da.structures.dColGrLexIterator;
import edu.asu.ca.kaushik.da.structures.dColGroup;
import edu.asu.ca.kaushik.da.structures.dSymTuple;

public class MDensityV2 implements DAAlgoIface {

	@Override
	public DAIface constructDetectingArray(int d, int t, int k, int v) {
		DAIface da = new RowDA(d, t, k, v);
		double[][][] countTab = new double[v][k][(d+1) * t];
		this.recompute(da, countTab, d, t, k);
		
		int rn = 1;
		do {
			int[] newRow = this.constructRow(da, countTab, d, t, k);
			//System.out.println("New Cover: " + da.countCovered(newRow));
			System.out.println("Row number: " + rn++);
			da.addRow(newRow);
			this.recompute(da, countTab, d, t, k);
		} while (!this.isAllZero(countTab, d, t, k, v));
		
		return da;
	}

	@Override
	public DAIface constructMixedDetectingArray(int d, int t, int k, int[] v) {
		if (k > v.length) {
			System.out.println("Number of levels for all the factors not given.");
			System.exit(1);
		}
		
		DAIface da = new RowDA(d, t, k, v);
		int v_max = Misc.findMax(v);
		double[][][] countTab = new double[v_max][k][(d+1) * t];
		this.recompute(da, countTab, d, t, k);
		
		int rn = 1;
		do {
			int[] newRow = this.constructRow(da, countTab, d, t, k);
			//System.out.println("New Cover: " + da.countCovered(newRow));
			System.out.println("Row number: " + rn++);
			da.addRow(newRow);
			this.recompute(da, countTab, d, t, k);
		} while (!this.isAllZero(countTab, d, t, k, v_max));
		
		return da;
	}
	
	private boolean isAllZero(double[][][] countTab, int d, int t, int k, int v) {
		for (int i = 0; i < v; i++) {
			for (int j = 0; j < k; j++) {
				for (int l = 0; l < (d+1)*t; l++) {
					if (countTab[i][j][l] > 0.0d) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private int[] constructRow(DAIface da, double[][][] countTab, int d, int t, int k) {
		int[] levels;
		int vMax;
		if (da.isMixedFlagUP()) {
			levels = da.getVArray();
			vMax = Misc.findMax(levels);
		} else {
			vMax = da.getV();
			levels = Misc.makeStarredRow(k, vMax);
		}
		
		int[] row = Misc.makeStarredRow(k,vMax);
		
		for (int i = 0; i < k; i++) {
			int bestCol = -1;
			int bestSym = -1;
			double bestExpCov = 0;
			for (int j = 0; j < k; j++) {
				if (row[j] == vMax) {
					for (int sym = 0; sym < levels[j]; sym++) {
						double expCov = this.computeExpCov(countTab[sym][j]); 
						if (expCov > bestExpCov) {
							bestCol = j;
							bestSym = sym;
							bestExpCov = expCov;
						}
					}
					
				}
			}
			//System.out.println(bestExpCov);
			if (bestCol >= 0) {
				row[bestCol] = bestSym;
				this.updateCount(da, countTab, row, bestCol);
			} else {
				break;
			}
		}
		return row;
	}
	
	private void updateCount(DAIface da, double[][][] countTab, int[] row, int newInd) {
		int d = da.getD();
		int t = da.getT();
		int k = da.getK();
		
		ColGrIterator colIt = new ColGrLexIterator(t, k);
		dColGrIterator dColIt = new dColGrLexIterator(d, t, k);
		while(colIt.hasNext()) {
			ColGroup cols = colIt.next();
			dColIt.rewind();
			while (dColIt.hasNext()) {
				dColGroup dCols = dColIt.next();
				if (cols.contains(newInd) || dCols.contains(newInd)) {
					Map<SymTuple, Set<dSymTuple>> map = da.getSymTupMap(cols, dCols);
					this.updateCount(map, countTab, row, cols, dCols, da, newInd);
				}
			}
		}
		
	}
	
	private void updateCount(Map<SymTuple, Set<dSymTuple>> map,
			double[][][] countTab, int[] row, ColGroup cols, dColGroup dCols, DAIface da, int newInd) {
		int[] V;
		int vMax;
		if (da.isMixedFlagUP()) {
			V = da.getVArray();
			vMax = Misc.findMax(V);
		} else {
			vMax = da.getV();
		}
		
		int[] colIndices = dCols.union(cols);
		int[] freeIndices = this.getFreeIndices(row, colIndices, vMax);
		int freeIndNum = freeIndices.length;
		
		if (freeIndNum > 0) {
			int[] rowCopy = Arrays.copyOf(row, row.length);
			List<SymTuple> allBlankEntries;
			if (da.isMixedFlagUP()) {
				V = da.getVArray();
				int[] l = Misc.getLevels(new ColGroup(freeIndices), V);
				allBlankEntries = Misc.getSymTupList(l);
			} else {
				allBlankEntries = Helper.createAllSymTuples(freeIndNum, vMax);
			}
			for (SymTuple tuple : allBlankEntries) {
				int[] entries = tuple.getSyms();
				Misc.setEntries(rowCopy, freeIndices, entries);
				SymTuple s = Misc.getSyms(cols, rowCopy);
				dSymTuple ds = Misc.getdSyms(dCols, rowCopy);
				int c = Misc.count(map, s, ds);
				this.update(countTab, freeIndices, entries, c, newInd, da);
			}
		}
	}
	
	private void update(double[][][] countTab, int[] freeIndices, int[] entries,
			int c, int newInd, DAIface da) {
		
		int freeIndNum = freeIndices.length;
		double prod = 0.0d;
		if (da.isMixedFlagUP()) {
			prod = this.productOfLevels(da.getVArray(), freeIndices);
		} else {
			prod = Math.pow(da.getV(), freeIndNum);
		}
		
		for  (int i = 0; i < freeIndNum; i++) {
			double d1 = 0.0, d2 = 0.0d;
			if (da.isMixedFlagUP()) {
				int[] levels = da.getVArray();
				d2 = prod / levels[freeIndices[i]];
				d1 = d2 * levels[newInd];
			} else {
				d1 = prod;
				d2 = prod / da.getV();
			}
			
			countTab[entries[i]][freeIndices[i]][freeIndNum] = countTab[entries[i]][freeIndices[i]][freeIndNum] - (c/d1);
			countTab[entries[i]][freeIndices[i]][freeIndNum-1] = countTab[entries[i]][freeIndices[i]][freeIndNum-1] + (c/d2);		
		}		
	}
	
	private int[] getFreeIndices(int[] row, int[] colIndices, int v) {
		int freeIndNum = 0;
		for (int ind : colIndices) {
			if (row[ind] == v) {
				freeIndNum++;
			}
		}
		
		int[] freeIndices = new int[freeIndNum];
		int i = 0;
		for (int ind : colIndices) {
			if (row[ind] == v) {
				freeIndices[i] = ind;
				i++;
			}
		}
		return freeIndices;
	}

	private double computeExpCov(double[] count) {
		int l = count.length;
		double exp = 0;
		for (int i = 0; i < l; i++) {
			exp = exp + count[i]; 
		}
		return exp;
	}
	
	private void recompute(DAIface da, double[][][] countTab, int d, int t, int k) {
		int v;
		int[] V;
		if (da.isMixedFlagUP()) {
			V = da.getVArray();
			v = Misc.findMax(V);
		} else {
			v = da.getV();
		}
		this.reset(countTab, d, t, k, v);
		
		ColGrIterator colIt = new ColGrLexIterator(t, k);
		dColGrIterator dColIt = new dColGrLexIterator(d, t, k);
		while(colIt.hasNext()) {
			ColGroup cols = colIt.next();
			dColIt.rewind();
			while (dColIt.hasNext()) {
				dColGroup dCols = dColIt.next();
				
				Map<SymTuple, Set<dSymTuple>> map = da.getSymTupMap(cols, dCols);
				this.updateTab(map, countTab, cols, dCols, k, da);
			}
		}		
	}
	
	private void reset(double[][][] countTab, int d, int t, int k, int v) {
		for (int i = 0; i < v; i++) {
			for (int j = 0; j < k; j++) {
				for (int l = 0; l < (d+1)*t; l++) {
					countTab[i][j][l] = 0.0d;
				}
			}
		}
		
	}
	
	private void updateTab(Map<SymTuple, Set<dSymTuple>> map,
			double[][][] countTab, ColGroup cols, dColGroup dCols, int k, DAIface da) {
		
		int[] colIndices = dCols.union(cols);
		int[] row = new int[k];
		List<SymTuple> allBlankEntries;
		if (da.isMixedFlagUP()) {
			int[] V = da.getVArray();
			int[] levels = Misc.getLevels(new ColGroup(colIndices), V);
			allBlankEntries = Misc.getSymTupList(levels);
		} else {
			int v = da.getV();
			allBlankEntries = Helper.createAllSymTuples(colIndices.length, v);
		}
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			Misc.setEntries(row, colIndices, entries);
			SymTuple s = Misc.getSyms(cols, row);
			dSymTuple ds = Misc.getdSyms(dCols, row);
			int c = Misc.count(map, s, ds);
			this.updateTabEntries(countTab, colIndices, entries, c, da);
		}
	}
	
	private void updateTabEntries(double[][][] countTab, int[] colIndices,
			int[] entries, int c, DAIface da) {
		
		int freeIndNum = colIndices.length - 1;
		double prod = 0.0d;
		if (da.isMixedFlagUP()) {
			prod = this.productOfLevels(da.getVArray(), colIndices);
		} else {
			prod = Math.pow(da.getV(), freeIndNum + 1);
		}
		for  (int i = 0; i < colIndices.length; i++) {
			double d = 0.0;
			if (da.isMixedFlagUP()) {
				d = prod / da.getVArray()[colIndices[i]];
			} else {
				d = prod / da.getV();
			}
			countTab[entries[i]][colIndices[i]][freeIndNum] = countTab[entries[i]][colIndices[i]][freeIndNum] + (c / d);		
		}
	}


	private double productOfLevels(int[] levels, int[] colIndices) {
		double prod = 1.0d;
		for (int i : colIndices) {
			prod = prod * levels[i];
		}
		return prod;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		/*DAAlgoIface algo = new MDensityV2();
		DAIface da = algo.constructDetectingArray(1, 2, 20, 2);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isDetectingArray());*/
		
		/*DAAlgoIface algo = new MDensityV2();
		int[] v = new int[]{2,2,3,3};
		DAIface da = algo.constructMixedDetectingArray(1, 2, 4, v);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isMixedDetectingArray());*/
		
		DAAlgoIface algo = new MDensityV2();
		int d = 1; int t = 2; int k = 24;
		//int[] v = Misc.getAldacoLevels(); // k = 75
		//int[] v = new int[]{2,2,2,2,2,3,4,4,4};
		//int[] v = new int[]{4,4,4,3,3,3,2,2,2};
		int[] v = new int[]{2,2,2,3,3,3,3,3,3,3,4,4,4,4,4,5,5,5,5,5,5,5,5,5};
		//int[] v = new int[]{3,3,5,5,5,5,3,4,5,3,5,4,3,5,4,3,5,4,2,2,4,3,5,2};
		String dirName = "data\\out\\da\\explicit";
		Runner.printAlgo(algo, d, t, k, v, dirName, "MDensityV2-sorted");
		//DAIface da = algo.constructMixedDetectingArray(1, 2, 9, v);
		
		/*DAAlgoIface algo = new MDensityV2();
		int d = 1; int t = 2; int k = 9;
		int v = 4;
		String dirName = "data\\out\\da\\explicit";
		Runner.printAlgo(algo, d, t, k, v, dirName, "density-v1");*/
		
		/*int d = 1, t = 0, k1 = 0, k2 = 0;
		
		if (args.length == 3) {
			t = Integer.parseInt(args[0]);
			k1 = Integer.parseInt(args[1]);
			k2 = Integer.parseInt(args[2]);
		} else {
			System.err.println("Need four arguments- t, kStart, and kEnd.");
			System.exit(1);
		}
		
		DAAlgoIface algo = new MDensityV2();
		String dirName = "data\\out\\da";
		
		String fileName = "MDensityV2";
		Runner.runMixed(algo, d, t, k1, k2, dirName, fileName);*/
	}

}
