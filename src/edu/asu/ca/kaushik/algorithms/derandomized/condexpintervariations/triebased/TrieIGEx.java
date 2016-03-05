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

package edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased;

import java.util.Arrays;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;

public class TrieIGEx extends TrieIG {
	
	
	
	public TrieIGEx() {
		super();
		// TODO Auto-generated constructor stub
	}


	public TrieIGEx(int t, int k, int v) {
		super(t, k, v);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected List<int[]> getCandidates(ColGroup cols) {
		return Helper.createAllTuples(super.getT(), super.getV());
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<int[]> c = Helper.createAllTuples(3,2);
		for (int[] a : c) {
			System.out.println(Arrays.toString(a));
		}

	}

}
