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

package edu.asu.ca.kaushik.algorithms.permvector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.permvector.utils.CPHF;
import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator2;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class MTPermutationVector implements CAGenAlgo {

	@Override
	public void setNumSamples(int numSamples) {
		// Dummy method.

	}

	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		int n = permVecLLLBound(t,k,v);
		System.out.println("CPHF size: " + n);
		
		CPHF c = new CPHF(n,k,v,t);
		ColGrIterator clGrIt = new ColGrIterator2(t, k);
		boolean isCPHF;
		int colGrNum;
		int iteration = 1;
		
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			clGrIt.rewind();
			isCPHF = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				if (!c.containsCoveringTuple(cols)) {
					c.reSample(cols);
					isCPHF = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					break;
				}
				colGrNum++;
			}
			iteration++;
		} while(!isCPHF);
		
		System.out.println("CPHF constructed.");
		
		CA ca = c.convertToCA();
		
		/*Iterator<Integer[]> caIt = ca.iterator();
		while (caIt.hasNext()) {
			System.out.println(Arrays.toString(caIt.next()));
		}*/
		
		ArrayCA aca = (ArrayCA)ca;
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println("\nThis " + (aca.isCompleteCA(cols) ? "is a CA" 
				: "is not a CA\n"));
		
		return ca;
	}

	private int permVecLLLBound(int t, int k, int v) {
		double vTotm1 = Math.pow(v, t-1);
		double vTotm2 = Math.pow(v, t-2);
		
		double nume1 = CombinatoricsUtils.factorialDouble(t);
		double nume2 = CombinatoricsUtils.binomialCoefficientDouble((int)vTotm1, t);
		double nume3 = v * (vTotm1 - 1) / (v - 1);
		double nume4 = CombinatoricsUtils.binomialCoefficientDouble((int)vTotm2, t);
		double nume = nume1 * (nume2 - nume3 * nume4);
		double dnom = Math.pow(vTotm1, t);
		double q = 1 - (nume / dnom);
		
		double d = CombinatoricsUtils.binomialCoefficientDouble(k, t) 
				- CombinatoricsUtils.binomialCoefficientDouble(k-t, t) - 1;
		
		return (int)Math.ceil((1 + Math.log(d + 1)) / Math.log(1/q));
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "MTPermutationVector";
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
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
		
		algoList.add(new MTPermutationVector());
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\perm-vec"
				, "perm-vec");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
