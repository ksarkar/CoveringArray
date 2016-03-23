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

package edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator2;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class TrieIG implements InteractionSet{
	
	private int t;
	private int k;
	private int v;
	Map<ColGroup, Trie> graph;
	int numInteraction;
	
	public TrieIG() {
		this.graph = new HashMap<ColGroup, Trie>();
	}

	public TrieIG(int t, int k, int v) {
		this();
		this.t = t;
		this.k = k;
		this.v = v;
		
		List<ColGroup> allColGroups = Helper.createAllColGroups(this.t, this.k);
		Trie symTrie = Trie.buildTrie(this.v, this.t);
		
		this.numInteraction = allColGroups.size() * symTrie.getCount();
		
		for (ColGroup col : allColGroups) {
			graph.put(col, Trie.copyTrie(symTrie));
		}	
	}



	@Override
	public boolean isEmpty() {
		return this.graph.isEmpty();
	}



	@Override
	public int deleteInteractions(Integer[] newRandRow, int[] covD) {
		int coverage = 0;
		
		Iterator<ColGroup>	colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			int[] indices = colGr.getCols();
			
			int[] syms = new int[this.t];
			for (int i = 0; i < this.t; i++) {
				syms[i] = newRandRow[indices[i]].intValue();
			}
			
			Trie trie = this.graph.get(colGr);
			coverage = coverage + trie.deleteInstance(syms);
			if (trie.getCount() == 0) {
				colGrIt.remove();
			}
		}
		
		this.numInteraction = this.numInteraction - coverage;
		return coverage;
	}



	@Override
	public ColGrIterator getColGrIterator() {
		return new ColGrIterator2(this.t, this.k);
	}



	@Override
	public boolean contains(Interaction interaction) {
		ColGroup colGr = interaction.getCols();
		int[] tuple = interaction.getSyms().getSyms();
		
		if (this.graph.containsKey(colGr)){
			Trie trie = this.graph.get(colGr);
			if (trie.containsInstance(tuple)){
				return true;
			}
		}
		return false;
	}



	@Override
	public Interaction selectInteraction(Integer[] row) {
		ColGroup cols = this.nextColGroup(row);
		List<int[]> symsList = this.getCandidates(cols);
		
		if (symsList == null) {
			return new Interaction(cols, new SymTuple(new int[]{0,0}));
		}
		
		double maxExpCoverage = 0.0d;
		int[] best = null;
		for (int[] syms : symsList) {
			double expCoverage = this.computeExpCov(row, syms);
			//System.out.println("sym: " + Arrays.toString(syms) + ", expCov: " + expCoverage);
			if (expCoverage > maxExpCoverage){
				maxExpCoverage = expCoverage;
				best = syms;
			}
		}
		return new Interaction(cols, new SymTuple(best));
	}

	protected List<int[]> getCandidates(ColGroup cols) {
		Trie t = this.graph.get(cols);
		return (t == null) ? null: t.getInstances(this.t);
	}

	private ColGroup nextColGroup(Integer[] row) {
		int sInd = 0;
		for (int i = 0; i < row.length; i++) {
			if (row[i] == this.v) {
				sInd = i;
				break;
			}
		}
		int[] cols = new int[this.t];
		for (int i = 0; i < t; i++) {
			cols[i] = sInd + i;
		}
		return new ColGroup(cols);
	}
	

	private double computeExpCov(Integer[] row, int[] syms) {
		int[] newRow = this.fillRow(row, syms);
		int[] symbs = new int[this.t];
		Set<Map.Entry<ColGroup, Trie>>	entries = this.graph.entrySet();
		double expCov = 0.0d;
		for (Map.Entry<ColGroup, Trie> entry : entries) {
			ColGroup cols = entry.getKey();
			Trie trie = entry.getValue();
			int numSymb = this.collectSyms(newRow, cols, symbs);
			int numInts = trie.getNumInstanceWPrefix(
					Arrays.copyOfRange(symbs, 0, numSymb));
			expCov = expCov + numInts / Math.pow(this.v, this.t - numSymb);
		}
		return expCov;
	}

	private int[] fillRow(Integer[] row, int[] syms) {
		int[] newRow = new int[row.length];
		for (int i = 0; i < row.length; i++) {
			newRow[i] = this.v;
		}
		int sInd = 0;
		for (int i = 0; i < newRow.length; i++) {
			if (row[i] == this.v) {
				sInd = i;
				break;
			} else {
				newRow[i] = row[i];
			}
		}
		for (int i = 0; i < this.t; i++) {
			newRow[sInd + i] = syms[i];
		}
		
		return newRow;
	}
	

	private int collectSyms(int[] newRow, ColGroup cols, int[] symbs) {
		int numSym = 0;
		int[] c = cols.getCols();
		for (int i = 0; i < c.length; i++) {
			symbs[i] = newRow[c[i]];
			if (newRow[c[i]] != this.v) {
				numSym++;
			}
		}
		return numSym;
	}

	@Override
	public int getComp() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void deleteFullyDeterminedInteractions(Integer[] newRow) {
		// Not needed
		
	}



	@Override
	public void deletIncompatibleInteractions(Interaction interaction) {
		// not needed
		
	}



	@Override
	public int getCoverage(Integer[] newRow, int[] covD) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getNumUncovInt(ColGroup colGr) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public double computeProbCoverage(int[] sCols, int[] entries) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getT() {
		return this.t;
	}
	
	public int getV() {
		return this.v;
	}
	
	@Override
	public String toString() {
		String s =  "InteractionGraph [";
		
		Set<Map.Entry<ColGroup, Trie>> set = this.graph.entrySet();
		for (Map.Entry<ColGroup, Trie> entry : set) {
			s = s + entry.getKey() + " -> " + this.printList(entry.getValue().getInstances(this.t)) + "\n";
		}
		
		return s + "]";
	}

	private String printList(List<int[]> instances) {
		String s = "[";
		for (int[] ins : instances) {
			s = s + Arrays.toString(ins) + ", ";
		}
		return s + "]";
	}

	public static InteractionSet makeCopy(InteractionSet ig) {
		assert(ig instanceof TrieIG);
		TrieIG igS = (TrieIG)ig;
		TrieIG igC = new TrieIG();
		igC.t = igS.t;
		igC.k = igS.k;
		igC.v = igS.v;
		igC.numInteraction = igS.numInteraction;
		Set<Map.Entry<ColGroup, Trie>> entries = igS.graph.entrySet();
		for (Map.Entry<ColGroup, Trie> entry : entries) {
			igC.graph.put(entry.getKey(), Trie.copyTrie(entry.getValue()));
		}
		
		return igC;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TrieIG ig = new TrieIG(2,3,2);
		//System.out.println(ig);
		ig.deleteInteractions(new Integer[]{0, 1, 0}, null);
		System.out.println(ig);
		
		//System.out.println(ig.contains(new Interaction(new ColGroup(new int[]{0,1}), new SymTuple(new int[]{1,1}))));
		
		System.out.println(ig.selectInteraction(new Integer[]{2,2,2}));
		
	}
	

}
