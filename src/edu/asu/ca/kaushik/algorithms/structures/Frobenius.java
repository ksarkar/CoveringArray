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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Frobenius implements Group {
	private int v;
	private FiniteField f;

	/**
	 * Frobenius group action.
	 * @param v size of the field. Must be a prime for the time being.
	 */
	public Frobenius(int v) {
		assert( v > 0);
		this.v = v;
		this.f = new FiniteField(v);
	}
	
	@Override
	public List<SymTuple> getAllOrbits(int t) {
		List<SymTuple> orbits = new ArrayList<SymTuple>();
		
		for (int i = 1; i < t; i++) {
			int[] syms = new int[t];
			syms[i] = 1;
			List<SymTuple> s = Helper.createAllSymTuples(t-i-1,this.v);
			for (SymTuple tup : s) {
				int[] sTup = tup.getSyms();
				for (int j = i+1; j < t; j++) {
					syms[j] = sTup[j-i-1];
				}
				orbits.add(new SymTuple(syms));
			}
			
		}
		return orbits;
	}

	@Override
	public ListCA develop(ListCA sCA) {
		ListCA ca = new ListCA(sCA.getT(),sCA.getK(),sCA.getV());
		Iterator<Integer[]> it = sCA.iterator();
		while(it.hasNext()) {
			ca.addRows(this.developRow(it.next()));
		}
		
		//add constant rows
		int k = sCA.getK();
		for (int i = 0; i < this.v; i++) {
			Integer[] row = new Integer[k];
			for (int j = 0; j < k; j++) {
				row[j] = i;
			}
			ca.addRow(row);
		}
		
		return ca;
	}

	private List<Integer[]> developRow(Integer[] r) {
		List<Integer[]> rows = new ArrayList<Integer[]>();
		int k = r.length;
		for (int a = 1; a < this.v; a++) {
			for (int b = 0; b < this.v; b++) {
				Integer[] row = new Integer[k];
				for (int j = 0; j < k; j++) {
					row[j] = this.act(a,b,r[j]);
				}
				rows.add(row);
			}
		}
		return rows;
	}
	
	@Override
	public List<SymTuple> getOrbits(ColGroup colGr, Integer[] row) {
		List<SymTuple> symTups = this.getSymTups(colGr, row);
		Set<SymTuple> orbits = new HashSet<SymTuple>();
		
		for (SymTuple tup : symTups) {
			SymTuple t = this.getOrbit(tup);
			orbits.add(t);
		}
		return new ArrayList<SymTuple>(orbits);
	}

	/*@Override
	public List<SymTuple> getOrbits(ColGroup colGr, Integer[] row) {
		List<SymTuple> symTups = this.getSymTups(colGr, row);
		List<SymTuple> orbits = new ArrayList<SymTuple>();
		
		while(!symTups.isEmpty()) {
			SymTuple s = symTups.get(0);
			orbits.add(s);
			Iterator<SymTuple> it = symTups.iterator();
			while(it.hasNext()) {
				SymTuple n = it.next();
				if (this.isInSameOrbit(s, n)) {
					it.remove();
				}
			}
		}
		return orbits;
	
	}*/

	private List<SymTuple> getSymTups(ColGroup colGr, Integer[] row) {
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
		List<SymTuple> symTups = new ArrayList<SymTuple>();
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			for (int i = 0; i < entries.length; i++){
				syms[starredCols.get(i).intValue()] = entries[i];
			}
			symTups.add(new SymTuple(syms));
		}
		return symTups;
	}
	
	@Override
	public boolean isInSameOrbit(SymTuple tup1, SymTuple tup2) {
		SymTuple t1 = this.getOrbit(tup1);
		SymTuple t2 = this.getOrbit(tup2);
		return t1.equals(t2);
	}

	/*@Override
	public boolean isInSameOrbit(SymTuple tup, SymTuple tuple) {
		assert(tup.getLen() == tuple.getLen());
		int t = tup.getLen();
		int[] tup1 = tup.getSyms();
		int[] tup2 = tuple.getSyms();
		int[] diff1 = new int[t-1], diff2 = new int[t-1];
		for (int i = 1; i < t; i++) {
			diff1[i-1] = tup1[i] - tup1[0] >= 0 ? tup1[i] - tup1[0] : this.v + tup1[i] - tup1[0];
			diff2[i-1] = tup2[i] - tup2[0] >= 0 ? tup2[i] - tup2[0] : this.v + tup2[i] - tup2[0];
		}
		
		int div1 = 0, div2 = 0;
		for (int i = 0; i < t-1; i++) {
			if (div1 == 0) {
				if ((diff1[i] != 0) && (diff2[i] != 0)) {
					div1 = diff1[i];
					div2 = diff2[i];
				} else if ((diff1[i] == 0) && (diff2[i] == 0)) {
					continue;
				} else {
					return false;
				}
			}
			
			if (this.f.divide(diff1[i], div1) != this.f.divide(diff2[i], div2)) {
				return false;
			}
			
		}
		return true;
	}*/
	
	public SymTuple act(int a, int b, SymTuple s) {
		assert((a>0) && (a<this.v) && (b>=0) && (b<this.v));
		
		int[] s1 = s.getSyms();
		int t = s1.length;
		int[] s2 = new int[t];
		for (int i = 0; i < t; i++) {
			s2[i] = this.act(a, b, s1[i]);
		}
		return new SymTuple(s2);
	}
	
	private int act(int a, int b, int x) {
		return this.f.add(this.f.multiply(a, x), b);
	}
	
	@Override
	public SymTuple getOrbit(SymTuple symTup) {
		int[] s1 = symTup.getSyms();
		int t = s1.length;
		int b = s1[0];
		for (int i = 0; i < t; i++) {
			s1[i] = this.f.subtract(s1[i], b);
		}
		int a = 0;
		for (int i = 1; i < t; i++) {
			if (s1[i] != 0) {
				a = s1[i];
				break;
			}
		}
		if (a != 0) {
			for (int i = 0; i < t; i++) {
				s1[i] = this.f.divide(s1[i], a);
			}
		}
		return new SymTuple(s1);
	}

	public static void main(String[] args) {
		SymTuple tup1 = new SymTuple(new int[]{0,1,2,1});
		SymTuple tup2 = new SymTuple(new int[]{0,2,1,0});
		
		SymTuple tup3 = new SymTuple(new int[]{0,0,1});
		SymTuple tup4 = new SymTuple(new int[]{0,0,0});
		
		Group g = new Frobenius(3);
		Frobenius g1 = new Frobenius(3);
		System.out.println(g1.act(2,0,tup1));
		
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
		printAllOrbits(orbits, g1, 3);
		
		System.out.println();
		System.out.println(g.getOrbit(tup2));
	}

	private static void printAllOrbits(List<SymTuple> orbits, Frobenius g1, int v) {
		Set<SymTuple> syms = new HashSet<SymTuple>();
		for (SymTuple s : orbits) {
			for (int a = 1; a < v; a++) {
				for (int b = 0; b < v; b++) {
					SymTuple sym = g1.act(a, b, s);
					if (syms.contains(sym)) {
						System.out.println("Duplicate interaction");
					} else {
						syms.add(sym);
					}
				}
			}
		}
		
		System.out.println();
		for (SymTuple s : syms) {
			System.out.println(s);
		}
		
	}

}
