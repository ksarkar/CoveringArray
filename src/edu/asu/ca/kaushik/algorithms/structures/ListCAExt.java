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

public class ListCAExt extends ListCA {
	
	// Remaining conflict graph information
	private int numUncovInt;
	private int numEdges;
	private int maxDeg;
	private int minDeg;
	private int numColors;

	public ListCAExt(int t, int k, int v) {
		super(t, k, v);
	}

	public int getNumUncovInt() {
		return numUncovInt;
	}

	public void setNumUncovInt(int numUncovInt) {
		this.numUncovInt = numUncovInt;
	}

	public int getNumEdges() {
		return numEdges;
	}

	public void setNumEdges(int numEdges) {
		this.numEdges = numEdges;
	}

	public int getMaxDeg() {
		return maxDeg;
	}

	public void setMaxDeg(int maxDeg) {
		this.maxDeg = maxDeg;
	}

	public int getMinDeg() {
		return minDeg;
	}

	public void setMinDeg(int minDeg) {
		this.minDeg = minDeg;
	}

	public int getNumColors() {
		return numColors;
	}

	public void setNumColors(int numColors) {
		this.numColors = numColors;
	}
	
	public void copyInfo(ListCAExt other) {
		this.numUncovInt = other.numUncovInt;
		this.numEdges = other.numEdges;
		this.maxDeg = other.maxDeg;
		this.minDeg = other.minDeg;
		this.numColors = other.numColors;
	}

}
