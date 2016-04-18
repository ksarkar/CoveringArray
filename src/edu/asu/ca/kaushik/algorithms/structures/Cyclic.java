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
import java.util.Iterator;
import java.util.List;

public class Cyclic implements Group {
	private int v;
	
	/**
	 * Action of a sharply transitive group (cyclic group) on interactions
	 * and rows
	 * @param v size of the group
	 */
	public Cyclic(int v) {
		this.v = v;
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
		
		assert(starredCols.size() != t);
		
		List<SymTuple> allBlankEntries = Helper.createAllSymTuples(starredCols.size(), this.v);
		List<SymTuple> orbits = new ArrayList<SymTuple>();
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			for (int i = 0; i < entries.length; i++){
				syms[starredCols.get(i).intValue()] = entries[i];
			}
			orbits.add(this.getOrbit(new SymTuple(syms)));
		}
		return orbits;
	}

	@Override
	public List<SymTuple> getAllOrbits(int t) {
		List<SymTuple> s = Helper.createAllSymTuples(t-1,this.v);
		List<SymTuple> orbits = new ArrayList<SymTuple>();
		int[] syms = new int[t];
		syms[0] = 0;
		for (SymTuple tup : s) {
			int[] sTup = tup.getSyms();
			for (int i = 1; i < t; i++) {
				syms[i] = sTup[i-1];
			}
			orbits.add(new SymTuple(syms));
		}
		return orbits;
	}

	@Override
	public boolean isInSameOrbit(SymTuple tup, SymTuple tuple) {
		assert(tup.getLen() == tuple.getLen());
		int t = tup.getLen();
		int[] tup1 = tup.getSyms();
		int[] tup2 = tuple.getSyms();
		for (int i = 1; i < t; i++) {
			int diff1 = tup1[i] - tup1[0];
			diff1 = diff1 >= 0 ? diff1 : (this.v + diff1);
			int diff2 = tup2[i] - tup2[0];
			diff2 = diff2 >= 0 ? diff2 : (this.v + diff2);
			if (diff1 != diff2) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ListCA develop(ListCA sCA) {
		ListCA ca = new ListCA(sCA.getT(),sCA.getK(),sCA.getV());
		Iterator<Integer[]> it = sCA.iterator();
		while(it.hasNext()) {
			ca.addRows(this.developRow(it.next()));
		}
		
		return ca;
	}
	
	private List<Integer[]> developRow(Integer[] r) {
		List<Integer[]> rows = new ArrayList<Integer[]>();
		int k = r.length;
		for (int i = 0; i < this.v; i++) {
			Integer[] row = new Integer[k];
			for (int j = 0; j < k; j++) {
				row[j] = (r[j] + i) % this.v;
			}
			rows.add(row);
		}
		return rows;
	}
	
	@Override
	public SymTuple getOrbit(SymTuple symTup) {
		int[] s1 = symTup.getSyms();
		int t = s1.length;
		int[] s2 = new int[t];
		int map = this.v - s1[0];
		for (int i = 0; i < t; i++) {
			s2[i] = (s1[i] + map) % this.v;
		}
		return new SymTuple(s2);
	}

	public static void main(String[] args) {
		SymTuple tup1 = new SymTuple(new int[]{1,1,2,0});
		SymTuple tup2 = new SymTuple(new int[]{2,2,0,1});
		
		Group g = new Cyclic(3);
		
		System.out.println(g.isInSameOrbit(tup1,tup2));
		
		List<SymTuple> orbits = g.getAllOrbits(3);
		for (SymTuple orbit : orbits) {
			System.out.println(orbit);
		}
		
		Integer[] row = {0, 1, 3, 3, 3};
		
		orbits = g.getOrbits(new ColGroup(new int[]{1,2,3,4}), row);
		System.out.println();
		for (SymTuple orbit : orbits) {
			System.out.println(orbit);
		}
		System.out.println();
		System.out.println(g.getOrbit(tup1));
	}

}
