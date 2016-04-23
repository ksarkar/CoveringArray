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

public interface Group {

	public ListCA develop(ListCA sCA);
	public List<SymTuple> getOrbits(ColGroup colGr, Integer[] row);
	public List<SymTuple> getAllOrbits(int t);
	public boolean isInSameOrbit(SymTuple tup, SymTuple tuple);
	public SymTuple getOrbit(SymTuple symTup);
	public GroupElement convert(SymTuple symTuple, SymTuple symTuple2);
	public Interaction act(GroupElement ge, Interaction inter);
	public GroupElement identity();

}
