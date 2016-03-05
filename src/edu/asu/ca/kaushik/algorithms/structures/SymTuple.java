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
