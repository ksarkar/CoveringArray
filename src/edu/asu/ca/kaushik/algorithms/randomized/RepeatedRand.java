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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;


public class RepeatedRand extends RandCAGenAlgo {
	
	private Random rand;
	
	public RepeatedRand() {
		super();
		super.name = new String("Repeated Random");
		this.rand = new Random(1234L);
	}

	@Override
	protected Integer[] createRandRow(InteractionGraph ig, int k, int v) {
		ig.setRand(this.rand);
		InteractionGraph igCopy = new InteractionGraph(ig);
		
		Integer[] newRow = new Integer[k];
		for (int i = 0; i < newRow.length; i++){
			newRow[i] = new Integer(v);
		}
		
		//System.out.println("starting:\n" + igCopy.toString() + "\n\n");
		
		while(!this.terminateP(newRow, v, igCopy)) {
			Interaction interaction = igCopy.getPropRandInteraction(newRow);
			newRow = this.updateRow(newRow, interaction);
			
			igCopy.deleteFullyDeterminedInteractions(newRow);
			igCopy.deletIncompatibleInteractions(interaction);	
			
			//System.out.println(interaction.toString());
			//System.out.println("row:\n" + getStringRow(newRow));
			//System.out.println(igCopy.toString() + "\n");
		}
		
		this.fixAllCols(newRow, v);
		return newRow;
	}


	private Integer[] updateRow(Integer[] newRow, Interaction interaction) {
		int[] fixCols = interaction.getCols().getCols();
		int[] fixSyms = interaction.getSyms().getSyms();
		for (int i = 0; i < fixCols.length; i++) {
			newRow[fixCols[i]] = new Integer(fixSyms[i]);
		}
		return newRow;
	}

	private void fixAllCols(Integer[] newRow, int v) {
		for (int i = 0; i < newRow.length; i++) {
			if (newRow[i].intValue() == v){
				newRow[i] = new Integer(this.rand.nextInt(v));
			}
		}
	}

	private boolean terminateP(Integer[] newRow, int v, InteractionGraph igCopy) {
		return igCopy.isEmpty() || this.allColsFixedP(newRow, v);
	}

	private boolean allColsFixedP(Integer[] newRow, int v) {
		for (Integer i : newRow) {
			if (i.intValue() == v){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		int t = 6;
		int v = 2;
		
		int k1 = 7;
		int k2 = 10;
		
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new UniformRandom());
		algoList.add(new PropRand());
		algoList.add(new RepeatedRand());

		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\randomized\\comprehensive\\");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}
	
}
