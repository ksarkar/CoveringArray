package edu.asu.ca.kaushik.scripts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.wrappers.CAGenAlgoFactory;


public class Runner {

	private int t;
	private int v;
	private int k_start;
	private int k_fin;
	private int k_step;
	List<CAGenAlgo> algos;
	
	private OutputFormatter formatter;
	
	public Runner(OutputFormatter formatter){
		this.algos = new ArrayList<CAGenAlgo>();
		this.formatter = formatter;
	}

	public void setParam(int t, int v, int k1, int k2, int ks) {
		this.t = t;
		this.v = v;
		this.k_start = k1;
		this.k_fin = k2;
		this.k_step = ks;
	}
	
	public void setParam(int t, int v, int k1, int k2) {
		this.setParam(t, v, k1, k2, 1);
	}
	
	public void setAlgo(List<String> algoNames){
		this.formatter.setAlgoNames(algoNames);
		CAGenAlgoFactory factory = new CAGenAlgoFactory();
		for (String s : algoNames){
			this.algos.add(factory.getAlgo(s));
		}
	}
	
	public void setAlgos(List<CAGenAlgo> algos) {
		this.algos = algos;
		List<String> algoNames = new ArrayList<String>();
		for (CAGenAlgo alg : algos) {
			algoNames.add(alg.getName());
		}
		this.formatter.setAlgoNames(algoNames);
	}
	
	public void run() throws IOException {
		this.formatter.preprocess(this.t, this.v);
	
		for (int k = this.k_start; k <= this.k_fin; k = k + this.k_step){
			this.runAlgos(k);
		}
	}

	private void runAlgos(int k) throws IOException {
		System.out.println("k = " + k);
		List<CA> results = new ArrayList<CA>();
		for (CAGenAlgo algo : this.algos){
			results.add(this.runAlgo(algo, k));
		}	
		System.out.println();
		
		this.formatter.output(k, results);
	}

	private CA runAlgo(CAGenAlgo algo, int k) {
		System.out.println(new Date());
		System.out.println(algo.getName() + " running...");
		CA ca = algo.generateCA(this.t, k, this.v);
		System.out.println(new Date());
		return ca;
	}


	public static void main(String[] args) throws IOException {
		int t = 3;
		int v = 2;
		
		int k1 = 5;
		int k2 = 15;
		
		//List<String> algoList = getAllDetAlgos();
		
		List<String> algoList = new ArrayList<String>();
		
		algoList.add("CondExpEntries");
		algoList.add("Hybrid");
		algoList.add("CondExpIteration");
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgo(algoList);
		runner.run();
	}


	private static List<String> getAllDetAlgos() {
		List<String> algoList = new ArrayList<String>();
		
		algoList.add("CondExpEntries");
		algoList.add("CondExpOneInteraction");
		algoList.add("CondExpIteration");
		
		return algoList;
	}

}
