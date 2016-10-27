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

import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class dSymTuple {
	private int d;
	private SymTuple[] dTups;

	public dSymTuple(SymTuple[] ds) {
		this.d = ds.length;
		this.dTups = new SymTuple[this.d];
		for (int i = 0; i < d; i++) {		// could use Arrays.copy instead
			dTups[i] = ds[i];
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + d;
		result = prime * result + Arrays.hashCode(dTups);
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
		if (!(obj instanceof dSymTuple)) {
			return false;
		}
		dSymTuple other = (dSymTuple) obj;
		if (d != other.d) {
			return false;
		}
		if (!Arrays.equals(dTups, other.dTups)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		//return "dSymTuple [dTups=" + Arrays.toString(dTups) + "]";
		return "dSymTuple" + Arrays.toString(dTups);
	}

}
