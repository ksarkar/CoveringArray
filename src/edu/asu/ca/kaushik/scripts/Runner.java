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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.wrappers.CAGenAlgoFactory;


public class Runner {

	private int t;
	private int v;
	private Iterator<Integer> kItr;
	List<CAGenAlgo> algos;
	
	private OutputFormatter formatter;
	
	public Runner(OutputFormatter formatter){
		this.algos = new ArrayList<CAGenAlgo>();
		this.formatter = formatter;
	}

	public void setParam(int t, int v, final int k1, final int k2, int ks) {
		this.t = t;
		this.v = v;
		
		this.kItr = new Iterator<Integer>() {
			
			int counter = k1;

			@Override
			public void forEachRemaining(Consumer<? super Integer> arg0) {
				// Dummy
				
			}

			@Override
			public boolean hasNext() {
				return counter <= k2;
			}

			@Override
			public Integer next() {
				return counter++;
			}

			@Override
			public void remove() {
				// Dummy
				
			}
			
		};
	}
	
	public void setParam(int t, int v, int k1, int k2) {
		this.setParam(t, v, k1, k2, 1);
	}
	
	public void setParam(int t, int v, Iterator<Integer> kIt) {
		this.t = t;
		this.v = v;
		this.kItr = kIt;
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
	
		while (this.kItr.hasNext()){
			int k = this.kItr.next();
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
