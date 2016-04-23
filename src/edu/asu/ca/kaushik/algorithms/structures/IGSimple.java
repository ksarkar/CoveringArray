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

public class IGSimple extends InteractionGraph {

	public IGSimple(int t, int k, int v) {
		super(t, k, v);
	}
	/*
	@Override
	public Interaction selectInteraction(Integer[] row) {		
		Set<Map.Entry<ColGroup, Set<SymTuple>>>	entries = super.graph.entrySet();
		
		double maxExpCoverage = 0.0d;
		Interaction bestInteraction = null;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : entries){
			ColGroup colGr = entry.getKey();
			Set<SymTuple> tuples = entry.getValue();
			for (SymTuple tuple : tuples) {
				Interaction interaction = new Interaction(colGr, tuple);
				if (this.isCompatible(interaction, row) {
					double expCoverage = this.computeInteractionCoverage(interaction, row);
					if (expCoverage > maxExpCoverage){
						maxExpCoverage = expCoverage;
						bestInteraction = interaction;
						
					}
				}
			}
		}
		
		return bestInteraction;
	}
	
	@Override
	protected double computeInteractionCoverage(Interaction interaction, Integer[] row) {
		//InteractionGraph igCopy = new InteractionGraph(this);
		//igCopy.deletIncompatibleInteractions(interaction);
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>>	entries = super.graph.entrySet();
		
		double expCoverage = 0.0d;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : entries){
			ColGroup colGr = entry.getKey();
			Set<SymTuple> tuples = entry.getValue();
			//TODO 
			expCoverage = expCoverage + tuples.size() 
					* calculateNumCoveringRows(row, colGr, interaction.getCols(), igCopy.v);
		}
		
		expCoverage = expCoverage 
				/ calculateNumCoveringRows(row, interaction.getCols(), interaction.getCols(), igCopy.v);
		
		return expCoverage;
	}

	*//**
	 * @param args
	 *//*
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/

}
