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
import java.util.Random;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.GroupLLLCA;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.algorithms.twostage.group.GTSDensity;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class GroupLLL implements CAGenAlgo {
	
	private int gId;
	private String name;
	
	private static String[] groupName = {"Trivial", "Cyclic", "Frobenius"};
	
	public GroupLLL(int gId) {
		this.gId = gId;
		this.name = "GroupLLL-" + groupName[gId];
	}
	
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		
		int n = this.arraySize(t,k,v);
		GroupLLLCA ca = new GroupLLLCA(t, k, v, n, this.gId, new Random());
		ColGrIterator clGrIt = new ColGrLexIterator(t, k);
		boolean allCovered;
		int colGrNum;
		int iteration = 1;
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			clGrIt.rewind();
			allCovered = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				ca.resetCovered();
				List<Interaction> notCovered = ca.notCovered(cols);
				if (!notCovered.isEmpty()) {
					ca.reSample(cols);
					allCovered = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					System.out.println(new Date());
					break;
				}
				colGrNum++;
			}
			iteration++;
		} while(!allCovered);
		
		Group g = ca.getGroup();
		ListCA lca = new ListCA(t,k,v);
		lca.join(ca);
		ListCA fullCA = g.develop(lca);
		
		/*System.out.println(new Date());
		
		ArrayCA testCA = new ArrayCA(fullCA);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println("\nThis " + (testCA.isCompleteCA(cols) ? "is a CA" 
				: "is not a CA\n"));*/
		
		
		return fullCA;
	}
	
	/**
	 * Size of the smaller array is (1 + ln (d * A)) / ln B
	 * Where d = choose(k,t) - choose(k-t,t), and
	 * A = v^t, B = A/(A-1) for the Trivial group,
	 * A = v^{t-1}, B = A/(A-1) for the cyclic group, and 
	 * A = (v^{t-1}-1)/(v-1), B = v^{t-1}/(v^{t-1}-v+1).
	 * @param t
	 * @param k
	 * @param v
	 * @return
	 */
	private int arraySize(int t, int k, int v) {
		double d = CombinatoricsUtils.binomialCoefficientDouble(k, t) 
				- CombinatoricsUtils.binomialCoefficientDouble(k-t, t);
		double A = 0.0d;
		double B = 0.0d;
		
		switch(this.gId) {
		case 0: // trivial group
				A = Math.pow(v, t);
				B = A / (A-1);
				break;
		case 1: // cyclic group
				A = Math.pow(v, t-1);
				B = A / (A-1);
				break;
		case 2: // Frobenius group
				double vtm1 = Math.pow(v, t-1);
				A = (vtm1 - 1) / (v - 1);
				B = vtm1 / (vtm1 - v + 1);
				break;
		default:
				System.err.println("Invalid group Id.");
				System.exit(1);
		}
		
		double nume = 1.0d + Math.log(d * A);
		double dnom = Math.log(B);
		
		return (int)Math.ceil(nume/dnom);
	}


	@Override
	public void setNumSamples(int numSamples) {
		assert false;
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		assert false;
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		int t = 0, k1 = 0, k2 = 0, v = 0,  g = -1;
		
		if (args.length == 5) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
			g = Integer.parseInt(args[4]);
		} else {
			System.err.println("Need 5 arguments- t, v, kStart, kEnd, and group type");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new GroupLLL(g));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\lll"
				, "GroupLLL");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
