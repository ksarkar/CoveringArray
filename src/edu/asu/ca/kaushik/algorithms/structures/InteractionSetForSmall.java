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

import java.util.Iterator;
import java.util.List;

public class InteractionSetForSmall implements InteractionSet {
	private List<Interaction> uncovInts;
	private int v;
	
	/**
	 * Stores the interactions to be covered in a list
	 * @param intList
	 */
	public InteractionSetForSmall(List<Interaction> intList, int v) {
		this.uncovInts = intList;
		this.v = v;
	}

	@Override
	public boolean isEmpty() {
		return this.uncovInts.isEmpty();
	}
	
	@Override
	public List<Interaction> getInteractions() {
		return this.uncovInts;
	}

	@Override
	public int deleteInteractions(Integer[] newRandRow, int[] covD) {
		int coverage = 0;
		Iterator<Interaction> it = this.uncovInts.iterator();
		while(it.hasNext()) {
			Interaction i = it.next();
			if (i.isCompatible(newRandRow, this.v) >= 0) {
				coverage++;
				it.remove();
			}
				
		}
		return coverage;
	}

	@Override
	public boolean contains(Interaction interaction) {
		assert false;
		return false;
	}

	@Override
	public Interaction selectInteraction(Integer[] newRow) {
		assert false;
		return null;
	}

	@Override
	public int getComp() {
		assert false;
		return 0;
	}

	@Override
	public void deleteFullyDeterminedInteractions(Integer[] newRow) {
		assert false;

	}

	@Override
	public void deletIncompatibleInteractions(Interaction interaction) {
		assert false;

	}

	@Override
	public int getCoverage(Integer[] newRow, int[] covD) {
		assert false;
		return 0;
	}

	@Override
	public int getNumUncovInt(ColGroup colGr) {
		assert false;
		return 0;
	}

	@Override
	public double computeProbCoverage(int[] sCols, int[] entries) {
		assert false;
		return 0;
	}

	@Override
	public int getT() {
		assert false;
		return 0;
	}

	@Override
	public Iterator<ColGroup> getColGrIterator() {
		assert false;
		return null;
	}
}
