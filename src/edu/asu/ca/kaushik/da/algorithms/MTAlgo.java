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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.da.structures.DAIface;
import edu.asu.ca.kaushik.da.structures.Misc;
import edu.asu.ca.kaushik.da.structures.RowDA;
import edu.asu.ca.kaushik.da.structures.dColGrIterator;
import edu.asu.ca.kaushik.da.structures.dColGrLexIterator;
import edu.asu.ca.kaushik.da.structures.dColGroup;
import edu.asu.ca.kaushik.da.structures.dSymTuple;

public class MTAlgo implements DAAlgoIface {
	
	private static final int MAX_ITER = 1000;
	private int N;
	
	public MTAlgo(int N) {
		this.N = N;
	}

	@Override
	public DAIface constructDetectingArray(int d, int t, int k, int v) {
		assert(k >= 2 * t);
		DAIface da = new RowDA(d,t,k,v);
		da.addRandomRows(N);
		
		int iterNo = 0;
		boolean isDA;
		List<SymTuple> syms = new ArrayList<SymTuple>(Helper.createAllSymTuples(t, v));
		Set<dSymTuple> dSyms = Misc.getDSymTupSet(syms);
		ColGrIterator colIt = new ColGrLexIterator(t,k);
		dColGrIterator dColIt = new dColGrLexIterator(d, t, k);
		
		do {
			iterNo++;
			System.out.println("Iteration : " + iterNo);
			
			isDA = true;
			colIt.rewind();
			dColIt.rewind();
			while(colIt.hasNext()) {
				ColGroup cols = colIt.next();
				dColIt.rewind();
				while (dColIt.hasNext()) {
					dColGroup dCols = dColIt.next();
					if (!da.isDetecting(cols, dCols, syms, dSyms)) {
						this.reSampleColumns(da, cols, dCols);
						isDA = false;						
						System.out.println("Problem : " + cols.toString() + ", " + dCols.toString());
						break;
					}
				}
				if (!isDA) {
					break;
				}
			}
			
		} while(!isDA && iterNo < MAX_ITER);
		
		if (!isDA) {
			System.out.println("Attention!!! Not a detecting array!");
			System.exit(1);
		}
		
		return da;
	}

	private void reSampleColumns(DAIface da, ColGroup cols, dColGroup dCols) {
		Set<Integer> s = new HashSet<Integer>();
		this.addCols(s, cols);
		ColGroup[] dc = dCols.getColGroups();
		for (ColGroup c : dc) {
			this.addCols(s, c);
		}
		da.reSampleCols(s);
	}

	private void addCols(Set<Integer> set, ColGroup cols) {
		int[] c = cols.getCols();
		for (int i : c) {
			set.add(i);
		}
	}
	
	@Override
	public DAIface constructMixedDetectingArray(int d, int t, int k, int[] V) {
		assert(k >= 2 * t);
		if (k != V.length) {
			System.out.println("Number of levels for all the factors not given.");
			System.exit(1);
		}
		
		DAIface da = new RowDA(d,t,k,V);
		da.addRandomRows(N);
		
		int iterNo = 0;
		boolean isDA;
		//List<SymTuple> syms = new ArrayList<SymTuple>(Helper.createAllSymTuples(t, v));
		//Set<dSymTuple> dSyms = Misc.getDSymTupSet(syms);
		ColGrIterator colIt = new ColGrLexIterator(t,k);
		dColGrIterator dColIt = new dColGrLexIterator(d, t, k);
		
		do {
			iterNo++;
			System.out.println("Iteration : " + iterNo);
			
			isDA = true;
			colIt.rewind();
			while(colIt.hasNext()) {
				ColGroup cols = colIt.next();
				dColIt.rewind();
				while (dColIt.hasNext()) {
					dColGroup dCols = dColIt.next();
					if (!da.isMixedDetecting(cols, dCols)) {
						this.reSampleColumns(da, cols, dCols);
						isDA = false;						
						System.out.println("No evidence for : " + cols.toString() + ", " + dCols.toString());
						break;
					}
				}
				if (!isDA) {
					break;
				}
			}
			
		} while(!isDA && iterNo < MAX_ITER);
		
		if (!isDA) {
			System.out.println("Attention!!! Not a detecting array!");
			System.exit(1);
		}
		
		return da;
	}
	
	public static void main(String[] args) {
		DAAlgoIface algo = new MTAlgo(28);
		DAIface da = algo.constructDetectingArray(1, 2, 3, 3);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isDetectingArray());
		
		/*DAAlgoIface algo = new MTAlgo(15);
		int[] v = new int[]{2,2,2,2};
		DAIface da = algo.constructMixedDetectingArray(1, 2, 4, v);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isMixedDetectingArray());*/
		
		/*int[] v = Misc.getAldacoLevels();
		DAIface da = algo.constructMixedDetectingArray(1, 2, 75, v);
		System.out.println(da.getNumRows());*/
		//System.out.println(da);
		
		

	}

}
