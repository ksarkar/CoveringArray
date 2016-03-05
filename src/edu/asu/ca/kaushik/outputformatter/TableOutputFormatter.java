package edu.asu.ca.kaushik.outputformatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.scripts.Runner;


public class TableOutputFormatter implements OutputFormatter {
	
	private List<String> algoNames;
	private String root = "data\\out\\tables\\";
	private String fileNamePrefix = "";
	private int t;
	private int v;
	
	public TableOutputFormatter(String dirName) {
		this.root = dirName;
	}
	
	public TableOutputFormatter(String dirName, String fileNamePrefix) {
		this.root = dirName;
		this.fileNamePrefix = fileNamePrefix;
	}
	
	@Override
	public void setAlgoNames(List<String> algoNames) {
		this.algoNames = algoNames;
	}

	@Override
	public void preprocess(int t, int v) throws IOException {
		this.t = t;
		this.v = v;
		
		//this.newDirName = "" + t + "-" + "k" + "-" + v;
		
		File newDir = new File(this.root);
		newDir.mkdirs();
		
		this.writeHeader();
	}
	
	private void writeHeader() throws IOException {
		String fileName = this.getFileName();
		String header = this.getHeader();
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, true));
			out.println(header);
		} finally {
			if (out != null){
				out.close();
			}
		}
		
	}
	
	private String getFileName() {
		return this.root+ "\\" + (this.fileNamePrefix.equals("") ? "" : (this.fileNamePrefix + "-"))
				+ this.t + "-k-" + this.v + ".txt";
	}

	private String getHeader() {
		String header = "k";
		for (int i = 0; i < this.algoNames.size(); i++){
			header = header + ", " + this.algoNames.get(i);
		}
		return header;
	}

	@Override
	public void output(int k, List<CA> results) throws IOException {
		this.writeToFile(k, results);

	}
	
	private void writeToFile(int k, List<CA> results) throws IOException {
		String fileName = this.getFileName();
		String row = this.getRowString(k, results);
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, true));
			out.println(row);
		} finally {
			if (out != null){
				out.close();
			}
		}
	}


	private String getRowString(int k, List<CA> results) {
		String row = k + "";
		for (CA ca : results){
			row = row + ", " + ca.getNumRows();
		}
		return row;
	}
	
	public static void main(String[] args) throws IOException {		
		//List<String> algoList = getAllDetAlgos();
		
		List<String> algoList = new ArrayList<String>();
		
		//algoList.add("CondExpEntries");
		algoList.add("DensityMultCandidateRow");
		//algoList.add("HybridMultiCandidate");
		//algoList.add("BestOfKDensityMultiCandidateHybrid");
		//algoList.add("BestOfKDensityHybrid");
		//algoList.add("Hybrid");
		//algoList.add("CondExpIteration");
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\");
		
		Runner runner = new Runner(formatter);
		runner.setAlgo(algoList);
		
		//runner.setParam(4, 3, 5, 15);
		//runner.run();
		
		//runner.setParam(5, 3, 6, 10);
		//runner.run();
		
		runner.setParam(6, 3, 17, 19);
		runner.run();
	}

}
