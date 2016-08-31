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

package edu.asu.ca.kaushik.algorithms.randomized.lll.partialArrray;

import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator2;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;

public class PartialArray {
	public static int generatePartialArray(int t, int k, int v, int r, int n) {
		assert(k >= 2 * t);
		LLLCA ca = new LLLCA(t, k, v, n, r, new Random());
		ColGrIterator clGrIt = new ColGrLexIterator(t, k);
		boolean isCovered;
		int colGrNum;
		int uncovIntNum;
		int iteration = 1;
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			uncovIntNum = 0;
			
			clGrIt.rewind();
			isCovered = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				ca.resetCovered();
				List<Interaction> notCovered = ca.notCovered(cols);
				if (notCovered.size() >= r) {
					ca.reSample(cols);
					isCovered = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					break;
				}
				uncovIntNum = uncovIntNum + notCovered.size();
				colGrNum++;
			}
			iteration++;
		} while(!isCovered);
		
		System.out.println("required partial array constructed.");
		
		return uncovIntNum;
	}
	
	public static void main(String[] args) {
		System.out.println("Number of uncovered interactions: " + generatePartialArray(3,200,2,3,103));
	}

}
