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

package edu.asu.ca.kaushik.algorithms.derandomized.pe.structures;

import java.util.Arrays;

import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class IFitness {
	SymTuple symTup;
	long[] overlapCountA;		

	public IFitness(SymTuple symTuple, int t, int k, int v) {
		this.symTup = symTuple;
		this.overlapCountA = Helper.getNewOverlapCountA(t, k, v);
	}

	@Override
	public int hashCode() {
		return symTup.hashCode();
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
			other = ((IFitness) obj).symTup;
		} else {
			other = (SymTuple) obj;
		}
		if (this.symTup == null) {
			if (other != null) {
				return false;
			}
		} else if (!this.symTup.equals(other)) {
			return false;
		}
		return true;
	}

	public SymTuple getSymTup() {
		return symTup;
	}

	public void setSymTup(SymTuple symTup) {
		this.symTup = symTup;
	}

	public long[] getOverlapCountA() {
		return overlapCountA;
	}

	public void setOverlapCountA(long[] overlapCountA) {
		this.overlapCountA = overlapCountA;
	}
	
	@Override
	public String toString() {
		return "IFitness [" + this.symTup + ", overlap=" + Arrays.toString(this.overlapCountA) + "]";
	}
}
