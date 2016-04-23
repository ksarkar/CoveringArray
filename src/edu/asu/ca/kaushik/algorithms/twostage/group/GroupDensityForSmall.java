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

package edu.asu.ca.kaushik.algorithms.twostage.group;

import java.util.Set;
import edu.asu.ca.kaushik.algorithms.derandomized.GroupDensity;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.OrbRepSet;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class GroupDensityForSmall extends GroupDensity {
	private int gId;

	public GroupDensityForSmall(int gId) {
		super(gId);
		this.gId = gId;
	}

	@Override
	protected double computeExpectedCoverage(ColGroup colGr, Integer[] row, OrbRepSet oSet) {
		Set<SymTuple> set = oSet.getOrbits(colGr);
		double numPosOrbits = this.numOfPossibleOrbits(colGr, row, oSet);
		
		int covered = 0;
		for (SymTuple orb : set) {
			if (this.isCompatible(colGr, orb, row, oSet.getGroup(), oSet.getV())) {
				covered++;
			}
		}
		
		return covered / numPosOrbits;
	}

	private double numOfPossibleOrbits(ColGroup colGr, Integer[] row,
			OrbRepSet oSet) {
		
		double num = 0.0d;
		Group g = oSet.getGroup();
		int v = oSet.getV();
		int nFCol = colGr.numFreeCols(row, v);
		
		assert((nFCol < colGr.getLen()) && (nFCol >= 0));
		
		switch(this.gId) {
		case 0:
		case 1:
				num = Math.pow(v, nFCol);
				break;
		case 2: 
				num = g.getOrbits(colGr, row).size();
				break;
		default: 
				System.err.println("numOfPossibleOrbits: Unknown group type.");
				System.exit(1);
		}
		
		return num;
	}
	
	private boolean isCompatible(ColGroup colGr, SymTuple orb, Integer[] row, Group g, int v) {
		Interaction orbit = new Interaction(colGr, orb);
		return orbit.isCompatible(row, g, v);
	}
}
