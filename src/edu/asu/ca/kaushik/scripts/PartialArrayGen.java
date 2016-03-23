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

package edu.asu.ca.kaushik.scripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator1;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.graph.ConflictGraph;

public class PartialArrayGen {
	
	private Random rand;
	private int numInts;
	private int numEdges;

	public PartialArrayGen() {
		this.rand = new Random(123456L);
	}

	public void generatePartialArray(int t, int k, int v, int N) {
		LLLCA pca = new LLLCA(t,k,v,N, this.rand);
		
		List<Interaction> uncovInts = new ArrayList<Interaction>();
		int uncovIntNum = 0;
		ColGrIterator clGrIt = new ColGrIterator1(t, k);
		clGrIt.rewind();
		while(clGrIt.hasNext()){
			ColGroup cols = clGrIt.next();
			pca.resetCovered();
			List<Interaction> notCovered = pca.notCovered(cols);
			if (!notCovered.isEmpty()) {
				uncovIntNum = uncovIntNum + notCovered.size();
				uncovInts.addAll(notCovered);
			}
		}
		
		this.numInts = uncovIntNum;
		
		ConflictGraph conflictGraph = new ConflictGraph(uncovInts);
		this.numEdges = conflictGraph.getNumEdges();		
	}

	public int getNumUncovInt() {
		return numInts;
	}

	public int getNumEdges() {
		return numEdges;
	}

}
