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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayCA implements CA {
	
	protected int t;
	protected int k;
	protected int v;
	protected int N;
	
	private List<SymTuple> symTups;
	private Map<SymTuple,Boolean> covered;
	
	protected int[][] ca;
	
	public ArrayCA(int t, int k, int v, int N) {			
		this.t = t;
		this.k = k;
		this.v = v;
		
		this.N = N;
		
		ca = new int[this.N][this.k];
		
		this.symTups = Helper.createAllSymTuples(this.t, this.v);
		this.covered = new HashMap<SymTuple,Boolean>();
		for (SymTuple syms : symTups) {
			covered.put(syms, false);
		}
	}
	
	public ArrayCA(CA c) {		
		this.t = c.getT();
		this.k = c.getK();
		this.v = c.getV();
		
		this.N = c.getNumRows();
		
		this.ca = new int[this.N][this.k];
		
		int i = 0;
		Iterator<Integer[]> caIt = c.iterator();
		while (caIt.hasNext()) {
			Integer[] row = caIt.next();
			for (int j = 0; j < k; j++) {
				this.ca[i][j] = row[j];
			}
			i++;
		}
		
		this.symTups = Helper.createAllSymTuples(this.t, this.v);
		this.covered = new HashMap<SymTuple,Boolean>();
		for (SymTuple syms : symTups) {
			covered.put(syms, false);
		}
	}
	
	public void setCA(int i, int j, int a) {
		assert((i < this.N) && (j < this.k) && (a < this.v));
		this.ca[i][j] = a;		
	}
	
	public int getCA(int i, int j) {
		assert((i < this.N) && (j < this.k));
		return this.ca[i][j];
	}

	@Override
	public int getT() {
		return this.t;
	}

	@Override
	public int getK() {
		return this.k;
	}

	@Override
	public int getV() {
		return this.v;
	}

	@Override
	public int getNumRows() {
		return this.N;
	}

	@Override
	public Iterator<Integer[]> iterator() {
		return new Iterator<Integer[]>() {
			
			int ind = 0;

			@Override
			public boolean hasNext() {
				return (ind < N) ? true : false;
			}

			@Override
			public Integer[] next() {
				Integer[] row = new Integer[k];
				for (int i = 0; i < k; i++){
					row[i] = ca[ind][i];
				}
				ind++;
				return row;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

	@Override
	public Instrumentation getInstrumentation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isCovered(Interaction interaction) {
		int[] cols = interaction.getCols().getCols();
		int[] symbs = interaction.getSyms().getSyms();
		assert((cols.length == this.t) && (symbs.length == this.t));
		boolean found = false;
		boolean isThisRow;
		for (int i = 0; i < this.N; i++) {
			isThisRow = true;
			for (int j = 0; j < this.t; j++) {
				if (ca[i][cols[j]] != symbs[j]) {
					isThisRow = false;
					break;
				}
			}
			if (isThisRow) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public void resetCovered() {
		for (SymTuple syms : symTups) {
			covered.put(syms, false);
		}
	}
	
	public boolean isAllCovered(ColGroup colGr) {
		int[] cols = colGr.getCols();
		assert(cols.length == this.t);
		int[] syms = new int[this.t];
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.t; j++) {
				syms[j] = ca[i][cols[j]];
			}
			SymTuple symTup = new SymTuple(syms);
			this.covered.put(symTup, true);
		}
		
		Collection<Boolean> values = this.covered.values();
		for (Boolean value : values) {
			if (value.equals(Boolean.FALSE)) {
				return false;
			}
		}
		return true;
	}
	
	public List<Interaction> notCovered(ColGroup colGr) {
		int[] cols = colGr.getCols();
		assert(cols.length == this.t);
		int[] syms = new int[this.t];
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.t; j++) {
				syms[j] = ca[i][cols[j]];
			}
			SymTuple symTup = new SymTuple(syms);
			this.covered.put(symTup, true);
		}
		
		List<Interaction> notCovered = new ArrayList<Interaction>();
		Set<Map.Entry<SymTuple,Boolean>> entries = this.covered.entrySet();
		for (Map.Entry<SymTuple,Boolean> entry : entries) {
			if (entry.getValue().equals(Boolean.FALSE)) {
				notCovered.add(new Interaction(colGr, entry.getKey()));
			}
		}
		
		return notCovered;
	}
	
	/**
	 * Checks if this array is a covering array
	 * @param cols set to the column group that has the first 
	 * 				uncovered interaction 
	 * @return true if it is a covering array, false otherwise
	 */
	public boolean isCompleteCA(ColGroup cols) {
		ColGrIterator clGrIt = new ColGrIterator(this.t, this.k);
		int colGrNum = 0;
		
		clGrIt.rewind();
		while(clGrIt.hasNext()){
			ColGroup c = clGrIt.next();
			this.resetCovered();
			if (!this.isAllCovered(c)) {
				cols = c;
				System.out.println("uncovered interaction in coloumn group: " + colGrNum);
				return false;
			}
			colGrNum++;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		String s =  "CA(" + this.getNumRows() + ";" + this.t + ","
				+ this.k + "," + this.v +")\n";
		for (int i = 0; i < this.getNumRows(); i++) {
			s = s + Arrays.toString(this.ca[i]) + "\n";
		}
		return s ;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[][] a = {{1, 2, 3}, {4, 5, 6}};
		for (int i = 0 ; i < 2; i++) {
			System.out.print(Arrays.toString(a[i]));
		}

	}

}
