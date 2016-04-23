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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.structures.graph.OrbitConflictGraph;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class GTSColoring extends GroupTwoStage {
	private List<Interaction> orbits;
	private Group g;
	

	public GTSColoring(int f, int times, int s, int gId) {
		super(f, times, s, gId);
		super.name = super.name + "-coloring-" + times + "-times";
		this.orbits = new ArrayList<Interaction>();
	}

	@Override
	protected void initSecondStage(int t, int k, int v, Group group) {
		this.g = group;
	}

	@Override
	protected void cover(List<Interaction> notCovered) {
		this.orbits.addAll(notCovered);
	}

	@Override
	protected void reset() {
		this.orbits = new ArrayList<Interaction>();

	}

	@Override
	protected void secondStage(ListCAExt remCA) {
		System.out.println("Creating conflict graph...");
		OrbitConflictGraph graph = new OrbitConflictGraph(this.orbits, this.g);
		System.out.println("Number of edges: " + graph.getNumEdges());
		graph.color(remCA);
		
		Iterator<Integer[]> it = remCA.iterator();
		while(it.hasNext()) {
			GTSOnlineGreedy.removeStarEntries(it.next(), remCA.getV());
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int t = 0, k1 = 0, k2 = 0, v = 0, times = 0, f = 0, s = 0, g = -1;
		
		if (args.length == 8) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
			times = Integer.parseInt(args[4]);
			f = Integer.parseInt(args[5]);
			s = Integer.parseInt(args[6]);
			g = Integer.parseInt(args[7]);
		} else {
			System.err.println("Need 8 arguments- t, v, kStart, kEnd, times, firstStage, slack percent and group type");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new GTSColoring(f, times, s, g));
		System.out.println("times = " + times);
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\group-two-stage"
				, "group-two-stage-coloring");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();

	}

}
