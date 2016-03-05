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
