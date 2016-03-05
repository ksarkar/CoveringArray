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
import java.util.Arrays;

import edu.asu.ca.kaushik.algorithms.derandomized.pe.structures.IFitness;


public final class SymTuple {
	
	private final int len;
	private final int[] syms;

	public SymTuple(int[] syms) {
		this.len = syms.length;
		
		this.syms = new int[this.len];
		System.arraycopy(syms, 0, this.syms, 0, this.len);
	}

	public SymTuple(SymTuple tuple, int i) {
		this.len = tuple.getLen() + 1;
		
		this.syms = Arrays.copyOf(tuple.getSyms(), this.len);
		this.syms[this.len - 1] = i;
		
	}
	
	public int getLen() {
		return this.len;
	}
	public int[] getSyms() {
		return Arrays.copyOfRange(this.syms, 0, this.len);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + len;
		result = prime * result + Arrays.hashCode(syms);
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
		if (!(obj instanceof IFitness) && !(obj instanceof SymTuple)) {
			return false;
		}
		SymTuple other = null;
		if (obj instanceof IFitness) {
			other = ((IFitness) obj).getSymTup();
		} else {
			other = (SymTuple) obj;
		}
		if (len != other.len) {
			return false;
		}
		if (!Arrays.equals(syms, other.syms)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SymTuple [syms=" + Arrays.toString(syms) + "]";
	}

}
