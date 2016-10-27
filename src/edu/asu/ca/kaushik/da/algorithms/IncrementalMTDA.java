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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.da.scripts.Runner;
import edu.asu.ca.kaushik.da.structures.DAIface;
import edu.asu.ca.kaushik.da.structures.Misc;
import edu.asu.ca.kaushik.da.structures.RowDA;
import edu.asu.ca.kaushik.da.structures.dColGrIterator;
import edu.asu.ca.kaushik.da.structures.dColGrLexIterator;
import edu.asu.ca.kaushik.da.structures.dColGroup;
import edu.asu.ca.kaushik.da.structures.dSymTuple;

public class IncrementalMTDA {
	private static final int MAX_ITER = 1000;
	
	public DAIface constructDetectingArray(int d, int t, int k, int v, int N) {
		assert(k >= 2 * t);
		
		boolean isDA;
		DAIface da;
		do {
			System.out.println("N = " + N);
			da = new RowDA(d,t,k,v);
			da.addRandomRows(N);
			
			int iterNo = 0;
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
							//this.reSampleColumns(da, cols, dCols);
							this.reSampleColumns1(da, cols);
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
				N++;
			}
		} while(!isDA);
		
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
	
	private void reSampleColumns1(DAIface da, ColGroup cols) {
		Set<Integer> s = new HashSet<Integer>();
		this.addCols(s, cols);
		da.reSampleCols(s);
	}

	private void addCols(Set<Integer> set, ColGroup cols) {
		int[] c = cols.getCols();
		for (int i : c) {
			set.add(i);
		}
	}
	
	public static void main(String[] args) throws IOException {
		/*IncrementalMT algo = new IncrementalMT();
		DAIface da = algo.constructDetectingArray(1, 2, 3, 2, 7);
		System.out.println(da);
		System.out.println(da.getNumRows());
		System.out.println(da.isDetectingArray());	*/
		
		int d = 1, t = 0, k1 = 0, k2 = 0, v = 0, N = 0;
		
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
		
		IncrementalMTDA algo = new IncrementalMTDA();
		String dirName = "data\\out\\da";
		String fileName = "IncrementalMTDA_1";
		Runner.runMTDA(algo, d, t, k1, k2, v, N, dirName, fileName);
	}

}
