package edu.asu.ca.kaushik.algorithms;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;


public interface CAGenAlgo {
	
	/*
	 * Interface for strategy pattern implementation.
	 */
	
	public void setNumSamples(int numSamples);
	public CA generateCA(int t, int k, int v);
	public CA completeCA(PartialCA partialCA);
	public String getName();

}
