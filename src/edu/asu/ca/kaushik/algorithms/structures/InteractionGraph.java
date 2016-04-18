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

package edu.asu.ca.kaushik.algorithms.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.structures.Helper;


public class InteractionGraph implements InteractionSet {
	private int t;
	private int k;
	private int v;
	protected Map<ColGroup, Set<SymTuple>> graph; 
	private long numInt;
	
	private long numUncovInt; // Number of uncovered interactions
	private int[] uncovDist; // Number of uncovered interaction involving each column
	// Number of uncovered interaction involving a column and a symbol
	private int[][] uncovDSym; 
	// probability of choosing symbol i in column j
	private double[][] prob;
	
	private Random rand;
	
	// time complexity measurement
	private int comp;	
	
	public InteractionGraph(InteractionGraph ig) {
		this.t = ig.t;
		this.k = ig.k;
		this.v = ig.v;
		this.rand = ig.rand;
		this.numInt = ig.numInt;
		this.numUncovInt = ig.numUncovInt;
		this.uncovDist = ig.uncovDist;
		this.uncovDSym = ig.uncovDSym;
		this.prob = ig.prob;
		// Here we are making a separate copy of the hashmap structure.
		// The copy is not very deep. The copied hashmap contains independent 
		// copies for the key and the value, but the values (sets) are not deeply
		// copied. The same sets are shared between the original and the copied 
		// hashmap. (Be careful when modifying the set of symTuples).
		this.graph = new HashMap<ColGroup, Set<SymTuple>>(ig.graph);
	}

	public InteractionGraph(int t, int k, int v){
		this.t = t;
		this.k = k;
		this.v = v;
		
		this.uncovDist = new int[this.k];
		this.uncovDSym = new int[this.v][this.k];
		this.prob = new double[this.v][this.k];
		
		this.graph = createFullGraph(t, k, v);
		this.rand = new Random(1234L);
	}	

	private Map<ColGroup, Set<SymTuple>> createFullGraph(int t, int k, int v) {
		ColGrIterator colIt = new ColGrLexIterator(t, k);
		List<SymTuple> symTuples = Helper.createAllSymTuples(t, v);
		
		this.numInt = (long)CombinatoricsUtils.binomialCoefficientDouble(k, t)
				* symTuples.size();
		this.numUncovInt = this.numInt;
		int uncovIntCol = (int)(this.numInt * this.t / this.k);
		int uncovIntColSym = uncovIntCol / this.v;
		for (int j = 0; j < this.k; j++){
			this.uncovDist[j] = uncovIntCol;
			
			for (int i = 0; i < v; i++) {
				this.uncovDSym[i][j] = uncovIntColSym; 
				this.prob[i][j] = 1.0d / this.v;
			}
		}
		
		Map<ColGroup, Set<SymTuple>> graph = new HashMap<ColGroup, Set<SymTuple>>();
		
		colIt.rewind();
		while (colIt.hasNext()) {
			ColGroup cols = colIt.next();
			graph.put(cols, new HashSet<SymTuple>(symTuples));
		}
		
		return graph;
	}
	
	public InteractionGraph(int t, int k, int v, List<Interaction> uncovInts) {
		this.t = t;
		this.k = k;
		this.v = v;
		
		this.rand = new Random(1234L);
		this.numUncovInt = this.numInt = uncovInts.size();
		
		this.graph = new HashMap<ColGroup, Set<SymTuple>>();
		Iterator<Interaction> it = uncovInts.iterator();
		while (it.hasNext()) {
			Interaction i = it.next();
			ColGroup c = i.getCols();
			SymTuple s = i.getSyms();
			Set<SymTuple> set = new HashSet<SymTuple>();
			if (this.graph.containsKey(c)) {
				set = this.graph.remove(c);
			}
			set.add(s);
			this.graph.put(c, set);
		}
		
		this.uncovDist = new int[this.k];
		this.uncovDSym = new int[this.v][this.k];
		this.prob = new double[this.v][this.k];
	}
	
	@Override
	public Iterator<ColGroup> getColGrIterator(){
		return this.graph.keySet().iterator();
	}
	
	@Override
	public int getComp() {
		return this.comp;
	}
	
	public long getNumInt() {
		return this.numInt;
	}
	
	public void setRand(Random rand) {
		this.rand = rand;		
	}

	@Override
	public boolean isEmpty() {
		return this.graph.isEmpty();
	}

