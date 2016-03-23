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

package edu.asu.ca.kaushik.algorithms.twostage;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator2;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;

public abstract class TwoStage implements CAGenAlgo {
	private static final int maxIteration = 20;
	private int firstStage;
	private int slackPercent;
	
	/**
	 * 
	 * @param flag What randomized algorithm to use in stage 1. Valid values: 0 -- uniform random, 
	 * 1 -- re-sample only the last offending interaction, 2 -- re-sample all the offending interactions
	 */
	public TwoStage(int flag, int sp) {
		this.firstStage = flag;
		this.slackPercent = sp;
	}
	
	@Override
	public void setNumSamples(int numSamples) {
		//dummy
	}
	
	@Override
	public CA completeCA(PartialCA partialCA) {
		//dummy
		return null;
	}

	@Override
	public abstract String getName();


	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		int n = this.partialArraySize(t,k,v);
		int numMinUncovInt = (int)Math.floor(Helper.expectNumUncovInt(t, k, v, n));
		numMinUncovInt = (int) (1.0 + this.slackPercent/100) * numMinUncovInt;
		long seed = 123456L;
		LLLCA partialCa = new LLLCA(t, k, v, n, new Random());
		ColGrIterator clGrIt = new ColGrIterator2(t, k);
		int uncovIntNum;
		int colGrNum;
		boolean enoughCovered;
		
		int iteration = 1;
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			Set<Integer> badCols = new HashSet<Integer>();
			uncovIntNum = 0;
			clGrIt.rewind();
			enoughCovered = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				partialCa.resetCovered();
				List<Interaction> notCovered = partialCa.notCovered(cols);
				if (!notCovered.isEmpty()) {
					uncovIntNum = uncovIntNum + notCovered.size();
					this.addCols(badCols, cols);
					this.cover(notCovered);
				}
				if (uncovIntNum > numMinUncovInt) {
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
		
		ListCAExt remCA = new ListCAExt(t, k, v);
		this.secondStage(remCA);
		ListCA fullCA = new ListCAExt(t, k, v);
		fullCA.join(partialCa);
		fullCA.join(remCA);
		
		((ListCAExt)fullCA).copyInfo(remCA);
		
		
		/*ArrayCA testCA = new ArrayCA(fullCA);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println("\nThis " + (testCA.isCompleteCA(cols) ? "is a CA" 
				: "is not a CA\n"));*/
		
		
		return fullCA;
	}

	private void addCols(Set<Integer> badCols, ColGroup cols) {
		int[] c = cols.getCols();
		for (int i : c) {
			badCols.add(i);
		}
	}

	private void reSampleCols(LLLCA partialCa, Set<Integer> badCols, List<Interaction> notCovered) {
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

	private void MTMethodAllInts(LLLCA partialCa, Set<Integer> badCols) {
		int len = badCols.size();
		int[] columns = new int[len];
		int ind = 0;
		for (Integer i : badCols) {
			columns[ind] = i;
			ind++;
		}
		partialCa.reSample(new ColGroup(columns));	
		
	}

	private void MTMethodLastInt(LLLCA partialCa, List<Interaction> notCovered) {
		int len = notCovered.size();
		Interaction interaction = notCovered.get(len - 1);
		partialCa.reSample(interaction.getCols());		
	}

	private void uniformRandom(LLLCA partialCa) {
		int k = partialCa.getK();
		int[] cols = new int[k];
		for (int i = 0; i < k; i++) {
			cols[i] = i;
		}
		partialCa.reSample(new ColGroup(cols));		
	}
	
	protected abstract int partialArraySize(int t, int k, int v);
	protected abstract void cover(List<Interaction> notCovered);
	protected abstract void reset();
	protected abstract void secondStage(ListCAExt remCA);
}
