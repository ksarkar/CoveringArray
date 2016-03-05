package edu.asu.ca.kaushik.scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class ConflictGraphEstimateCheck {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int t = 6;
		int k = 20;
		int v = 3;
		int N1 = 6000;
		int N2 = 8000;
		int gap = 5;
		
		int repeat = 10;
		
		String fileName = "data\\out\\lll\\" + t + "-k-" + v + "\\" 
				+ t + "-" + k + "-" + v + "-average-uncov-int.csv";
		
		PartialArrayGen pa = new PartialArrayGen();
		writeHeader(fileName);
		
		System.out.println(new Date());
		for (int N = N1; N <= N2; N = N + gap) {		
			System.out.println("N = " + N);
			
			for (int i = 0; i < repeat; i++) { 
				pa.generatePartialArray(t,k,v,N);
				int numInts = pa.getNumUncovInt();
				int numEdges = pa.getNumEdges();
				
				writeResults(fileName, N, numInts, numEdges);
			}
		}
		
		System.out.println(new Date());

	}

	private static void writeResults(String fileName, int N, int numInts,
			int numEdges) throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, true));
			out.println("" + N + "," + numInts + "," +  numEdges);			
			
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
			out.println("Partial Array Size, Num Uncov Int, Num Edges");
			
			
		} finally {
			if (out != null){
				out.close();
			}
		}		
	}

}
