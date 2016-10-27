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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public interface DAIface {

	public void addRow(int[] newRow);
	public int getD();
	public int getT();
	public int getK();
	public int getV();
	public int getNumRows();
	public boolean isDetectingArray();
	public boolean isDetecting(ColGroup cols, dColGroup dCols,
			List<SymTuple> syms, Set<dSymTuple> dSyms);
	public boolean isMixedDetectingArray();
	public boolean isMixedDetecting(ColGroup cols, dColGroup dCols);
	public void reSampleCols(Set<Integer> s);
	public void addRandomRows(int n);
	public void writeToFile(String fName) throws IOException;
	public int countCovered(ColGroup cols, dColGroup dCols, SymTuple s,
			dSymTuple ds);
	public Map<SymTuple, Set<dSymTuple>> getSymTupMap(ColGroup cols,
			dColGroup dCols);
	public long countCovered(int[] row);
	public boolean isMixedFlagUP();
	public int[] getVArray();
	
}
