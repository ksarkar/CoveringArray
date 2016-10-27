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

package edu.asu.ca.kaushik.da.structures;

import java.util.Arrays;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;

public class dColGroup {
	private int d;
	private ColGroup[] dCols;
	
	public dColGroup(ColGroup[] dc) {
		this.d = dc.length;
		this.dCols = new ColGroup[this.d];
		for (int i = 0; i < this.d; i++) {
			this.dCols[i] = dc[i];
		}
	}

	public ColGroup[] getColGroups() {
		return Arrays.copyOfRange(this.dCols, 0, this.d);
	}

	public boolean contains(ColGroup cols) {
		for (int i = 0; i < d; i++) {
			if (cols.equals(dCols[i])) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(int index) {
		for (int i = 0; i < d; i++) {
			if (dCols[i].contains(index)) {
				return true;
			}
		}
		return false;
	}
	
	public int[] union(ColGroup cols) {
		int[] c = cols.getCols();
		for (int i = 0; i < d; i++) {
			c = dCols[i].union(c);
		}
		return c;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + d;
		result = prime * result + Arrays.hashCode(dCols);
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
		if (!(obj instanceof dColGroup)) {
			return false;
		}
		dColGroup other = (dColGroup) obj;
		if (d != other.d) {
			return false;
		}
		if (!Arrays.equals(dCols, other.dCols)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		//return "dColGroup [dCols=" + Arrays.toString(dCols) + "]";
		return "dColGroup" + Arrays.toString(dCols);
	}

}
