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

import edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased.TrieIGEx;
import edu.asu.ca.kaushik.algorithms.structures.IGSimple;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;

public class CEIntM1 extends CondExpIteration{
	
	public CEIntM1(){
		super();
		super.name = new String("CEIntM1");
	}
	
	@Override
	public InteractionSet createIntersectionSet(int t, int k, int v) {
		return new IGSimple(t, k, v);
	}
	
	/*@Override
	public Integer[] selectNextRow(InteractionSet ig, int k, int v){
		assert(ig instanceof IGSimple);
		Integer[] newRow = super.makeStarredRow(k, v);
		while (!allColsFixed(newRow, v)){
			Interaction interaction = ig.selectInteraction(newRow);
			if (interaction != null) {
				newRow = super.updateRow(newRow, interaction);
			} else {
				fillAllCols(newRow, v);
			}			
		}
		return newRow;		
	}*/

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
