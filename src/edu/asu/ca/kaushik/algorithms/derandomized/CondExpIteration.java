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
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;


public class CondExpIteration extends CondExpEntries {
	
	public CondExpIteration(){
		super();
		super.name = new String("Cond Exp Iteration");
	}
	
	@Override
	public Integer[] selectNextRow(InteractionSet ig, int k, int v){
		assert(ig instanceof InteractionGraph);
		InteractionSet igCopy = new InteractionGraph((InteractionGraph)ig);
		Integer[] newRow = super.makeStarredRow(k, v);
		
		while (!allInteractionsSeen(newRow, v, igCopy)){
			Interaction interaction = igCopy.selectInteraction(newRow);
			
			// time complexity measurement
			super.comp = super.comp + igCopy.getComp();
			
			newRow = super.updateRow(newRow, interaction);
			
			igCopy.deleteFullyDeterminedInteractions(newRow);
			igCopy.deletIncompatibleInteractions(interaction);	
		}
		
		fillAllCols(newRow, v);
		return newRow;		
	}

	private static void fillAllCols(Integer[] newRow, int v) {
		for (int i = 0; i < newRow.length; i++) {
			if (newRow[i].intValue() == v){
				newRow[i] = new Integer(0);
			}
		}
	}

	private static boolean allInteractionsSeen(Integer[] newRow, int v,
			InteractionSet igCopy) {
		return igCopy.isEmpty() || allColsFixedP(newRow, v);
	}

	private static boolean allColsFixedP(Integer[] newRow, int v) {
		for (Integer i : newRow) {
			if (i.intValue() == v){
				return false;
			}
		}
		return true;
	}

}
