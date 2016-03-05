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

package edu.asu.ca.kaushik.algorithms.randomized.lll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.algorithms.structures.graph.ConflictGraph;
import edu.asu.ca.kaushik.outputformatter.CAWriter;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

/**
 * Two phase algorithm that generates a random partial covering array in the
 * first phase, and uses coloring to cover the remaining uncovered interactions
 * in the second phase.
 * 
 * @author ksarkar1
 *
 */

public class LLLColoring extends TwoStageSimpleMT {

	/**
	 * 
	 * @param N size of the partial covering array that is to be constructed
	 * 			randomly.
	 */
	public LLLColoring(int N) {
		super(N);
	}

	/**
	 * builds a partial covering array to cover the remaining uncovered 
	 * interactions. Constructs the conflict graph and colors it greedily
	 * to find out a small number of rows for covering the remaining 
	 * interactions.
	 * 
	 * @param remCA the remaining CA to be updated. (out)
	 * @param uncovInts the list of uncovered interactions (in)
	 */
	@Override
	protected void makeRemCA(ListCAExt remCA, List<Interaction> uncovInts) {		
		ConflictGraph conflictGraph = new ConflictGraph(uncovInts);
		conflictGraph.color(remCA);	
	}

	@Override
	public String getName() {
		return new String("LLL+Coloring");
	}
	
	/*
	public static void main(String[] args) {
		int[] col1 = {0, 1};
		int[] col2 = {1, 2};
		int[] col3 = {0, 1};
		
		int[] sym1 = {0, 1};
		int[] sym2 = {1, 0};
		int[] sym3 = {1, 0};
		
		Interaction int1 = new Interaction(new ColGroup(col1), new SymTuple(sym1));
		Interaction int2 = new Interaction(new ColGroup(col2), new SymTuple(sym2));
		Interaction int3 = new Interaction(new ColGroup(col3), new SymTuple(sym3));
		
		List<Interaction> ints = new ArrayList<Interaction>();
		ints.add(int1);
		ints.add(int2);
		ints.add(int3);
		
		ListCA ca = new ListCA(2,3,2);
		
		LLLColoring algo = new LLLColoring(3);
		algo.makeRemCA(ca, ints);
		System.out.println(ca);
		
	}*/
	
	public static void main(String[] args) throws IOException {
		CAWriter writer = new StandardCAWriter();
		CAGeneration c = new CAGeneration();
		
		int t = 6;
		int k = 20;
		int v = 3;
		int N = 4000;
		
		
		/*
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
			v = Integer.parseInt(args[2]);
			N = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need three arguments- t, k, v and N");
			System.exit(1);
		}
		*/
		System.out.println("LLL+Coloring");
		
		System.out.println(new Date());
		c.setCAGenAlgo(new LLLColoring(N));
		CA ca = c.generateCA(t, k, v);
		ListCAExt lca = (ListCAExt)ca;
		System.out.println("Number of uncovered interactions: " + lca.getNumUncovInt());
		System.out.println("Number of edges in the conflict graph: " + lca.getNumEdges());
		System.out.println("Maximum degree in the conflict graph " + lca.getMaxDeg());
		System.out.println("Minimum degree in the conflict graph: " + lca.getMinDeg());
		System.out.println("Number of extra rows: " + lca.getNumColors());
		
		System.out.println(new Date());
		
		writer.writeCA(ca, "data\\out\\lll\\6-k-3\\" + t + "-" + k + "-" + v + "-" + N +"-LLL-coloring.txt");
		
		System.out.println("LLL+Coloring Done");
	}

	
	
}
