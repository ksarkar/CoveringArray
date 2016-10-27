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
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.da.structures.DAIface;
import edu.asu.ca.kaushik.da.structures.RowDA;

public class RandRowAlgoV2 implements DAAlgoIface {
	private Random rand = new Random(12345L);
	private int numSamples = 1000;

	@Override
	public DAIface constructDetectingArray(int d, int t, int k, int v) {
		DAIface da = new RowDA(d, t, k, v);
		
		while (!da.isDetectingArray()) {
			long coverage = 0;
			int[] bestRow = null;
			for (int i = 0; i < this.numSamples; i++) {
				int[] row = Helper.constructRandRow1(k, v, rand);
				long c = da.countCovered(row);
				//System.out.println("Coverage: " + c);
				if (c >= coverage) {
					coverage = c;
					bestRow = row;
				}
			}
			System.out.println("Best coverage: " + coverage);
			da.addRow(bestRow);			
		}
		
		return da;
	}

	@Override
	public DAIface constructMixedDetectingArray(int d, int t, int k, int[] v) {
		if (k > v.length) {
			System.out.println("Number of levels for all the factors not given.");
			System.exit(1);
		}
		
		DAIface da = new RowDA(d, t, k, v);
		
		int rowNum = 1;
		while (!da.isMixedDetectingArray()) {
			long coverage = 0;
			int[] bestRow = null;
			for (int i = 0; i < this.numSamples; i++) {
				int[] row = Helper.constructMixedRandRow(k, v, rand);
				long c = da.countCovered(row);
				System.out.println("Row: " + rowNum + "; Coverage: " + c);
				if (c > coverage) {
					coverage = c;
					bestRow = row;
				}
			}
			System.out.println("Best coverage: " + coverage);
			da.addRow(bestRow);
			rowNum++;
		}
		
		return da;
	}

	public static void main(String[] args) throws IOException {
		DAAlgoIface algo = new RandRowAlgoV2();
		DAIface da = algo.constructDetectingArray(1, 2, 10, 2);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isDetectingArray());
		
		/*DAAlgoIface algo = new RandRowAlgoV2();
		int[] v = new int[]{2,2,3,3};
		DAIface da = algo.constructMixedDetectingArray(1, 2, 4, v);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isMixedDetectingArray());*/
		
		/*DAAlgoIface algo = new RandRowAlgoV2();
		int d = 1; int t = 2; int k = 50;
		//int[] v = new int[]{2,2,3,3};
		int[] v = Misc.getAldacoLevels();
		String dirName = "data\\out\\da\\explicit";
		Runner.printAlgo(algo, d, t, k, v, dirName);*/
	}

}
