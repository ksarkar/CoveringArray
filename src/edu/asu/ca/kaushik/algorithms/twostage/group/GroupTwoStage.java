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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;

public abstract class GroupTwoStage implements CAGenAlgo	{
	private static final int maxIteration = 20;
	private int firstStage;
	private int slackPercent;
	private int gType;
	protected String name;
	
	private static String[] groupName = {"Trivial", "Cyclic", "Frobenius"};
	
	/**
	 * Two stage algorithm with group action
	 * @param f flag for the random partial array generation algorithm.
	 * 			Valid values: 0 -- uniform random, 
	 * 			1 -- re-sample only the last offending interaction, 
	 * 			2 -- re-sample all the offending interactions
	 * @param s slack in number of uncovered interaction (in percent).
	 * @param gId Type of group. 0 -- trivial group, 1 -- cyclic group, 2 -- Frobenius 
	 * 			group.
	 */
	public GroupTwoStage(int f, int s, int gId) {
		this.firstStage = f;
		this.slackPercent = s;
	
		this.name = groupName[gId];
		this.gType = gId;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		int n = this.partialArraySize(t,k,v, this.gType);
		int numMinUncovOrb = (int)Math.floor(this.expectNumUncovInt(t, k, v, n, this.gType));
		numMinUncovOrb = (int) (1.0 + this.slackPercent/100) * numMinUncovOrb;
		
		GroupLLLCA partialCa = new GroupLLLCA(t, k, v, n, this.gType, new Random());
		this.initSecondStage(partialCa.getGroup());
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
		
		System.out.println("First phase over.");
		System.out.println("Partial array size: " + n);
		System.out.println(new Date());
		System.out.println("Number of uncovered interactions: " + uncovOrbNum);
		
		
		ListCAExt remCA = new ListCAExt(t, k, v);
		this.secondStage(remCA);
		ListCA ca = new ListCAExt(t, k, v);
		ca.join(partialCa);
		ca.join(remCA);
		
		((ListCAExt)ca).copyInfo(remCA);
		
		System.out.println("Number of rows added in second phase: " + remCA.getNumRows());
		
		Group g = partialCa.getGroup();
		ListCA fullCA = g.develop(ca);
		
		
		ArrayCA testCA = new ArrayCA(fullCA);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println("\nThis " + (testCA.isCompleteCA(cols) ? "is a CA" 
				: "is not a CA\n"));
		
		
		return fullCA;
	}

	private void addCols(Set<Integer> badCols, ColGroup cols) {
		int[] c = cols.getCols();
		for (int i : c) {
			badCols.add(i);
		}
	}
	
	private double expectNumUncovInt(int t, int k, int v, int n, int gType2) {
		// TODO Auto-generated method stub
		return 0;
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

	
	protected abstract int partialArraySize(int t, int k, int v, int gType);
	protected abstract void initSecondStage(Group group);
	protected abstract void cover(List<Interaction> notCovered);
	protected abstract void reset();
	protected abstract void secondStage(ListCAExt remCA);

	@Override
	public void setNumSamples(int numSamples) {
		assert false;
	}
	
	@Override
	public CA completeCA(PartialCA partialCA) {
		assert false;
		return null;
	}

}