	@Override
	public int deleteInteractions(Integer[] newRandRow, int[] covDist) {
		// time complexity measurement
		int coverage = 0;
		int[] covD = new int[this.k];
		
		Iterator<ColGroup>	colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			int[] indices = colGr.getCols();
			
			int[] syms = new int[this.t];
			for (int i = 0; i < this.t; i++) {
				syms[i] = newRandRow[indices[i]].intValue();
			}
			SymTuple tuple = new SymTuple(syms);
			
			Set<SymTuple> set = this.graph.get(colGr);
			if (set.contains(tuple)) {
				// time complexity measurement
				coverage += 1;
				
				
				for (int i = 0; i < this.t; i++){
					covD[indices[i]] += 1;
					this.uncovDSym[syms[i]][indices[i]] -= 1;
				}
				
				set.remove(tuple);
				if (set.isEmpty()) {
					colGrIt.remove();
				}
			}
		}
		
		this.numUncovInt = this.numUncovInt - coverage;
		for (int j = 0; j < this.k; j++) {
			this.uncovDist[j] = this.uncovDist[j] - covD[j];
			
			for (int i = 0; i < this.v; i++) {
				this.prob[i][j] = (this.uncovDist[j] == 0) ? 0.0d : (this.uncovDSym[i][j] * 1.0d / this.uncovDist[j]);
			}
		}
		
		if (covDist != null) {
			assert (covDist.length == this.k);
			for (int i = 0; i < this.k; i++) {
				covDist[i] = covD[i];
			}
		}
		
