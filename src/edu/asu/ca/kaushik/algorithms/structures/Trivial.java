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

package edu.asu.ca.kaushik.algorithms.structures;

import java.util.ArrayList;
import java.util.List;

public class Trivial implements Group {
	private int v;
	
	public Trivial(int v) {
		this.v = v;
	}

	@Override
	public ListCA develop(ListCA sCA) {
		return sCA;
	}

	@Override
	public List<SymTuple> getOrbits(ColGroup colGr, Integer[] row) {
		List<Integer> starredCols = new ArrayList<Integer>();
		int t = colGr.getLen();
		int[] cols = colGr.getCols();
		int[] syms = new int[t];
		
		for (int i = 0; i < t; i++){
			int s = row[cols[i]].intValue();
			if ( s == this.v){
				starredCols.add(new Integer(i));
			}
			else {
				syms[i] = s;
			}
		}
		
		List<SymTuple> allBlankEntries = Helper.createAllSymTuples(starredCols.size(), this.v);
		List<SymTuple> orbits = new ArrayList<SymTuple>();
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			for (int i = 0; i < entries.length; i++){
				syms[starredCols.get(i).intValue()] = entries[i];
			}
			orbits.add(new SymTuple(syms));
		}
		return orbits;
	}

	@Override
	public List<SymTuple> getAllOrbits(int t) {
		return Helper.createAllSymTuples(t,this.v);
	}

	@Override
	public boolean isInSameOrbit(SymTuple tup, SymTuple tuple) {
		return tup.equals(tuple);
	}

	@Override
	public SymTuple getOrbit(SymTuple symTup) {
		return symTup;
	}

}
