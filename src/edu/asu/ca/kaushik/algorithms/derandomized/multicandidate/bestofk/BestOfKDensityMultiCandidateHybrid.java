package edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.bestofk;
import edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.DensityMultCandidateRow;


public class BestOfKDensityMultiCandidateHybrid extends BestOfKDensityHybrid {

	public BestOfKDensityMultiCandidateHybrid() {
		super();
		
		super.name = new String("BestOfK-DensityMultiCandidate-Hybrid");
		super.density = new DensityMultCandidateRow();
	}
	
}
