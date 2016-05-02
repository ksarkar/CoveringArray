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

package edu.asu.ca.kaushik.algorithms.twostage.group;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.GroupLLLCA;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;

public class Script {
	private static final int maxIteration = 20;
	private int firstStage;
	private int slackPercent;
	protected int gType;
	private int times;
	private GroupTwoStage[] algos;
	
	private String dirName;
	private String fNamePref;
	private int t, v;
	
	
	public Script(int f, int times, int s, int gId) {
		this.firstStage = f;
		this.slackPercent = s;
		this.times = times;
	
		this.gType = gId;
		
		int numAlgos = times == 1 ? 4 : 3;
		this.algos = new GroupTwoStage[numAlgos];
		
		this.algos[0] = new GTSDensity(f,times,s,gId);
		this.algos[1] = new GTSOnlineGreedy(f,times,s,gId);
		this.algos[2] = new GTSColoring(f,times,s,gId);
		if (times == 1) {
			this.algos[3] = new GTSSimple(f,s,gId);
		}
		
	}

	public GroupLLLCA firstStage(int t, int k, int v) {
		assert(k >= 2 * t);
		int n = this.partialArraySize(t,k,v);
		System.out.println("Target partial array size: " + n);
		int numMinUncovOrb = (int)Math.floor(this.expectNumUncovOrbs(t, k, v, n));
		numMinUncovOrb = (int) (1.0 + this.slackPercent/100) * numMinUncovOrb;
		
		GroupLLLCA partialCa = new GroupLLLCA(t, k, v, n, this.gType, new Random());
		this.initSecondStage(t,k,v,partialCa.getGroup());
		ColGrIterator clGrIt = new ColGrLexIterator(t, k);
		int uncovOrbNum;
		int colGrNum;
		boolean enoughCovered;
		
		int iteration = 1;
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			Set<Integer> badCols = new HashSet<Integer>();
			uncovOrbNum = 0;
			clGrIt.rewind();
			enoughCovered = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				partialCa.resetCovered();
				List<Interaction> notCovered = partialCa.notCovered(cols);
				if (!notCovered.isEmpty()) {
					uncovOrbNum = uncovOrbNum + notCovered.size();
					this.addCols(badCols, cols);
					this.cover(notCovered);
				}
				if (uncovOrbNum > numMinUncovOrb) {
					this.reset();
					this.reSampleCols(partialCa, badCols, notCovered);
					enoughCovered = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					System.out.println(new Date());
					break;
				}
				colGrNum++;
			}
			iteration++;
		} while(!enoughCovered && iteration <= maxIteration);
		
		if (!enoughCovered) {
			System.out.println("output is not a covering array");
			//System.exit(1);
		}
		
		System.out.println("Number of uncovered orbits: " + uncovOrbNum);
		return partialCa;
	}

	private void addCols(Set<Integer> badCols, ColGroup cols) {
		int[] c = cols.getCols();
		for (int i : c) {
			badCols.add(i);
		}
	}
	
	/**
	 * number of rows in the partial array = ln(A * B * ln(C/(C-1))/times) / ln(C/(C-1))
	 * Where A = choose(k,t)
	 * B=C=v^t for the trivial group
	 * B=C=v^(t-1) for the cyclic gorup
	 * B=(v^(t-1)-1)/(v-1), C=v^(t-1)/(v-1) for the Frobenius group
	 */
	protected int partialArraySize(int t, int k, int v) {
		double A = CombinatoricsUtils.binomialCoefficientDouble(k, t);
		double B = 0.0d;
		double C = 0.0d;
		
		switch(this.gType) {
		case 0: // trivial group
				B = Math.pow(v, t);
				C = B;
				break;
		case 1: // cyclic group
				B = Math.pow(v, t-1);
				C = B;
				break;
		case 2: // Frobenius group
				double vtm1 = Math.pow(v, t-1);
				B = (vtm1 - 1) / (v - 1);
				C = vtm1 / (v - 1);
				break;
		default:
				System.err.println("Invalid group Id.");
				System.exit(1);
		}
		
		double ln = Math.log(C / (C - 1));
		
		return (int)Math.ceil(Math.log(A * B * ln / this.times) / ln);
	}
	
	/**
	 * Expected number of uncovered orbtis = choose(k,t) * B * (1 - 1/C)^n
	 * where B=C=v^t for trivial group
	 * B=C=v^(t-1) for cyclic group
	 * B=(v^(t-1)-1)/(v-1), C=v^(t-1)/(v-1) for Frobenius group.
	 * @param t
	 * @param k
	 * @param v
	 * @param n
	 * @return
	 */
	private double expectNumUncovOrbs(int t, int k, int v, int n) {
		double A = CombinatoricsUtils.binomialCoefficientDouble(k, t);
		double B = 0.0d;
		double C = 0.0d;
		
		switch(this.gType) {
		case 0: // trivial group
				B = Math.pow(v, t);
				C = B;
				break;
		case 1: // cyclic group
				B = Math.pow(v, t-1);
				C = B;
				break;
		case 2: // Frobenius group
				double vtm1 = Math.pow(v, t-1);
				B = (vtm1 - 1) / (v - 1);
				C = vtm1 / (v - 1);
				break;
		default:
				System.err.println("Invalid group Id.");
				System.exit(1);
		}
		
		double pr = 1 - 1/C;
		return A * B * Math.pow(pr, n);
	}
	
	private void reSampleCols(GroupLLLCA partialCa, Set<Integer> badCols, 
				List<Interaction> notCovered) {
		//this.reset();
		switch(this.firstStage) {
		case 0:
			uniformRandom(partialCa);
			break;
		case 1:
			MTMethodLastInt(partialCa, notCovered);
			break;
		case 2:
			MTMethodAllInts(partialCa, badCols);
			break;
		default :
			System.err.println("Unknown parameter for flag in the constructor for TwoStageSimple.");
			System.exit(1);
		}
	}

	private void MTMethodAllInts(GroupLLLCA partialCa, Set<Integer> badCols) {
		int len = badCols.size();
		int[] columns = new int[len];
		int ind = 0;
		for (Integer i : badCols) {
			columns[ind] = i;
			ind++;
		}
		partialCa.reSample(new ColGroup(columns));	
		
	}

	private void MTMethodLastInt(GroupLLLCA partialCa, List<Interaction> notCovered) {
		int len = notCovered.size();
		Interaction interaction = notCovered.get(len - 1);
		partialCa.reSample(interaction.getCols());		
	}

	private void uniformRandom(GroupLLLCA partialCa) {
		int k = partialCa.getK();
		int[] cols = new int[k];
		for (int i = 0; i < k; i++) {
			cols[i] = i;
		}
		partialCa.reSample(new ColGroup(cols));		
	}
	
	private void initSecondStage(int t, int k, int v, Group group) {
		for (int i = 0; i < this.algos.length; i++) {
			this.algos[i].initSecondStage(t, k, v, group);
		}
	}
	
	private void cover(List<Interaction> notCovered) {
		for (int i = 0; i < this.algos.length; i++) {
			this.algos[i].cover(notCovered);
		}
	}
	private void reset() {
		for (int i = 0; i < this.algos.length; i++) {
			this.algos[i].reset();
		}
	}
	
	private void directory(String dirName, String fileNamePref, int t, int v) throws IOException {
		this.dirName = dirName;
		this.fNamePref = fileNamePref;
		this.t = t;
		this.v = v;
		
		File newDir = new File(this.dirName);
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
		return this.dirName+ "\\" + (this.fNamePref.equals("") ? "" : (this.fNamePref + "-"))
				+ this.t + "-k-" + this.v + ".txt";
	}

	private String getHeader() {
		String header = "k";
		for (int i = 0; i < this.algos.length; i++){
			header = header + ", " + this.algos[i].getName();
		}
		return header;
	}
	
	private void writeK(int k) throws IOException {
		this.writeToFile("" + k);
	}

	private void appendToFile(ListCA fullCA) throws IOException {
		this.writeToFile(", " + fullCA.getNumRows());
	}
	
	private void addNewLine() throws IOException {
		this.writeToFile("\n");		
	}
	
	private void writeToFile(String s) throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(this.getFileName(), true));
			out.print(s);
		} finally {
			if (out != null){
				out.close();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		int t = 0, k1 = 0, k2 = 0, v = 0, times = 0, f = 0, s = 0, gId = -1;
		
		if (args.length == 8) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
			times = Integer.parseInt(args[4]);
			f = Integer.parseInt(args[5]);
			s = Integer.parseInt(args[6]);
			gId = Integer.parseInt(args[7]);
		} else {
			System.err.println("Need 8 arguments- t, v, kStart, kEnd, times, firstStage, slack percent and group type");
			System.exit(1);
		}
		
		Script scr = new Script(f, times, s, gId);
		scr.directory("data\\out\\tables\\group-two-stage", "group-two-stage", t,v);
		
		for (int k = k1; k <= k2; k++) {
			scr.writeK(k);
			System.out.println();
			System.out.println("k = " + k);
			
			System.out.println("Running first stage...");
			GroupLLLCA fca = scr.firstStage(t,k,v);
			System.out.println("Partial array size: " + fca.getNumRows());
			System.out.println(new Date());
			
			for (int i = 0; i < scr.algos.length; i++) {
				System.out.println();
				System.out.println("Running second stage...");
				System.out.println("Running " + scr.algos[i].getName() + "...");
				ListCAExt sca = new ListCAExt(t, k, v);
				scr.algos[i].secondStage(sca);
				ListCA ca = new ListCAExt(t, k, v);
				ca.join(fca);
				ca.join(sca);
				
				((ListCAExt)ca).copyInfo(sca);
				
				System.out.println("Number of rows added in second phase: " + sca.getNumRows());
				
				Group g = fca.getGroup();
				ListCA fullCA = g.develop(ca);
				
				System.out.println(new Date());
				
				/*ArrayCA testCA = new ArrayCA(fullCA);
				ColGroup cols = new ColGroup(new int[0]);
				System.out.println("\nThis " + (testCA.isCompleteCA(cols) ? "is a CA" 
						: "is not a CA\n"));*/
				
				
				scr.appendToFile(fullCA);
			}
			scr.addNewLine();
		}
	}
}
