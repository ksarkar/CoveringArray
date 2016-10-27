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

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;

public class dColGrLexIterator implements dColGrIterator {
	private int d, t, k;
	private ColGroup[] dc;
	private ColGrIterator colIt;

	public dColGrLexIterator(int d, int t, int k) {
		// TODO modify for general d;
		if (d != 1) {
			System.err.println("Sorry! does not work for any case other than d=1.");
			System.exit(1);
		}
		
		this.d = d;
		this.t = t; 
		this.k = k;
		this.dc = new ColGroup[this.d];
		this.colIt = new ColGrLexIterator(this.t, this.k);	
	}

	@Override
	public void rewind() {
		this.colIt.rewind();
	}

	@Override
	public boolean hasNext() {
		//TODO modify for general d
		return this.colIt.hasNext();
	}

	@Override
	public dColGroup next() {
		// TODO modify for general d
		this.dc[0] = this.colIt.next();
		return new dColGroup(this.dc);
	}


}
