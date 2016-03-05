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

package edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.bestofk;
import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpIteration;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;


public class BestOfKDensityHybrid implements CAGenAlgo {
	//private Random rand;
	protected String name;
	
	//time complexity computation
	protected int comp;
	
	protected CondExpEntries density;
	private CondExpIteration interaction;
	
	//private long cutOff;
	private int factor;
	private int numSamples;
	
	public BestOfKDensityHybrid(){
		super();
		//this.rand = new Random(1234L);
		this.name = new String("BestOfKDensityHybrid");
		
		//time complexity computation
		this.comp = 0;
		
		this.density = new CondExpEntries();
		this.interaction = new CondExpIteration();
		
		this.factor = 100;
		
		this.numSamples = 10;
	}

	@Override
	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;

	}

	@Override
	public CA generateCA(int t, int k, int v) {
		ListCA bestCa = new ListCA(t, k, v);
		long bestNumInteraction = Long.MAX_VALUE;
		//this.cutOff = Math.round(numInteraction * 0.20);
		long cutOff = this.setCutoff(t, k, v, this.factor);
		
		InteractionGraph bestIg = null;
		
		// best of K density sampling part
		for (int i = 0 ; i < this.numSamples; i++) {
			ListCA tempCa = new ListCA(t, k, v);
			InteractionGraph ig = new InteractionGraph(t, k, v);
			long numInteraction = ig.getNumInt();
			while (numInteraction > cutOff){
				this.density.setComp(0);
				Integer[] newRandRow = this.density.selectNextRow(ig, k, v);
				this.comp = this.density.getComp();
				
				int coverage = ig.deleteInteractions(newRandRow, null);
				tempCa.addRow(newRandRow);	
				
				numInteraction = numInteraction - coverage;
				
				// time complexity measurement
				tempCa.addComp(this.comp);
				tempCa.addCoverage(coverage);
			}
			
			int bestCaSize = bestCa.getNumRows();
			int tempCaSize = tempCa.getNumRows();
			
			System.out.println("size of CA: " + tempCaSize + ", i = " + i );
			
			if ((bestCaSize == 0) || (tempCaSize <= bestCaSize)){
				if ((tempCaSize < bestCaSize) || (numInteraction < bestNumInteraction)){
					bestCa = tempCa;
					bestIg = ig;
					bestNumInteraction = numInteraction;
				}
			}
		}
		
		// interaction algorithm part
		while(!bestIg.isEmpty()) {
			this.interaction.setComp(0);
			Integer[] newRandRow = this.interaction.selectNextRow(bestIg, k, v);
			this.comp = this.interaction.getComp();
			
			int coverage = bestIg.deleteInteractions(newRandRow, null);
			bestCa.addRow(newRandRow);	
			
			bestNumInteraction = bestNumInteraction - coverage;
			
			// time complexity measurement
			bestCa.addComp(this.comp);
			bestCa.addCoverage(coverage);
		}
		
		return bestCa;
	}
	
	private long setCutoff(int t, int k, int v, int factor) {
		double cutOffSq = factor * v * this.choose(k - 1, t - 1) * Math.pow(v, t - 1);
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
		return this.name;
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		// TODO Auto-generated method stub
		return null;
	}

}
