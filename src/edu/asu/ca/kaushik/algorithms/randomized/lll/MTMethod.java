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
import java.util.Date;

import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.outputformatter.CAWriter;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class MTMethod extends LLL {
	private int N;
	private static final int maxIteration = 1000000;
	
	public MTMethod(int N) {
		this.N = N;
	}
	
	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		LLLCA ca = new LLLCA(t, k, v, this.N);
		ColGrIterator clGrIt = new ColGrIterator(t, k);
		boolean allCovered;
		int iteration = 0;
		int colGrNum = 0;
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			clGrIt.rewind();
			allCovered = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				ca.resetCovered();
				if (!ca.isAllCovered(cols)) {
					ca.reSample(cols);
					allCovered = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					break;
				}
				colGrNum++;
			}
			iteration++;
		} while(!allCovered && iteration < maxIteration);
		
		if (!allCovered) {
			System.out.println("output is not a covering array");
			System.exit(1);
		}
		
		return ca;
	}
	
	@Override
	public String getName() {
		return new String("MTMethod");
	}
	
	public static void main(String[] args) throws IOException {
		CAWriter writer = new StandardCAWriter();
		CAGeneration c = new CAGeneration();
		
		int t = 6;
		int k = 20;
		int v = 3;
		int N = 11000;
		
		/*
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
			v = Integer.parseInt(args[2]);
			N = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need three arguments- t, k, v and N");
			System.exit(1);
		}
		*/
		System.out.println("MTMethod");
		
		System.out.println(new Date());
		c.setCAGenAlgo(new MTMethod(N));
		CA ca = c.generateCA(t, k, v);
		System.out.println(new Date());
		
		writer.writeCA(ca, "data\\out\\lll\\6-k-3\\" + t + "-" + k + "-" + v + "-MTMethod.txt");
		
		System.out.println("MTMethod Done");
	}

}
