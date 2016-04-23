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

package edu.asu.ca.kaushik.algorithms.derandomized;

import java.util.Iterator;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;

public class DensityForSmall extends CondExpEntries {
	
	/**
	 * Implementation of the density algorithm optimized for the situation 
	 * when number of interactions to cover is small, i.e. in the order of v^t
	 * instead of (k choose t) * v^t 
	 */
	public DensityForSmall() {
		super();
		super.name = "DensityForSmall";
	}
	
	@Override
	protected double computeExpCoverageSymb(Integer[] row, int index, int v, InteractionSet ig) {
		double expCov = 0.0d;
		Iterator<Interaction> it = ig.getInteractions().iterator();
		while(it.hasNext()) {
			Interaction i = it.next();
			ColGroup cols = i.getCols();
			if (cols.contains(index)) {
				expCov = expCov +  this.computeExpCov(row, index, v, i);
			}
		}
		return expCov;
	}

	private double computeExpCov(Integer[] row, int index, int v, Interaction i) {
		double condExp = 0.0d;
		int numFreeCol = i.isCompatible(row, v);
		if (numFreeCol >= 0) {
			condExp = 1.0d / Math.pow(v, numFreeCol);
		}
		return condExp;
	}

}
