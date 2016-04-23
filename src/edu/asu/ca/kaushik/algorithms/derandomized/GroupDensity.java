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

package edu.asu.ca.kaushik.algorithms.derandomized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.OrbRepMap;
import edu.asu.ca.kaushik.algorithms.structures.OrbRepSet;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class GroupDensity implements CAGenAlgo {
	private int groupType;
	private String name;
	private Random rand;
	
	private static String[] groupName = {"Trivial", "Cyclic", "Frobenius"};
	
	/**
	 * Density algorithm with group action
	 * @param grType Type of group to be considered. 0 - Trival group (equivalent to 
	 * normal density algorithm), 1 - cyclic group, 2 - Frobenius group
	 */
	public GroupDensity(int grType) {
		this.groupType = grType;	
		this.name = "GroupDensity-" + groupName[this.groupType];
		this.rand = new Random();
	}

	@Override
	public void setNumSamples(int numSamples) {
		assert false;
	}

	@Override
	public CA generateCA(int t, int k, int v) {		
		OrbRepSet oSet = new OrbRepMap(t,k,v,this.groupType);
		ListCA oCA = this.constructOrbitCA(oSet);
		
		Group g = oSet.getGroup();
		ListCA ca = g.develop(oCA);	
		
		ArrayCA testCA = new ArrayCA(ca);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println("\nThis " + (testCA.isCompleteCA(cols) ? "is a CA" 
				: "is not a CA\n"));
		
		return ca;	
	}

	public ListCA constructOrbitCA(OrbRepSet oSet) {
		ListCA oCA = new ListCA(oSet.getT(), oSet.getK(), oSet.getV());
		while(!oSet.isEmpty()) {		
			Integer[] newRandRow = selectNextRow(oSet);
			int coverage = oSet.deleteOrbits(newRandRow);
			oCA.addRow(newRandRow);	
			oCA.addCoverage(coverage);
		}
		
		return oCA;	
	}

	private Integer[] selectNextRow(OrbRepSet oSet) {
		return this.fillRow(this.makeStarredRow(oSet.getK(), oSet.getV()), oSet);
	}
	
	private Integer[] fillRow(Integer[] newRow, OrbRepSet oSet) {
		List<Integer> indexList = this.makeIndexList(newRow, oSet.getV());
		
		int l = indexList.size();
		for (int i = 0; i < l; i++){
			int index = this.selectIndexUniRandWORep(indexList);
			newRow[index] = this.chooseSymbol(newRow, index, oSet);
		}
		
		return newRow;
	}
	
	private Integer chooseSymbol(Integer[] newRow, int index, OrbRepSet oSet) {
		Integer[] row = Arrays.copyOf(newRow, newRow.length);
		double maxCoverage = 0;
		int optSymb = 0;
		for (int symb = 0; symb < oSet.getV(); symb++){
			row[index] = new Integer(symb);
			double coverage = this.computeExpCoverageSymb(row, index, oSet);
			if (coverage > maxCoverage){
				maxCoverage = coverage;
				optSymb = symb;
			}
		}
		return new Integer(optSymb);
	}
	
	protected double computeExpCoverageSymb(Integer[] row, int index, OrbRepSet oSet) {
		Iterator<ColGroup> colIt = oSet.getColGrIterator();
		double expCoverage = 0;
		
		while (colIt.hasNext()){
			ColGroup colGr = colIt.next();
			if (colGr.contains(index)) {
				expCoverage = expCoverage + this.computeExpectedCoverage(colGr, row, oSet);
			}
		}
		return expCoverage;
	}
	
	protected double computeExpectedCoverage(ColGroup colGr, Integer[] row, OrbRepSet oSet) {		
		Group g = oSet.getGroup();
		Iterator<SymTuple> orbits = g.getOrbits(colGr, row).iterator();
		double size = 0.0d;
		double count = 0.0d;
		while(orbits.hasNext()) {
			size++;
			if (oSet.containsOrbit(new Interaction(colGr, orbits.next()))) {
				count++;
			}
		}
		
		return count / size;
	}
	
	private int selectIndexUniRandWORep(List<Integer> indexList) {
		int len = indexList.size();
		int randInd = this.rand.nextInt(len);
		int index = indexList.remove(randInd).intValue();
		return index;
	}
	
	private List<Integer> makeIndexList(Integer[] newRow, int v) {
		List<Integer> li = new ArrayList<Integer>();
		int k = newRow.length;
		for (int i = 0; i < k; i++) {
			if (newRow[i].intValue() == v) {
				li.add(new Integer(i));
			}
		}
		return li;
	}

	private Integer[] makeStarredRow(int k, int v) {
		Integer[] newRow = new Integer[k];
		for (int i = 0; i < k; i++){
			newRow[i] = new Integer(v);
		}
		
		return newRow;
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		assert false;
		return null;
	}

	@Override
	public String getName() {
		return this.name;
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
		
		algoList.add(new GroupDensity(2));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\groupDensity"
				, "group-density");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
