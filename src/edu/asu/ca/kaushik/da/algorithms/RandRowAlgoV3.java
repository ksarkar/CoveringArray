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
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.da.scripts.Runner;
import edu.asu.ca.kaushik.da.structures.DAIface;
import edu.asu.ca.kaushik.da.structures.Misc;
import edu.asu.ca.kaushik.da.structures.RowDA;
import edu.asu.ca.kaushik.da.structures.dColGrIterator;
import edu.asu.ca.kaushik.da.structures.dColGrLexIterator;
import edu.asu.ca.kaushik.da.structures.dColGroup;
import edu.asu.ca.kaushik.da.structures.dSymTuple;

public class RandRowAlgoV3 implements DAAlgoIface {
	private Random rand = new Random(12345L);
	private int numSamples = 50;
	
	@Override
	public DAIface constructDetectingArray(int d, int t, int k, int v) {
		DAIface da = new RowDA(d, t, k, v);
		
		while (!da.isDetectingArray()) {
			int[][] rows = new int[this.numSamples][k];
			int[] cov = new int[this.numSamples];
			
			for (int i = 0; i < this.numSamples; i++) {
				for (int j = 0; j < k; j++) {
					rows[i][j] = this.rand.nextInt(v);
				}
			}
			
			this.countCovered(rows,da, cov);
			int maxInd = this.findMaxInd(cov);
			System.out.println("Best coverage: " + cov[maxInd]);
			int[] bestRow = new int[k];
			for (int i = 0; i < k; i++) {
				bestRow[i] = rows[maxInd][i];
			}
			da.addRow(bestRow);			
		}
		
		return da;
	}

	private void countCovered(int[][] rows, DAIface da, int[] cov) {
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
				
				Map<SymTuple, Set<dSymTuple>> map = da.getSymTupMap(cols, dCols);
				for(int i = 0; i < this.numSamples; i++) {
					SymTuple s = Misc.getSyms(cols, rows[i]);
					dSymTuple ds = Misc.getdSyms(dCols, rows[i]);
					cov[i] = cov[i] + Misc.count(map, s, ds);
				}
			}
		}		
	}

	private int findMaxInd(int[] cov) {
		int k = cov.length;
		int max = 0;
		int maxInd = 0;
		for (int i = 0; i < k; i++) {
			if (cov[i] > max) {
				max = cov[i];
				maxInd = i;
			}
		}
		return maxInd;
	}

	@Override
	public DAIface constructMixedDetectingArray(int d, int t, int k, int[] v) {
		DAIface da = new RowDA(d, t, k, v);
		
		int rowNum = 1;
		while (!da.isMixedDetectingArray()) {
			int[][] rows = new int[this.numSamples][k];
			int[] cov = new int[this.numSamples];
			
			for (int i = 0; i < this.numSamples; i++) {
				for (int j = 0; j < k; j++) {
					rows[i][j] = this.rand.nextInt(v[j]);
				}
			}
			
			this.countCovered(rows,da, cov);
			int maxInd = this.findMaxInd(cov);
			System.out.println("Row " + rowNum++ + "; Best coverage: " + cov[maxInd]);
			int[] bestRow = new int[k];
			for (int i = 0; i < k; i++) {
				bestRow[i] = rows[maxInd][i];
			}
			da.addRow(bestRow);			
		}
		
		return da;
	}
	
	public static void main(String[] args) throws IOException {
		/*DAAlgoIface algo = new RandRowAlgoV3();
		DAIface da = algo.constructDetectingArray(1, 2, 27, 2);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isDetectingArray());*/
		
		/*DAAlgoIface algo = new RandRowAlgoV3();
		int[] v = new int[]{2,2,3,3};
		DAIface da = algo.constructMixedDetectingArray(1, 2, 4, v);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isMixedDetectingArray());*/
		
		DAAlgoIface algo = new RandRowAlgoV3();
		int d = 1; int t = 2; int k = 24;
		//int[] v = Misc.getAldacoLevels(); // k = 75
		//int[] v = new int[]{2,2,2,3,3,3,4,4,4};
		//int[] v = new int[]{4,4,4,3,3,3,2,2,2};
		int[] v = new int[]{2,2,2,3,3,3,3,3,3,3,4,4,4,4,4,5,5,5,5,5,5,5,5,5};
		//int[] v = new int[]{3,3,5,5,5,5,3,4,5,3,5,4,3,5,4,3,5,4,2,2,4,3,5,2};
		String dirName = "data\\out\\da\\explicit";
		Runner.printAlgo(algo, d, t, k, v, dirName, "randRowV3-50-r-sorted");
		
		/*int d = 1, t = 0, k1 = 0, k2 = 0;
		
		if (args.length == 3) {
			t = Integer.parseInt(args[0]);
			k1 = Integer.parseInt(args[1]);
			k2 = Integer.parseInt(args[2]);
		} else {
			System.err.println("Need four arguments- t, kStart, and kEnd.");
			System.exit(1);
		}
		
		DAAlgoIface algo = new RandRowAlgoV3();
		String dirName = "data\\out\\da";
		
		String fileName = "RandRowAlgoV3";
		Runner.runMixed(algo, d, t, k1, k2, dirName, fileName);*/
	}

}
