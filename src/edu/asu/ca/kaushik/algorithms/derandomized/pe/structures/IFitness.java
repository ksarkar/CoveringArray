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
