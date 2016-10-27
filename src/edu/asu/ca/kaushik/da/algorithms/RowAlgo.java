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

package edu.asu.ca.kaushik.da.algorithms;

import edu.asu.ca.kaushik.da.structures.CovTab;
import edu.asu.ca.kaushik.da.structures.CovTabIface;
import edu.asu.ca.kaushik.da.structures.DAIface;
import edu.asu.ca.kaushik.da.structures.RowDA;

public abstract class RowAlgo implements DAAlgoIface {

	@Override
	public DAIface constructDetectingArray(int d, int t, int k, int v) {
		CovTabIface covTab = new CovTab(d, t, k, v);
		DAIface da = new RowDA(d, t, k, v);
		
		while (!covTab.isEmpty()) {
			int[] newRow = this.constructRow(covTab);
			
			covTab.deleteCovered(newRow);
			da.addRow(newRow);			
		}
		
		return da;
	}
	
	@Override
	public DAIface constructMixedDetectingArray(int d, int t, int k, int[] v) {
		if (k > v.length) {
			System.out.println("Number of levels for all the factors not given.");
			System.exit(1);
		}
		
		CovTabIface covTab = new CovTab(d, t, k, v);
		DAIface da = new RowDA(d, t, k, v);
		
		while (!covTab.isEmpty()) {
			int[] newRow = this.constructMixedRow(covTab);
			
			covTab.deleteCovered(newRow);
			da.addRow(newRow);			
		}
		
		return da;
	}
	
	protected abstract int[] constructMixedRow(CovTabIface covTab);
	protected abstract int[] constructRow(CovTabIface covTab);

}
