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


public class Interaction {
	ColGroup cols;
	SymTuple syms;
	public Interaction(ColGroup cols, SymTuple syms) {
		super();
		this.cols = cols;
		this.syms = syms;
	}
	public ColGroup getCols() {
		return cols;
	}
	public void setCols(ColGroup cols) {
		this.cols = cols;
	}
	public SymTuple getSyms() {
		return syms;
	}
	public void setSyms(SymTuple syms) {
		this.syms = syms;
	}
	@Override
	public String toString() {
		return "Interaction [cols=" + cols + ", syms=" + syms + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cols == null) ? 0 : cols.hashCode());
		result = prime * result + ((syms == null) ? 0 : syms.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Interaction)) {
			return false;
		}
		Interaction other = (Interaction) obj;
		if (cols == null) {
			if (other.cols != null) {
				return false;
			}
		} else if (!cols.equals(other.cols)) {
			return false;
		}
		if (syms == null) {
			if (other.syms != null) {
				return false;
			}
		} else if (!syms.equals(other.syms)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this interaction is compatible with the given row
	 * @param row
	 * @param v the wild card (star) symbol
	 * @return -1 if incompatible, otherwise number of free columns
	 */
	public int isCompatible(Integer[] row, int v) {
		int numStar = 0;
		int[] cols = this.cols.getCols();
		int[] syms = this.syms.getSyms();
		int t = cols.length;
		for (int i = 0; i < t; i++) {
			if (row[cols[i]] == v) {
				numStar++;
			} else {
				if (syms[i] != row[cols[i]]) {
					return -1;
				}
			}
		}
		return numStar;
	}
	
	public static void main(String args[]) {
		int[] c = {0,2,3};
		int[] s = {0,0,1};
		Interaction in = new Interaction(new ColGroup(c), new SymTuple(s));
		Integer[] row = {0, 0, 2, 1};
		int v = 2;
		System.out.println(in.isCompatible(row, v));
	}
	
	public boolean isCompatible(Integer[] row, Group g, int v) {
		int[] cols = this.getCols().getCols();
		int[] syms = this.getSyms().getSyms();
		int t = cols.length;
		List<Integer> fixed = new ArrayList<Integer>();
		for (int i = 0; i < t; i++) {
			if (row[cols[i]] != v) { // not a starred entry
				fixed.add(i);
			}
		}
		
		if (fixed.size() == 0) {
			return true;
		} else {
			int l = fixed.size();
			int[] s1 = new int[l];
			int[] s2 = new int[l];
			for (int i = 0; i < l; i++) {
				int ind = fixed.get(i);
				s1[i] = syms[ind];
				s2[i] = row[cols[ind]];
			}
			return g.isInSameOrbit(new SymTuple(s1), new SymTuple(s2));
		}
	}
	public boolean isCompatible(Interaction int2) {
		int[] c1 = this.getCols().getCols();
		int[] s1 = this.getSyms().getSyms();
		
		int[] c2 = int2.getCols().getCols();
		int[] s2 = int2.getSyms().getSyms();
		
		int t = c1.length;
		for (int i = 0; i < t; i++) {
			for (int j = 0; j < t; j++) {
				if (c1[i] == c2[j]) { // same columns
					if (s1[i] != s2[j]) {
						return false;
					}
				}
			}
		}
		
		return true;		
	}
	public GroupElement convert(Interaction orbit, Group g) {
		int[] c1 = this.getCols().getCols();
		int[] s1 = this.getSyms().getSyms();
		
		int[] c2 = orbit.getCols().getCols();
		int[] s2 = orbit.getSyms().getSyms();
		
		int t = c1.length;
		List<Integer> cols1 = new ArrayList<Integer>();
		List<Integer> cols2 = new ArrayList<Integer>();
		
		for (int i = 0; i < t; i++) {
			for (int j = 0; j < t; j++) {
				if (c1[i] == c2[j]) { // same column
					cols1.add(i);
					cols2.add(j);
				}
			}
		}
		
		if (cols1.size() == 0) {
			return g.identity();
		} else {
			int l = cols1.size();
			int[] sym1 = new int[l];
			int[] sym2 = new int[l];
			for (int i = 0; i < l; i++) {
				int ind1 = cols1.get(i);
				int ind2 = cols2.get(i);
				sym1[i] = s1[ind1];
				sym2[i] = s2[ind2];
			}
			
			return g.convert(new SymTuple(sym1), new SymTuple(sym2));
		}
	}

}
