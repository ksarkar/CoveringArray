package edu.asu.ca.kaushik.algorithms.derandomized.multicandidate;
import edu.asu.ca.kaushik.algorithms.derandomized.Hybrid;


public class HybridMultiCandidate extends Hybrid {

	public HybridMultiCandidate() {
		super();
		super.density =  new DensityMultCandidateRow();
		super.name = new String("Hybrid-MultiCandidate");
	}
	
}
