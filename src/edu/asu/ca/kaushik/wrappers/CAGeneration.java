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
