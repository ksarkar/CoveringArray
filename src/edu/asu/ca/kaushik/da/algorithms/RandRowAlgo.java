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

import edu.asu.ca.kaushik.da.scripts.Runner;
import edu.asu.ca.kaushik.da.structures.CovTabIface;
import edu.asu.ca.kaushik.da.structures.DAIface;
import edu.asu.ca.kaushik.da.structures.Misc;

public class RandRowAlgo extends RowAlgo {
	private int numSamples = 100;
	private Random rand = new Random(12345L);

	@Override
	protected int[] constructRow(CovTabIface covTab) {
		int k = covTab.getk();
		int v = covTab.getv();
		
		long coverage = 0;
		int[] bestRow = null;
		for (int i = 0; i < this.numSamples; i++) {
			int[] row = this.constructRandRow(k,v);
			long c = covTab.countCovered(row);
			if (c >= coverage) {
				coverage = c;
				bestRow = row;
			}
		}
		System.out.println("Best coverage: " + coverage);
		return bestRow;
	}

	private int[] constructRandRow(int k, int v) {
		int[] row = new int[k];
		
		for (int i = 0 ; i < k; i++) {
			row[i] = this.rand.nextInt(v);
		}
		
		return row;
	}
	
	@Override
	protected int[] constructMixedRow(CovTabIface covTab) {
		int k = covTab.getk();
		int[] V = covTab.getV();
		
		long coverage = 0;
		int[] bestRow = null;
		for (int i = 0; i < this.numSamples; i++) {
			int[] row = this.constructMixedRandRow(k, V);
			long c = covTab.countCovered(row);
			if (c >= coverage) {
				coverage = c;
				bestRow = row;
			}
		}
		System.out.println("Best coverage: " + coverage);
		return bestRow;
	}

	private int[] constructMixedRandRow(int k, int[] V) {
		int[] row = new int[k];
		
		for (int i = 0 ; i < k; i++) {
			row[i] = this.rand.nextInt(V[i]);
		}
		
		return row;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		/*RandRowAlgo algo = new RandRowAlgo();
		DAIface da = algo.constructDetectingArray(1, 2, 10, 2);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isDetectingArray());*/
		
		/*RandRowAlgo algo = new RandRowAlgo();
		int[] v = new int[]{2,2,3,3};
		DAIface da = algo.constructMixedDetectingArray(1, 2, 4, v);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isMixedDetectingArray());*/
		
		/*int d = 1, t = 0, k1 = 0, k2 = 0, v = 0;
		
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need four arguments- t, v, kStart, and kEnd.");
			System.exit(1);
		}
		
		DAAlgoIface algo = new RandRowAlgo();
		String dirName = "data\\out\\da";
		
		String fileName = "RandRowDA";
		Runner.run(algo, d, t, k1, k2, v, dirName, fileName);*/
		
		RandRowAlgo algo = new RandRowAlgo();
		int d = 1; int t = 2; int k = 27;
		//int[] v = new int[]{2,2,3,3};
		int[] v = Misc.getAldacoLevels();
		String dirName = "data\\out\\da\\explicit";
		Runner.printAlgo(algo, d, t, k, v, dirName, "randRow-v1");
	}

}
