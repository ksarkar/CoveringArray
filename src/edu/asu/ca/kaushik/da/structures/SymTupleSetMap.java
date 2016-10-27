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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class SymTupleSetMap {
	/**
	 * The key is a tuple consisting of alphabet sizes for the given t columns.
	 * The value is the set of all possible SymTuples for these t columns.
	 */
	private Map<SymTuple, List<SymTuple>> map;
	
	public SymTupleSetMap() {
		this.map = new HashMap<SymTuple, List<SymTuple>>();
	}

	public List<SymTuple> getSymTups(SymTuple levels) {
		List<SymTuple> syms = this.map.get(levels);
		if (syms == null) {
			syms = Misc.getSymTupList(levels.getSyms());
			this.map.put(levels, syms);
		}
		return syms;
	}
}
