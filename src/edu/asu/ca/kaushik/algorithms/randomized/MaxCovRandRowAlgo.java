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

package edu.asu.ca.kaushik.algorithms.randomized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.twostage.TwoStageColoring;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class MaxCovRandRowAlgo extends RandRowCAGenAlgo {
	private int numSamples = 1000;
	private Random rand = new Random(12345L);

	@Override
	protected boolean selectRandRow(InteractionGraph ig, Integer[] nextRow) {
		int k = ig.getK();
		int v = ig.getV();
		
		long coverage = 0;
		Integer[] bestRow = null;
		for (int i = 0; i < this.numSamples; i++) {
			Integer[] row = Helper.constructRandRow(k,v, this.rand);
			long c = ig.getCoverage(row, null);
			if (c >= coverage) {
				coverage = c;
				bestRow = row;
			}
		}
		for (int i = 0; i < k; i++) {
			nextRow[i] = bestRow[i];
		}
		return true;
	}
	
	@Override 
	public String getName() {
		return "MaxCovRandRowAlgo";
	}
	
	public static void main(String[] args) throws IOException {
		MaxCovRandRowAlgo algo = new MaxCovRandRowAlgo();
		CA ca = algo.generateCA(4, 100, 2);
		System.out.println(ca);
		System.out.println(ca.getNumRows());
		ArrayCA testCA = new ArrayCA(ca);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println(testCA.isCompleteCA(cols));
		
		/*int t = 0, k1 = 0, k2 = 0, v = 0;
		
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need four arguments- t, v, kStart, and kEnd.");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new MaxCovRandRowAlgo());
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\da"
				, "MaxCovRandRowAlgo");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();*/
	}

	

}
