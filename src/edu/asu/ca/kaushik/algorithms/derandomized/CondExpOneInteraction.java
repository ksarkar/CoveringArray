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


public class CondExpOneInteraction extends CondExpEntries {
	
	public CondExpOneInteraction(){
		super();
		super.name = new String("Cond Exp One Interaction");
	}

	@Override
	public Integer[] selectNextRow(InteractionSet ig, int k, int v){
		Integer[] newRow = super.makeStarredRow(k, v);
		
		Interaction firstInteraction = ig.selectInteraction(newRow);
		// time complexity measurement
		super.comp = super.comp + ig.getComp();
		
		newRow = super.updateRow(newRow, firstInteraction);
		return super.fillRow(newRow, ig, v);	
	}
}
