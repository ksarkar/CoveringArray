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

import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class BalancedDensityBiasedProb extends CondExpEntries {

	public BalancedDensityBiasedProb() {
		super();
		super.name = new String("BalancedDensityBiasedProb");
	}
	
	@Override
	protected double computeExpectedCoverage(ColGroup colGr, Integer[] row, int v,
			InteractionSet ig) {
		List<Integer> starredCols = new ArrayList<Integer>();
		int t = colGr.getLen();
		int[] syms = new int[t];
		int[] cols = colGr.getCols();
		
		for (int i = 0; i < t; i++){
			int s = row[cols[i]].intValue();
			if ( s == v){
				starredCols.add(new Integer(i));
			}
			else {
				syms[i] = s;
			}
		}
		
		int[] sCols = new int[starredCols.size()];
		for (int i = 0; i < sCols.length; i++) {
			sCols[i] = cols[starredCols.get(i)];
		}
		
		List<SymTuple> allBlankEntries = Helper.createAllSymTuples(starredCols.size(), v);
		
		int score = ig.getNumUncovInt(colGr);
		double expScore = 0;
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			for (int i = 0; i < entries.length; i++){
				syms[starredCols.get(i).intValue()] = entries[i];
			}
			
			if (ig.contains(new Interaction(colGr, new SymTuple(syms)))){
				expScore = expScore + (score * ig.computeProbCoverage(sCols, entries));
			}
		}
		
		// time complexity measurement
		super.comp = super.comp + allBlankEntries.size();
		
		return expScore;
	}
}
