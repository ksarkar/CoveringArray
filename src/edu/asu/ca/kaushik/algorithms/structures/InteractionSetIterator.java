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

import java.util.List;

public class InteractionSetIterator {
	List<ColGroup> colGrs;
	List<SymTuple> symTups;
	int colGrInd;
	int symTupInd;
	int colGrSz;
	int symTupSz;

	public InteractionSetIterator(int t, int k, int v) {
		this.colGrs = Helper.createAllColGroups(t, k);
		this.symTups = Helper.createAllSymTuples(t, v);
		this.colGrInd = 0;
		this.symTupInd = 0;
		this.colGrSz = this.colGrs.size();
		this.symTupSz = this.symTups.size();
	}

	public boolean hasNext() {
		return (this.colGrInd < this.colGrSz) ? true : false;
	}

	public Interaction next() {
		Interaction interacton = new Interaction(this.colGrs.get(this.colGrInd), 
													this.symTups.get(symTupInd));
		if (this.symTupInd < this.symTupSz - 1) {
			this.symTupInd++;
		} else {
			this.colGrInd++;
			this.symTupInd = 0;
		}
		
		return interacton;
	}

	public void rewind() {
		this.colGrInd = this.symTupInd = 0;		
	}

}
