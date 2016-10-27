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

package edu.asu.ca.kaushik.algorithms.randomized.lll;

import java.io.IOException;

import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator2;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.da.algorithms.IncrementalMTDA;
import edu.asu.ca.kaushik.da.scripts.Runner;

public class IncrementalMTCA {
	private static final int MAX_ITER = 1000;
	
	public CA generateCA(int t, int k, int v, int N) {
		assert(k >= 2 * t);
		
		LLLCA ca;
		boolean allCovered;
		
		do {
			System.out.println("N = " + N);
			ca = new LLLCA(t, k, v, N);
			ColGrIterator clGrIt = new ColGrIterator2(t, k);
			int iteration = 0;
			do {
				System.out.println("Iteration: " + iteration );				
				clGrIt.rewind();
				allCovered = true;
				while(clGrIt.hasNext()){
					ColGroup cols = clGrIt.next();
					ca.resetCovered();
					if (!ca.isAllCovered(cols)) {
						ca.reSample(cols);
						allCovered = false;
						System.out.println("uncovered interaction in coloumn group: " + cols);
						break;
					}
				}
				iteration++;
			} while(!allCovered && iteration <= MAX_ITER);
			if (!allCovered) {
				N++;
			}
		
		} while(!allCovered);
		
		return ca;
	}
	
	public static void main(String[] args) throws IOException {
		/*IncreametalMTCA algo = new IncreametalMTCA();
		CA ca = algo.generateCA(2, 30, 2, 12);
		System.out.println(ca);
		System.out.println(ca.getNumRows());
		ArrayCA testCA = new ArrayCA(ca);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println(testCA.isCompleteCA(cols));*/
		
		int t = 0, k1 = 0, k2 = 0, v = 0, N = 0;
		
		if (args.length == 5) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
			N = Integer.parseInt(args[4]);;
		} else {
			System.err.println("Need five arguments- t, v, kStart, kEnd, and N.");
			System.exit(1);
		}
		
		IncrementalMTCA algo = new IncrementalMTCA();
		String dirName = "data\\out\\da";
		String fileName = "IncrementalMTCA";
		Runner.runMTCA(algo, t, k1, k2, v, N, dirName, fileName);
	}
}