		return coverage;
	}
	
	/**
	 * Given a row computes coverage
	 * @param newRandRow - the row for which coverage is computed <input>
	 * @param covD - updates with number of interactions for 
	 * each column <output>
	 * @return Number of newly covered interactions
	 */
	@Override
	public int getCoverage(Integer[] newRandRow, int[] covD) {	
		int coverage = 0;
		
		if (covD != null) {
			assert (covD.length == this.k);
			for (int i = 0; i < this.k; i++) {
				covD[i] = 0;
			}
		}
		
		Iterator<ColGroup>	colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			int[] indices = colGr.getCols();
			
			int[] syms = new int[this.t];
			for (int i = 0; i < this.t; i++) {
				syms[i] = newRandRow[indices[i]].intValue();
			}
			SymTuple tuple = new SymTuple(syms);
			
			Set<SymTuple> set = this.graph.get(colGr);
			if (set.contains(tuple)) {
				coverage += 1;
				
				if (covD != null) {
					for (int i = 0; i < this.t; i++){
						covD[indices[i]] += 1;
					}
				}
			}
		}
		
		return coverage;
	}
	
	public Interaction getUniformRandInteraction() {
		assert !this.isEmpty();
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>> set = this.graph.entrySet();

		int M = 0;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			Set<SymTuple> tupleSet = entry.getValue();
			M = M + tupleSet.size();
		}
		
		int chosen = this.rand.nextInt(M);
		
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			ColGroup cols = entry.getKey();
			Set<SymTuple> tupleSet = entry.getValue();
			for (SymTuple tuple : tupleSet){
				if (chosen == 0){
					return new Interaction(cols, tuple);
				}
				else {
					chosen = chosen - 1;
				}
			}
		}
		
		assert false; // control will never reach this statement
		return null;
	}
	
	public Interaction getPropRandInteraction(Integer[] newRow) {
		assert !this.isEmpty();
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>> set = this.graph.entrySet();
		
		List<Integer> N = new ArrayList<Integer>();
		
		int M = 0;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			ColGroup cols = entry.getKey();
			int n = this.getNValue(cols, newRow);
			Set<SymTuple> tupleSet = entry.getValue();
			int numEl = tupleSet.size();
			M = M + (numEl * n);
			for(int i = 0; i < numEl; i++) {
				N.add(new Integer(n));
			}
		}
		
		int randInt = this.rand.nextInt(M);
		
		int i = 0;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			ColGroup cols = entry.getKey();
			Set<SymTuple> tupleSet = entry.getValue();
			for (SymTuple tuple : tupleSet){
				if (randInt < N.get(i).intValue()){
					return new Interaction(cols, tuple);
				}
				else {
					randInt = randInt - N.get(i).intValue();
					i++;
				}
			}
		}
		
		assert false; // control will never reach this statement
		return null;
	}
	
	
	
	private int getNValue(ColGroup cols, Integer[] newRow) {
		int[] row = new int[newRow.length];
		for (int i = 0; i < newRow.length; i++){
			row[i] = newRow[i].intValue();
		}
		
		int[] columns = cols.getCols();
		for (int i : columns){
			row[i] = 0;
		}
		
		int n = 0;
		for (int i : row){
			if (i == this.v){
				n++;
			}
		}
		return (int)Math.pow(this.v, n);
	}
	
	@Override
	public void deletIncompatibleInteractions(Interaction interaction) {
		ColGroup fixColGr = interaction.getCols();	
		Set<ColGroup> colGrSet = this.graph.keySet();
		Set<ColGroup> intersectColGrs = new HashSet<ColGroup>();
		for (ColGroup colGr : colGrSet){
			if (fixColGr.intersects(colGr)){
				intersectColGrs.add(colGr);
			}
		}
		
		for (ColGroup colGr : intersectColGrs){
			this.delIncompatibleInteractions(colGr, interaction);
		}
	}

	private void delIncompatibleInteractions(ColGroup colGr, Interaction interaction) {
		
		int[] intersectSyms = colGr.getIntersectCols(interaction, this.v);
		
		Set<SymTuple> set = this.graph.remove(colGr);
		Set<SymTuple> newVal = new HashSet<SymTuple>();
		for (SymTuple tuple : set) {
			int[] syms = tuple.getSyms();
			if (!this.incompatible(intersectSyms, syms)){
				newVal.add(tuple);
			}
		}
		
		if (!newVal.isEmpty()){
			this.graph.put(colGr, newVal);
		}
	}

	private boolean incompatible(int[] intersectSyms, int[] syms) {
		for (int i = 0; i < intersectSyms.length; i++){
			if (intersectSyms[i] != this.v){
				if (intersectSyms[i] != syms[i]){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void deleteFullyDeterminedInteractions(Integer[] newRow) {
		Iterator<ColGroup> colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()){
			if (colGrIt.next().isFullyDetermined(newRow, this.v)){
				colGrIt.remove();
			}
		}
	}
	
	@Override
	public boolean contains(Interaction interaction) {
		ColGroup colGr = interaction.getCols();
		SymTuple tuple = interaction.getSyms();
		
		if (this.graph.containsKey(colGr)){
			Set<SymTuple> tuples = this.graph.get(colGr);
			if (tuples.contains(tuple)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Interaction selectInteraction(Integer[] row) {
		// time complexity measurement
		this.comp = 0;
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>>	entries = this.graph.entrySet();
		
		double maxExpCoverage = 0.0d;
		Interaction bestInteraction = null;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : entries){
			ColGroup colGr = entry.getKey();
			Set<SymTuple> tuples = entry.getValue();
			for (SymTuple tuple : tuples) {
				// time complexity measurement
				this.comp += 1;
				
				Interaction interaction = new Interaction(colGr, tuple);
				double expCoverage = this.computeInteractionCoverage(interaction, row);
				if (expCoverage > maxExpCoverage){
					maxExpCoverage = expCoverage;
					bestInteraction = interaction;
					
				}
			}
		}
		
		// time complexity measurement
		this.comp = this.comp * this.comp;
		
		return bestInteraction;
	}

	protected double computeInteractionCoverage(Interaction interaction, Integer[] row) {
		InteractionGraph igCopy = new InteractionGraph(this);
		igCopy.deletIncompatibleInteractions(interaction);
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>>	entries = igCopy.graph.entrySet();
		
		double expCoverage = 0.0d;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : entries){
			ColGroup colGr = entry.getKey();
			Set<SymTuple> tuples = entry.getValue();
			expCoverage = expCoverage + tuples.size() 
					* calculateNumCoveringRows(row, colGr, interaction.getCols(), igCopy.v);
		}
		
		expCoverage = expCoverage 
				/ calculateNumCoveringRows(row, interaction.getCols(), interaction.getCols(), igCopy.v);
		
		return expCoverage;
	}

	private static double calculateNumCoveringRows(Integer[] row, ColGroup colGr1,
			ColGroup colGr2, int v) {
		Integer[] rowCopy = Arrays.copyOf(row, row.length);
		rowCopy = markCols(rowCopy, colGr1);
		rowCopy = markCols(rowCopy, colGr2);
		
		double exp = 0;
		for (Integer i : rowCopy){
			if (i.intValue() == v){
				exp = exp + 1.0d;
			}
		}
		
		return Math.pow(v, exp);
	}

	private static Integer[] markCols(Integer[] rowCopy, ColGroup colGr) {
		int[] cols = colGr.getCols();
		for (int i : cols){
			rowCopy[i] = new Integer(0);
		}
		return rowCopy;
	}

	@Override
	public String toString() {
		String s =  "InteractionGraph [";
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>> set = this.graph.entrySet();
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set) {
			s = s + entry.getKey() + " -> " + entry.getValue() + "\n                  ";
		}
		
		return s + "]";
	}
	
	public void deleteCoveredInteractions(CA partialCA) {
		Iterator<Integer[]> caIt = partialCA.iterator();
		while(caIt.hasNext()) {
			this.deleteInteractions(caIt.next(), null);
		}
	}
	
	@Override
	public double computeProbCoverage(int[] starredCols, int[] entries) {
		assert((starredCols.length == entries.length) && (entries.length <= this.t));
		
		int numCols = starredCols.length;
		double prob = 1.0d;
		for (int i = 0; i < numCols; i++){
			prob = prob * this.prob[entries[i]][starredCols[i]];
		}
		
		return prob;
	}

	@Override
	public int getNumUncovInt(ColGroup colGr) {
		int numInts = 0;
		if (this.graph.containsKey(colGr)) {
			numInts = this.graph.get(colGr).size();
		}
		return numInts;
	}
	
	public InteractionGraph sampledIG(long cutOff) {
		InteractionGraph sampledIg = new InteractionGraph(this);
		
		sampledIg.uncovDist = null;
		sampledIg.uncovDSym = null;
		sampledIg.prob = null;
		sampledIg.numInt = 0;
		
		Map<ColGroup, Set<SymTuple>> newGraph = new HashMap<ColGroup, Set<SymTuple>>();
		Iterator<ColGroup>	colGrIt = sampledIg.graph.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			Set<SymTuple> set = sampledIg.graph.get(colGr);
			Set<SymTuple> newSet = new HashSet<SymTuple>();
			Iterator<SymTuple> symIt = set.iterator();
			while (symIt.hasNext()) {
				SymTuple symT = symIt.next();
				if (!this.shouldDelete(cutOff)) {
					newSet.add(symT);
					sampledIg.numInt +=1;
				}
			}
			if (!newSet.isEmpty()) {
				newGraph.put(colGr, newSet);
			}
		}
		sampledIg.graph = newGraph;
		sampledIg.numUncovInt = sampledIg.numInt;
		return sampledIg;
	}

	private boolean shouldDelete(long cutOff) {
		double prob = 1.0d * cutOff / this.numInt;
		return (this.rand.nextDouble() > prob);
	}
	
	public int deleteInteractions(Integer[] newRandRow,
			InteractionGraph sampledIg, Object object) {
		int coverage = 0;
		int coverageSIG = 0;
		
		Iterator<ColGroup>	colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			int[] indices = colGr.getCols();
			
			int[] syms = new int[this.t];
			for (int i = 0; i < this.t; i++) {
				syms[i] = newRandRow[indices[i]].intValue();
			}
			SymTuple tuple = new SymTuple(syms);
			
			Set<SymTuple> set = this.graph.get(colGr);
			if (set.contains(tuple)) {
				coverage += 1;
				set.remove(tuple);
				if (set.isEmpty()) {
					colGrIt.remove();
				}
				
				if (sampledIg.graph.containsKey(colGr)) {
					Set<SymTuple> symSet = sampledIg.graph.get(colGr);
					if (symSet.contains(tuple)) {
						symSet.remove(tuple);
						coverageSIG += 1;
						if (symSet.isEmpty()) {
							sampledIg.graph.remove(colGr);
						}
					}
				}			
			}
		}
		
		this.numUncovInt = this.numUncovInt - coverage;
		sampledIg.numUncovInt = sampledIg.numUncovInt - coverageSIG;
		
		return coverage;
	}
	
	@Override
	public List<Interaction> getInteractions() {
		// this method should not be called
		assert false;
		return null;
	}

	public static void main(String[] args){
		/*InteractionGraph ig = new InteractionGraph(2,3,2);
		System.out.println(ig);
		int[] row = {0, 2, 2};
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row1 = {0, 0, 0};
		ig.deleteInteractions(newRow(row1));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row2 = {1, 0, 1};
		ig.deleteInteractions(newRow(row2));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row3 = {0, 1, 1};
		ig.deleteInteractions(newRow(row3));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row4 = {1, 1, 0};
		ig.deleteInteractions(newRow(row4));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		System.out.println(ig.isEmpty());
		*/
		
		
		//List<SymTuple> test = ig.createAllSymTuples(3, 2);
		//System.out.print(test.toString());
		
		//List<ColGroup> test1 = ig.createAllColGroups(2, 4);
		//System.out.println(test1);
		
		InteractionGraph ig = new InteractionGraph(2,3,2);
		System.out.println(ig);
		InteractionGraph igS = ig.sampledIG(4);
		System.out.println(igS);
		System.out.println(ig);
		
		int[] row1 = {0, 1, 1};
		ig.deleteInteractions(newRow(row1), igS, null);
		System.out.println(ig);
		System.out.println(igS);
		
	}
	
	@Override
	public int getT() {
		return t;
	}

	public int getK() {
		return k;
	}

	public int getV() {
		return v;
	}

	public long getNumUncovInt() {
		return numUncovInt;
	}
	
	public int[] getUncovDist() {
		return uncovDist;
	}

	private static Integer[] newRow(int[] row) {
		Integer[] a = new Integer[row.length];
		for (int i = 0; i < row.length; i++) {
			a[i] = new Integer(row[i]);
		}
		return a;
	}

}
