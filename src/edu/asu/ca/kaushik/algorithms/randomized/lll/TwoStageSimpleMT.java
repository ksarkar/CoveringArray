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

package edu.asu.ca.kaushik.algorithms.randomized.lll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.randomized.PropRand;
import edu.asu.ca.kaushik.algorithms.randomized.RepeatedRand;
import edu.asu.ca.kaushik.algorithms.randomized.UniformRandom;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator2;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.outputformatter.CAWriter;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class TwoStageSimpleMT extends LLL {
	private int N;
	private static final int maxIteration = 20;
	
	public TwoStageSimpleMT(int N) {
		this.N = N;
	}
	
	public TwoStageSimpleMT() {}
	
	private int twoStageSimpleBound(int t, int k, int v) {
		double kChooset = CombinatoricsUtils.binomialCoefficientDouble(k, t);
		double vpowt = Math.pow(v, t);
		double denom = Math.log(vpowt / (vpowt - 1));
		double nume = Math.log(kChooset * vpowt * denom);
		return (int)Math.ceil(nume/denom);
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		this.N = this.twoStageSimpleBound(t,k,v);
		int numMinUncovInt = (int)Math.ceil(Helper.expectNumUncovInt(t, k, v, this.N));
		LLLCA partialCa = new LLLCA(t, k, v, this.N);
		ColGrIterator clGrIt = new ColGrIterator2(t, k);
		List<Interaction> uncovInts;
		int uncovIntNum;
		int colGrNum;
		boolean enoughCovered;
		
		int iteration = 0;
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			uncovInts = new ArrayList<Interaction>();
			uncovIntNum = 0;
			clGrIt.rewind();
			enoughCovered = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				partialCa.resetCovered();
				List<Interaction> notCovered = partialCa.notCovered(cols);
				if (!notCovered.isEmpty()) {
					uncovIntNum = uncovIntNum + notCovered.size();
					uncovInts.addAll(notCovered);
				}
				if (uncovIntNum > numMinUncovInt) {
					//partialCa.reSample(cols);
					this.reSampleCols(partialCa, uncovInts);
					enoughCovered = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					break;
				}
				colGrNum++;
			}
			iteration++;
		} while(!enoughCovered && iteration < maxIteration);
		
		if (!enoughCovered) {
			System.out.println("output is not a covering array");
			//System.exit(1);
		}
		
		System.out.println("First phase over.");
		
		ListCAExt remCA = new ListCAExt(t, k, v);
		this.makeRemCA(remCA, uncovInts);
		ListCA fullCA = new ListCAExt(t, k, v);
		fullCA.join(partialCa);
		fullCA.join(remCA);
		
		((ListCAExt)fullCA).copyInfo(remCA);
		
		/*LLLCA testCA = new LLLCA(fullCA);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println("This is " + (testCA.isCompleteCA(cols) ? "is a CA" 
				: "is not a CA"));*/
		
		return fullCA;
	}
	
	private void reSampleCols(LLLCA partialCa, List<Interaction> uncovInts) {
		for (Interaction intrctn : uncovInts) {
			ColGroup cols = intrctn.getCols();
			partialCa.reSample(cols);
		}
	}

	protected void makeRemCA(ListCAExt remCA, List<Interaction> uncovInts) {
		int k = remCA.getK();
		int t = remCA.getT();
		
		for (Interaction interaction : uncovInts) {
			int[] cols = interaction.getCols().getCols();
			int[] syms = interaction.getSyms().getSyms();
			Integer[] row = new Integer[k];
			for (int i = 0; i < k; i++) {
				row[i] = 0;
			}
			for (int i = 0;i < t; i++) {
				row[cols[i]] = syms[i];
			}
			
			remCA.addRow(row);
		}		
	}

	@Override
	public String getName() {
		return new String("TwoStageSimpleMT");
	}
	
	public static void main(String[] args) throws IOException {
		
		int t = 0,k1 = 0,k2 = 0,v = 0;
		
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need four arguments- t, v, kStart and kEnd");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new TwoStageSimpleMT());
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\two-stage"
				, "two-stage-simple");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
		
		/*CAWriter writer = new StandardCAWriter();
		CAGeneration c = new CAGeneration();
		
		int t = 6;
		int k = 54;
		int v = 3;
		
		//int N = 12123;
		
		
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
			v = Integer.parseInt(args[2]);
			N = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need three arguments- t, k, v and N");
			System.exit(1);
		}
		
		
		System.out.println("TwoStageSimple");
		
		System.out.println(new Date());
		c.setCAGenAlgo(new TwoStageSimple());
		CA ca = c.generateCA(t, k, v);
		System.out.println(new Date());
		
		writer.writeCA(ca, "data\\out\\lll\\6-k-3\\" + t + "-" + k + "-" + v + "-LLLExtension.txt");
		
		System.out.println("TwoStageSimple Done");*/
	}

}
