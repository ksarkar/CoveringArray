package edu.asu.ca.kaushik.scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import edu.asu.ca.kaushik.algorithms.randomized.lll.LLLColoring;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class LLLColEmpirical {

	public static void main(String[] args) throws IOException {	
		int t = 6;
		int k = 20;
		int v = 3;
		int N1 = 5800;
		int N2 = 6495;
		int gap = 5;
		
		String fileName = "data\\out\\lll\\6-k-3\\" 
				+ t + "-" + k + "-" + v + "-LLL-col-empirical.csv";
		
		CAGeneration c = new CAGeneration();
		writeHeader(fileName);
		
		System.out.println(new Date());
		for (int N = N1; N <= N2; N = N + gap) {		
			System.out.println("N = " + N);
			c.setCAGenAlgo(new LLLColoring(N));
			CA ca = c.generateCA(t, k, v);
			writeResults(fileName, N, (ListCAExt)ca);
		}
		
		System.out.println(new Date());

	}

	private static void writeResults(String fileName, int N, ListCAExt ca) throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, true));
			out.println("" + N + "," + ca.getNumRows() + "," +  ca.getNumUncovInt() + "," +
					ca.getNumEdges() + "," + ca.getMaxDeg() + "," + ca.getMinDeg() + "," + ca.getNumColors());			
			
		} finally {
			if (out != null){
				out.close();
			}
		}
		
	}

	private static void writeHeader(String fileName) throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, false));
			out.println("Partial Array Size, Num Rows, Num Uncov Int, " +
					"Num edges, Max Deg, Min Deg, Num Extra Rows");
			
			
		} finally {
			if (out != null){
				out.close();
			}
		}
		
	}

	

}
