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
	
	

}
