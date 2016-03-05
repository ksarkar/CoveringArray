package edu.asu.ca.kaushik.outputformatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.Instrumentation;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.scripts.Runner;


public class ComputeCoverageOutputFormatter implements OutputFormatter {
	private List<String> algoNames;
	private String dir = "data\\out\\comp-cov\\";
	private String newDirName;
	private int t;
	private int v;

	@Override
	public void setAlgoNames(List<String> algoNames) {
		this.algoNames = algoNames;
	}

	@Override
	public void preprocess(int t, int v) throws IOException {
		this.t = t;
		this.v = v;
		
		this.newDirName = "" + t + "-" + "k" + "-" + v;
		
		File newDir = new File(this.dir + this.newDirName);
		newDir.mkdirs();
	}

	@Override
	public void output(int k, List<CA> results) throws IOException {
		for (int i = 0; i < results.size(); i++){
			this.writeToFile(k, this.algoNames.get(i), results.get(i));
		}

	}

	private void writeToFile(int k, String algoName, CA ca) throws IOException {
		String filename = this.getFileName(k, algoName);
		
		Instrumentation instru = ca.getInstrumentation();
		int numRows = ca.getNumRows();
	
		List<Integer> comp = instru.getComp();
		List<Integer> cov = instru.getCoverage();
		List<int[]> covD = instru.getCovDist();
		List<Boolean> status = instru.getStatus();
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(filename, true));
			
			String header = this.getHeader(k);
			out.println(header);
			for (int i = 0; i < numRows; i++){
				String cDSt = Arrays.toString(covD.get(i));
				String covDStr = cDSt.substring(1, cDSt.length()-1);
				String compStr = (comp.size() == 0)? "0" : comp.get(i).toString();
				String covStr = (cov.size() == 0)? "0" : cov.get(i).toString();
				String stStr = (status.size() == 0)? "FALSE" : status.get(i).toString();
				out.println(this.getRowString(i+1, compStr, covStr, stStr, covDStr));
			}
			
		} finally {
			if (out != null){
				out.close();
			}
		}
		
	}

	private String getHeader(int k) {
		StringBuilder rowNums =  new StringBuilder();
		for (int i = 0; i < k; i++) {
			rowNums.append((i+1) + ",");
		}
		return "row_number, computation, coverage, status," + rowNums.toString();
	}
	
	private String getRowString(int i, String compute, String cov, 
			String status, String covD){
		return "" + i + "," + compute + "," + cov + "," + status +  "," + covD + "," ;
	}

	private String getFileName(int k, String algoName) {
		//return dir + "\\" + this.t + "-" + "k" + "-" + this.v + "\\" + this.t + "-" + k + "-" + this.v + "-" + algoName + ".csv";
		return dir + newDirName + "\\" +  this.t + "-" + k + "-" + this.v + "-" + algoName + ".csv";
	}
	
	public static void main(String[] args) throws IOException {
		int t = 6;
		int v = 3;
		
		int k1 = 15;
		int k2 = 15;
		
		List<String> algoList = new ArrayList<String>();
		
		//algoList.add("CondExpEntries");
		//algoList.add("Hybrid");
		//algoList.add("CondExpIteration");
		//algoList.add("AvCovRow");
		//algoList.add("MaxUncovColRow");
		//algoList.add("AllColAvCovRow");
		//algoList.add("AllColBalancedRow");
		//algoList.add("BiasedDensity");
		//algoList.add("BalancedDensityUniProb");
		algoList.add("BalancedDensityBiasedProb");
		
		OutputFormatter formatter = new ComputeCoverageOutputFormatter();
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgo(algoList);
		runner.run();
	}

}
