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

package edu.asu.ca.kaushik.da.scripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.asu.ca.kaushik.algorithms.randomized.lll.IncrementalMTCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.da.algorithms.DAAlgoIface;
import edu.asu.ca.kaushik.da.algorithms.IncrementalMTDA;
import edu.asu.ca.kaushik.da.structures.DAIface;

public class Runner {

	public static void run(DAAlgoIface algo, int d, int t, int k1, int k2,
			int v, String dirName, String fileName) throws IOException {
		
		File newDir = new File(dirName);
		newDir.mkdirs();
		
		String fName = dirName + "\\" + fileName + "-" + d + "-" + t + "-" + v + ".csv";
		String header = "k, N";
		writeLine(fName, header);
		
		System.out.println(fileName);
		for (int k = k1; k <= k2; k++) {
			DAIface da = algo.constructDetectingArray(d, t, k, v);
			String line = k + ", " + da.getNumRows();
			System.out.println(line);
			writeLine(fName, line);
		}
	}

	public static void writeLine(String fName, String str) throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fName, true));
			out.println(str);
		} finally {
			if (out != null){
				out.close();
			}
		}		
	}

	public static void runMTDA(IncrementalMTDA algo, int d, int t, int k1,
			int k2, int v, int N, String dirName, String fileName) throws IOException {
		File newDir = new File(dirName);
		newDir.mkdirs();
		
		String fName = dirName + "\\" + fileName + "-" + d + "-" + t + "-" + v + ".csv";
		String header = "k, N";
		writeLine(fName, header);
		
		System.out.println(fileName);
		for (int k = k1; k <= k2; k++) {
			DAIface da = algo.constructDetectingArray(d, t, k, v, N);
			N = da.getNumRows();
			String line = k + ", " + N;
			System.out.println(line);
			writeLine(fName, line);	
		}
		
	}

	public static void runMTCA(IncrementalMTCA algo, int t, int k1, int k2,
			int v, int N, String dirName, String fileName) throws IOException {
		File newDir = new File(dirName);
		newDir.mkdirs();
		
		String fName = dirName + "\\" + fileName + "-"  + t + "-" + v + ".csv";
		String header = "k, N";
		writeLine(fName, header);
		
		System.out.println(fileName);
		for (int k = k1; k <= k2; k++) {
			CA ca = algo.generateCA(t, k, v, N);
			N = ca.getNumRows();
			String line = k + ", " + N;
			System.out.println(line);
			writeLine(fName, line);	
		}
	}

	public static void printAlgo(DAAlgoIface algo, int d, int t, int k,
			int[] v, String dirName, String algoName) throws IOException {
		File newDir = new File(dirName);
		newDir.mkdirs();
		
		String fileName = d + "-" + t + "-MDA-" + k + "-" + v[0] + "-" + v[v.length-1] + "-" + algoName + ".txt";  
		String fName = dirName + "\\" + fileName;
		
		DAIface da = algo.constructMixedDetectingArray(d, t, k, v);
		da.writeToFile(fName);
	}
	
	public static void printAlgo(DAAlgoIface algo, int d, int t, int k,
			int v, String dirName, String algoName) throws IOException {
		File newDir = new File(dirName);
		newDir.mkdirs();
		
		String fileName = d + "-" + t + "-DA-" + k + "-" + v + "-" + algoName + ".txt";  
		String fName = dirName + "\\" + fileName;
		
		DAIface da = algo.constructDetectingArray(d, t, k, v);
		da.writeToFile(fName);
	}
	
	public static void runMixed(DAAlgoIface algo, int d, int t, int k1, int k2,
			String dirName, String fileName) throws IOException {
		
		File newDir = new File(dirName);
		newDir.mkdirs();
		
		//String levelType = "balanced"; 
		String levelType = "skewed";
		
		String fName = dirName + "\\" + fileName + "-" + d + "-" + t + "-" + levelType + ".csv";
		String header = "k, N";
		writeLine(fName, header);
		
		System.out.println(fileName);
		for (int k = k1; k <= k2; k++) {
			//int[] v = getBalancedLevels(k);
			int[] v = getSkewLevels(k);
			DAIface da = algo.constructMixedDetectingArray(d, t, k, v);
			String line = k + ", " + da.getNumRows();
			System.out.println(line);
			writeLine(fName, line);
		}
	}
	
	public static int[] getSkewLevels(int k) {
		int[] v = new int[k];
		int l = k / 3;
		int i = 0;
		for (; i < k-l; i++) {
			v[i] = 2;
		}

		for (; i < k; i++) {
			v[i] = 4;
		}
		return v;
	}
	

	public static int[] getBalancedLevels(int k) {
		int[] v = new int[k];
		int l = k / 3;
		int i = 0;
		for (; i < l; i++) {
			v[i] = 2;
		}
		int r = (k % 3 == 2) ? l+1 : l;
		for (; i < r + l; i++) {
			v[i] = 3;
		}
		int s = (k % 3 != 0) ? r : l;
		for (; i < k; i++) {
			v[i] = 4;
		}
		return v;
	}

}
