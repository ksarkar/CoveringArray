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


public final class ColGroup {
	
	private final int len;
	private final int[] cols;

	public ColGroup(int[] cols) {
		this.len = cols.length;
		
		this.cols = new int[this.len];
		System.arraycopy(cols, 0, this.cols, 0, this.len);
	}
	
	public ColGroup(ColGroup old, int i){
		this.len = old.getLen() + 1;
		
		this.cols = Arrays.copyOf(old.getCols(), this.len);
		this.cols[this.len - 1] = i;
	}
	
	

	public int getLen() {
		return len;
	}

	public int[] getCols() {
		return Arrays.copyOf(this.cols, this.len);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(cols);
		result = prime * result + len;
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
		if (!(obj instanceof ColGroup)) {
			return false;
		}
		ColGroup other = (ColGroup) obj;
		if (!Arrays.equals(cols, other.cols)) {
			return false;
		}
		if (len != other.len) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ColGroup [cols=" + Arrays.toString(cols) + "]";
	}

	public boolean intersects(ColGroup otherCols) {
		int[] oC = otherCols.getCols();
		for (int i : this.cols) {
			for (int j : oC){
				if (i == j) {
					return true;
				}
			}
		}
		return false;
	}
/*
	public int[] getIntersectCols(ColGroup otherCols) {
		int[] mask = new int[this.len];
		int[] oC = otherCols.getCols();
		for (int i = 0; i < this.len; i++){
			for (int j = 0; j < this.len; j++){
				// TODO 
			}
		}
		return null;
	}
*/
	public int[] getIntersectCols(Interaction interaction, int v) {
		int[] fixCols = interaction.getCols().getCols();
		int[] fixSyms = interaction.getSyms().getSyms();
		
		int[] syms = new int[this.len];
		for (int i = 0; i < this.len; i++){
			syms[i] = v;
		}
		
		for (int i = 0; i < this.len; i++){
			for (int j = 0; j < this.len; j++){
				if (this.cols[i] == fixCols[j]){
					syms[i] = fixSyms[j];
					break;
				}
			}
		}
		
		return syms;
	}

	public boolean isFullyDetermined(Integer[] newRow, int v) {
		for (int i : this.cols){
			if (newRow[i].intValue() == v){
				return false;
			}
		}
		return true;
	}

	public boolean contains(int index) {
		for (Integer i : this.cols){
			if (i.intValue() == index){
				return true;
			}
		}
		return false;
	}

	public ColGroup addCol(int index) {
		int[] newCols = new int[this.len + 1];
		boolean greater = false;
		for (int i = 0; i < this.len; i++) {
			if (!greater) {
				if (this.cols[i] < index) {
					newCols[i] = this.cols[i];
				} else {
					greater = true;
					newCols[i] = index;
					newCols[i + 1] = this.cols[i] + 1;
				}
			} else {
				newCols[i + 1] = this.cols[i] + 1;
			}
		}
		if (!greater) {
			newCols[this.len] = index;
		}
		return new ColGroup(newCols);
	}
	
	

}
