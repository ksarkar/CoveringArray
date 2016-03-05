package edu.asu.ca.kaushik.algorithms.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Collects data related to the performance of the CA gen algorithms.
 * 
 * @author ksarkar1
 *
 */

public class Instrumentation {
	
	private List<Integer> comp; // measures computation required for each row
	private List<Integer> coverage; // measures new coverage for each row
	
	private List<int[]> covDist;	// number of covered interaction for each row
	
	private List<Boolean> status; // If the row meets the selection criterion. Used in Rand algos.
	
	public Instrumentation() {
		// time complexity measurement
		this.comp = new ArrayList<Integer>();
		this.coverage = new ArrayList<Integer>();
		
		this.covDist = new ArrayList<int[]>();
		
		this.status = new ArrayList<Boolean>();
		
	}
	
	public void addComp(int comp) {
		this.comp.add(new Integer(comp));
	}
	
	public void addCoverage(int coverage) {
		this.coverage.add(new Integer(coverage));		
	}

	public List<Integer> getComp() {
		return comp;
	}

	public List<Integer> getCoverage() {
		return coverage;
	}
	
	public void addCovDist(int[] covD) {
		int[] cD = Arrays.copyOf(covD, covD.length);
		this.covDist.add(cD);
	}
	
	public List<int[]> getCovDist() {
		return this.covDist;
	}

	public void addStatus(boolean st) {
		this.status.add(st);
	}
	
	
	public List<Boolean> getStatus() {
		return this.status;
	}

}
