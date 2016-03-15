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

package edu.asu.ca.kaushik.algorithms.twostage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.structures.graph.ConflictGraph;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

/**
 * Two phase algorithm that generates a random partial covering array in the
 * first phase, and uses coloring to cover the remaining uncovered interactions
 * in the second phase.
 * 
 * @author ksarkar1
 *
 */
public class TwoStageColoring extends TwoStage {
	private int times;
	private List<Interaction> uncovInts; // to keep track of all the uncovered interactions
	
	/**
	 * 
	 * @param times how many times of v^t interactions will be left uncovered in the first stage.
	 * @param firstStage What randomized algorithm to use in stage 1. Valid values: 0 -- uniform random, 
	 * 1 -- re-sample only the last offending interaction, 2 -- re-sample all the offending interactions
	 */
	public TwoStageColoring(int times, int firstStage) {
		super(firstStage);
		this.times = times;
		this.uncovInts = new ArrayList<Interaction>();
	}

	@Override
	public String getName() {
		return new String("TwoStageColoring-" + this.times + "-times");
	}

	@Override
	protected int partialArraySize(int t, int k, int v) {
		double vpowt = Math.pow(v, t);
		double n1 = Math.ceil(Math.log(CombinatoricsUtils.binomialCoefficientDouble(k,t) * vpowt
				* Math.log(vpowt/(vpowt-1))) / Math.log(vpowt / (vpowt - 1)));
		double denom = Math.log(1 - (1/vpowt));
		double n = (Math.log(this.times) + n1 * denom) / denom;
		return (int)Math.ceil(n);
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
	protected void secondStage(ListCAExt remCA) {
		ConflictGraph conflictGraph = new ConflictGraph(this.uncovInts);
		conflictGraph.color1(remCA);
	}
	
	@Override
	protected void cover(List<Interaction> notCovered) {
		this.uncovInts.addAll(notCovered);
	}
	
	@Override
	protected void reset() {
		this.uncovInts = new ArrayList<Interaction>();	
	}

	public static void main(String[] args) throws IOException {
		int t = 0,k1 = 0,k2 = 0,v = 0;
		
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need four arguments- t, v, kStart and kEnd");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new TwoStageColoring(2, 2));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\two-stage"
				, "two-stage-coloring");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();

	}

}
