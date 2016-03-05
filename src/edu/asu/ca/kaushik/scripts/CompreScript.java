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

package edu.asu.ca.kaushik.scripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import edu.asu.ca.kaushik.algorithms.randomized.PropRand;
import edu.asu.ca.kaushik.algorithms.randomized.RandCAGenAlgo;
import edu.asu.ca.kaushik.algorithms.randomized.RepeatedRand;
import edu.asu.ca.kaushik.algorithms.randomized.UniformRandom;
import edu.asu.ca.kaushik.algorithms.structures.CA;

public class CompreScript {

	private int t;
	private int v;
	private int k_start;
	private int k_fin;
	private int k_step;
	private int numRuns;
	private List<RandCAGenAlgo> algos;
	private List<String> algoNames;
	
	private String dirName;

	public void setParam(int t, int v, int k1, int k2, int ks, int numRuns) {
		this.t = t;
		this.v = v;
		this.k_start = k1;
		this.k_fin = k2;
		this.k_step = ks;
		this.numRuns = numRuns;
	}
	
	public void setParam(int t, int v, int k1, int k2, int numRuns) {
		this.setParam(t, v, k1, k2, 1, numRuns);
	}
	
	public void setAlgos(List<RandCAGenAlgo> algos) {
		this.algos = algos;
		this.algoNames = new ArrayList<String>();
		for (RandCAGenAlgo alg : algos) {
			algoNames.add(alg.getName());
		}
	}
	
	private void setOutputFolder(String dirName) {
		this.dirName = dirName;
	}
	
	public void run() throws IOException {	
		File newDir = new File(this.dirName);
		newDir.mkdirs();
		
		for (RandCAGenAlgo algo : this.algos){
			this.writeResult(algo, this.runAlgo(algo));
		}
	}

	private int[][] runAlgo(RandCAGenAlgo algo) {
		System.out.println("Running: " + algo.getName());
		int[][] sizes = new int[this.numRuns + 1][this.k_fin - this.k_start + 1];
		for (int k = this.k_start; k <= this.k_fin; k = k + this.k_step){
			this.runOnK(algo, k, sizes);
		}
		
		return sizes;
	}
	
	private void runOnK(RandCAGenAlgo algo, int k, int[][] sizes) {
		int col = k - this.k_start;
		sizes[0][col] = k; // write the value of k in the first row
		for (int i = 0; i < this.numRuns; i++) {
			CA randCa = algo.generateRandCA(this.t, k, this.v);
			sizes[i+1][col] = randCa.getNumRows();
		}
	}
	
	private void writeResult(RandCAGenAlgo algo, int[][] sizes) throws IOException {
		String fileName = this.dirName + "\\" + this.t + "-" + this.k_start + "-" 
				+ this.k_fin + "-" + this.v + "-" + this.numRuns + "-" + algo.getName() + ".csv";
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, false));
			int m = this.numRuns + 1;
			int n = this.k_fin - this.k_start + 1;
			for (int i = 0 ; i < m; i++) {
				for (int j = 0; j < n; j++) {
					out.print(sizes[i][j] + ",");
				}
				out.println();
			}
			
		} finally {
			if (out != null){
				out.close();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		int t = 6;
		int v = 2;
		
		int k1 = 7;
		int k2 = 10;
		
		int numRuns = 2000;
		
		
		List<RandCAGenAlgo> algoList = new ArrayList<RandCAGenAlgo>();
		
		algoList.add(new UniformRandom());
		algoList.add(new PropRand());
		algoList.add(new RepeatedRand());

		
		CompreScript script = new CompreScript();
		script.setParam(t, v, k1, k2, numRuns);
		script.setAlgos(algoList);
		script.setOutputFolder("data\\out\\tables\\randomized\\comprehensive\\");
		script.run();

	}

}
