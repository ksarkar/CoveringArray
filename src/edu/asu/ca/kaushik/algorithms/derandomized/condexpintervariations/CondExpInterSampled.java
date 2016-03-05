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

package edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpIteration;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class CondExpInterSampled implements CAGenAlgo {
	protected String name;
	
	private CondExpIteration interactionAlgo;
	
	private long cutOff;
	private int factor;
	
	public CondExpInterSampled(int factor) {
		this.name = new String("CEI-Sampled");
		this.interactionAlgo = new CondExpIteration(); 
		this.factor = factor;
	}

	@Override
	public void setNumSamples(int numSamples) {
		// dummy method. Here used for setting the factor
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		ListCA ca = new ListCA(t, k, v);
		InteractionGraph ig = new InteractionGraph(t, k, v);
		long numInteraction = ig.getNumInt();

		this.cutOff = this.setCutoff(t, k, v, this.factor);
		InteractionGraph sampledIg = null;
		
		while(!ig.isEmpty()) {						
			if (numInteraction > this.cutOff) {
				sampledIg = ig.sampledIG(this.cutOff);
				int covSum = 0;
				// while (!sampledIg.isEmpty()) { // version 1
				while (!sampledIg.isEmpty() && (covSum < this.cutOff)) {
					Integer[] newRandRow 
						= this.interactionAlgo.selectNextRow(sampledIg, k, v);
					int coverage = ig.deleteInteractions(newRandRow, sampledIg, null);
					covSum = covSum + coverage;
					ca.addRow(newRandRow);		
					numInteraction = numInteraction - coverage;
					ca.addCoverage(coverage);
				}
				
			} else {
				Integer[] newRandRow = this.interactionAlgo.selectNextRow(ig, k, v);			
				int coverage = ig.deleteInteractions(newRandRow, null);
				ca.addRow(newRandRow);		
				numInteraction = numInteraction - coverage;
				ca.addCoverage(coverage);
			}

		}
		
		return ca;
	}
	
	private long setCutoff(int t, int k, int v, int factor) {
		double cutOffSq = factor * v * this.choose(k - 1, t - 1) * Math.pow(v, t - 1); // funny!
		return Math.round(Math.sqrt(cutOffSq));
	}

	private long choose(int n, int k) {
		assert((n >= 0) && (k >= 0) && (k <= n));
		
		if (k == 0) {
			return 1;
		} else if (n == k) {
			return 1;
		} else {
			return this.choose(n - 1, k - 1) + this.choose(n - 1, k);
		}
	}



	@Override
	public String getName() {
		return this.name + "f" + this.factor;
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		int t = 3;
//		int k = 50;
//		int v = 2;
//		
//		int factor = 1000;
//		
//		CAGeneration c = new CAGeneration();
//		System.out.println("Cond. Exp. Interaction - Sampled");
//		c.setCAGenAlgo(new CondExpInterSampled(factor));
//		StandardCAWriter writer = new StandardCAWriter();
//		writer.writeCA(c.generateCA(t,k,v), 
//				"data\\out\\explicit-arrays\\" + t + "-k-" + v + "\\" + t + "-" + k + "-" + v + "-int-sampled-v2-f" + factor + ".txt");
		
		int t = 4;
		int v = 2;
		
		int k1 = 53;
		int k2 = 70;
		
		//List<String> algoList = getAllDetAlgos();
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new CondExpEntries());
		algoList.add(new CondExpInterSampled(10));
		algoList.add(new CondExpInterSampled(100));
		algoList.add(new CondExpInterSampled(1000));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\CEI-s\\");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
