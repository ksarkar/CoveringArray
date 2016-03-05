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

package edu.asu.ca.kaushik.algorithms.randomized;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;


public class PropRand extends RandCAGenAlgo {
	
	private Random rand;

	public PropRand() {
		super();
		super.name = new String("Proportional Random");
		this.rand = new Random(1234L);
	}

	@Override
	protected Integer[] createRandRow(InteractionGraph ig, int k, int v) {
		ig.setRand(this.rand);
		Integer[] randRow = createUniformRandRow(k, v);
		
		Interaction interaction = ig.getUniformRandInteraction();
		int[] cols = interaction.getCols().getCols();
		int[] syms = interaction.getSyms().getSyms();
		
		for (int i = 0; i < cols.length; i++){
			randRow[cols[i]] = syms[i];
		}
		
		//System.out.println(ig.toString());
		//System.out.println(interaction.toString());
		//System.out.println("row:\n" + getStringRow(randRow) + "\n\n");
		
		return randRow;
	}
	
	private Integer[] createUniformRandRow(int k, int v) {
		Integer[] randRow = new Integer[k];
		
		for (int i = 0; i < k; i++){
			randRow[i] = new Integer(this.rand.nextInt(v));
		}
		return randRow;
	}

}
