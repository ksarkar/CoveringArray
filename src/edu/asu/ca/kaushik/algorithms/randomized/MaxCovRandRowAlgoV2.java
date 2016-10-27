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

package edu.asu.ca.kaushik.algorithms.randomized;

import java.io.IOException;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class MaxCovRandRowAlgoV2 implements CAGenAlgo {
	private Random rand = new Random(123456L);
	private int numSamples = 100;

	@Override
	public CA generateCA(int t, int k, int v) {
		ListCA ca = new ListCA(t,k,v);
		
		while(!ca.isCompleteCA()) {
			int coverage = 0;
			Integer[] bestRow = null;
			for (int i = 0; i < this.numSamples; i++) {
				Integer[] row = Helper.constructRandRow(k,v, this.rand);
				int c = this.countNewCov(row, ca);
				if (c >= coverage) {
					coverage = c;
					bestRow = row;
				}
			}
			ca.addRow(bestRow);
		}
		return ca;
	}
	
	private int countNewCov(Integer[] row, ListCA ca) {
		int t = ca.getT();
		int k = ca.getK();
		ColGrIterator caIt = new ColGrLexIterator(t,k);
		int cov = 0;
		while(caIt.hasNext()) {
			ColGroup c = caIt.next();
			int[] indices = c.getCols();
			
			int[] syms = new int[t];
			for (int i = 0; i < t; i++) {
				syms[i] = row[indices[i]].intValue();
			}
			SymTuple tuple = new SymTuple(syms);
			Interaction inter = new Interaction(c,tuple);
			if (!ca.isCovered(inter)) {
				cov++;
			}
		}
		return cov;
	}

	@Override
	public void setNumSamples(int numSamples) {
		// TODO Auto-generated method stub

	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		MaxCovRandRowAlgoV2 algo = new MaxCovRandRowAlgoV2();
		CA ca = algo.generateCA(4, 30, 2);
		System.out.println(ca);
		System.out.println(ca.getNumRows());
		ArrayCA testCA = new ArrayCA(ca);
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println(testCA.isCompleteCA(cols));
	}

}
