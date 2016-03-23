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

/**
 * This implementation is not memory efficient. During initialization it generates
 * all possible column groups and stores them in the memory as a list.
 * @author ksarkar1
 *
 */

public class ColGrIterator1 implements ColGrIterator {

	private List<ColGroup> colGrs;
	private int colGrInd;
	private int colGrSz;
	
	/**
	 * Time efficient colGrIterator. Generates all the column groups and stores them
	 * in the memory and then iterates them. Requires a lot of memory for large k.
	 * But takes very little time.
	 * @param t
	 * @param k
	 */
	public ColGrIterator1(int t, int k) {
		//TODO use true iterator
		this.colGrs = Helper.createAllColGroups(t, k);
		this.colGrInd = 0;
		this.colGrSz = this.colGrs.size();
	}

	public boolean hasNext() {
		return (this.colGrInd < this.colGrSz) ? true : false;
	}

	public ColGroup next() {
		ColGroup cols = this.colGrs.get(this.colGrInd);
		this.colGrInd++;
		
		return cols;
	}

	public void rewind() {
		this.colGrInd = 0;		
	}


}
