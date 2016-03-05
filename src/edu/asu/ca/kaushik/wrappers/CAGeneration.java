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

package edu.asu.ca.kaushik.wrappers;
import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.pe.CondExpIterPE;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;


public class CAGeneration {
	
	private CAGenAlgo algo;
	
	public CAGeneration() {
	}

	public CAGeneration(CAGenAlgo a) {
		this.algo = a;
	}
	
	public void setCAGenAlgo(CAGenAlgo a) {
		this.algo = a;
	}
	
	public void setNumSamples(int numSamples) {
		this.algo.setNumSamples(numSamples);
	}
	
	public CA generateCA(int t, int k, int v) {
		return this.algo.generateCA(t, k, v);
	}
	
	public CA completeCA(PartialCA partialCA) {
		return this.algo.completeCA(partialCA);
	}
	
	private String getAlgoName() {
		return this.algo.getName();
	}	
	
	public static void main(String args[]) {
		CAGeneration c = new CAGeneration();
		
		// UniformRandom, PropRand, RepeatedRand, 
		// CondExpEntries, CondExpOneInteraction, CondExpIteration, Hybrid, DensityMultCandidateRow, 
		// CondExpIterPE
		c.setCAGenAlgo(new CondExpIterPE());
		c.setNumSamples(100);
		int k = 15;
		CA ca = c.generateCA(4, k, 3);
		System.out.println(ca.getNumRows());
		System.out.println(c.getAlgoName());
		//System.out.println(ca.getComp());
		//System.out.println(ca.getCoverage());
	}

}
