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
import edu.asu.ca.kaushik.algorithms.derandomized.BalancedDensityBiasedProb;
import edu.asu.ca.kaushik.algorithms.derandomized.BalancedDensityUniProb;
import edu.asu.ca.kaushik.algorithms.derandomized.BiasedDensity;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpIteration;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpOneInteraction;
import edu.asu.ca.kaushik.algorithms.derandomized.Hybrid;
import edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.DensityMultCandidateRow;
import edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.HybridMultiCandidate;
import edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.bestofk.BestOfKDensityHybrid;
import edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.bestofk.BestOfKDensityMultiCandidateHybrid;
import edu.asu.ca.kaushik.algorithms.derandomized.pe.CondExpIterPE;
import edu.asu.ca.kaushik.algorithms.randomized.AllColAvCovRow;
import edu.asu.ca.kaushik.algorithms.randomized.AllColBalancedRow;
import edu.asu.ca.kaushik.algorithms.randomized.AvCovRow;
import edu.asu.ca.kaushik.algorithms.randomized.MaxUncovColRow;
import edu.asu.ca.kaushik.algorithms.randomized.lll.LLL;


public class CAGenAlgoFactory {

	public CAGenAlgo getAlgo(String s) {
		CAGenAlgo algo = null;
		
		if (s.equals("CondExpEntries")){
			algo = new CondExpEntries();
		} else if (s.equals("CondExpIteration")){
			algo = new CondExpIteration();
		} else if (s.equals("CondExpOneInteraction")){
			algo = new CondExpOneInteraction();
		} else if (s.equals("Hybrid")){
			algo = new Hybrid();
		} else if (s.equals("BestOfKDensityHybrid")){
			algo = new BestOfKDensityHybrid();
		} else if (s.equals("DensityMultCandidateRow")){
			algo = new DensityMultCandidateRow();
		} else if (s.equals("HybridMultiCandidate")){
			algo = new HybridMultiCandidate();
		} else if (s.equals("BestOfKDensityMultiCandidateHybrid")){
			algo = new BestOfKDensityMultiCandidateHybrid();
		} else if (s.equals("CondExpIterPE")){
			algo = new CondExpIterPE();
		} else if (s.equals("LLL")){
			algo = new LLL();
		} else if (s.equals("AvCovRow")){
			algo = new AvCovRow();
		} else if (s.equals("MaxUncovColRow")){
			algo = new MaxUncovColRow();
		} else if (s.equals("AllColAvCovRow")){
			algo = new AllColAvCovRow();
		} else if (s.equals("AllColBalancedRow")){
			algo = new AllColBalancedRow();
		} else if (s.equals("BiasedDensity")){
			algo = new BiasedDensity();
		} else if (s.equals("BalancedDensityUniProb")){
			algo = new BalancedDensityUniProb();
		} else if (s.equals("BalancedDensityBiasedProb")){
			algo = new BalancedDensityBiasedProb();
		} else {
			System.err.println("Algo name string does not match any existing algo");
		}
		
		return algo;
	}

}
