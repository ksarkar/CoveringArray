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

package edu.asu.ca.kaushik.da.structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.da.scripts.Runner;

public class RowDA implements DAIface {
	private int d,t,k,v;
	private int[] V;
	private boolean isMixed;
	private List<int[]> array;
	
	private Random rand = new Random(1234L);

	public RowDA(int d, int t, int k, int v) {
		this.d = d;
		this.t = t;
		this.k = k;
		this.v = v;
		this.isMixed = false;
		
		this.array = new ArrayList<int[]>();		
	}
	
	public RowDA(int d, int t, int k, int[] v) {
		this.d = d;
		this.t = t;
		this.k = k;
		this.V = v;
		this.isMixed = true;
		
		this.array = new ArrayList<int[]>();
	}

	@Override
	public void addRow(int[] newRow) {
		if (newRow.length != this.k) {
			System.out.println("RowDA.addRow : lenght of the new row does not match the number of factors.");
			System.out.println("Number of factors: " + this.k + ", length of new row: " + newRow.length);
		}
		
		this.array.add(newRow);
	}
	
	@Override
	public int getD() {
		return this.d;
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
	public int[] getVArray() {
		return this.V;
	}
	
	@Override
	public int getNumRows() {
		return this.array.size();
	}
	
	@Override
	public boolean isMixedFlagUP() {
		return this.isMixed;
	}
	
	@Override
	public boolean isDetectingArray() {
		List<SymTuple> syms = new ArrayList<SymTuple>(Helper.createAllSymTuples(this.t, this.v));
		Set<dSymTuple> dSyms = Misc.getDSymTupSet(syms);
		ColGrIterator colIt = new ColGrLexIterator(this.t, this.k);
		dColGrIterator dColIt = new dColGrLexIterator(this.d, this.t, this.k);
		while(colIt.hasNext()) {
			ColGroup cols = colIt.next();
			dColIt.rewind();
			while (dColIt.hasNext()) {
				dColGroup dCols = dColIt.next();
				if (!this.isDetecting(cols, dCols, syms, dSyms)) {
					System.out.println("Problem : " + cols.toString() + ", " + dCols.toString());
					return false;
				}
			}
		}
		System.out.println("Valid detecting array.");
		return true;
	}

	public boolean isDetecting(ColGroup cols, dColGroup dCols, List<SymTuple> syms, Set<dSymTuple> dSyms) {
		Map<SymTuple, Set<dSymTuple>> map = Misc.getSymTupleMap(syms, dSyms, dCols.contains(cols), new int[]{0});
		for (int[] r : this.array) {
			SymTuple s = Misc.getSyms(cols, r);
			dSymTuple ds = Misc.getdSyms(dCols, r);
			Set<dSymTuple> set = map.get(s);
			if (set != null) {
				Iterator<dSymTuple> dIt = set.iterator();
				while(dIt.hasNext()) {
					dSymTuple ds2 = dIt.next();
					if (!ds.equals(ds2)) {
						dIt.remove();
					}
				}
				if (set.isEmpty()) {
					map.remove(s);
				}
			}
			if (map.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isMixedDetectingArray() {
		if (!isMixed) {
			return false;
		}
		
		ColGrIterator colIt = new ColGrLexIterator(this.t, this.k);
		dColGrIterator dColIt = new dColGrLexIterator(this.d, this.t, this.k);
		while(colIt.hasNext()) {
			ColGroup cols = colIt.next();
			dColIt.rewind();
			while (dColIt.hasNext()) {
				dColGroup dCols = dColIt.next();
				if (!this.isMixedDetecting(cols, dCols)) {
					System.out.println("Problem : " + cols.toString() + ", " + dCols.toString());
					return false;
				}
			}
		}
		System.out.println("Valid mixed detecting array.");
		return true;
	}
	
	@Override
	public boolean isMixedDetecting(ColGroup cols, dColGroup dCols) {
		List<SymTuple> syms = Misc.getSymTuples(cols, this.V);
		Set<dSymTuple> dSyms = Misc.getDSymTupleSet(dCols, this.V);
		
		return this.isDetecting(cols, dCols, syms, dSyms);
	}
	
	@Override
	public long countCovered(int[] row) {	
		ColGrIterator colIt = new ColGrLexIterator(t, k);
		dColGrIterator dColIt = new dColGrLexIterator(d, t, k);
		int cov = 0;
		while(colIt.hasNext()) {
			ColGroup cols = colIt.next();
			dColIt.rewind();
			while (dColIt.hasNext()) {
				dColGroup dCols = dColIt.next();
				SymTuple s = Misc.getSyms(cols, row);
				dSymTuple ds = Misc.getdSyms(dCols, row);
				cov = cov + this.countCovered(cols, dCols, s , ds);
			}
		}
		return cov;
	}
	
	@Override
	public int countCovered(ColGroup cols, dColGroup dCols, SymTuple syms,
			dSymTuple dsyms) {
		
		List<SymTuple> symList = this.isMixed ? Misc.getSymTuples(cols, this.V) :
			new ArrayList<SymTuple>(Helper.createAllSymTuples(this.t, this.v));
		Set<dSymTuple> dSymSet = this.isMixed ? Misc.getDSymTupleSet(dCols, this.V) : Misc.getDSymTupSet(symList);
		
		Map<SymTuple, Set<dSymTuple>> map = Misc.getSymTupleMap(symList, dSymSet, dCols.contains(cols), new int[]{0});
		
		for (int[] r : this.array) {
			SymTuple s = Misc.getSyms(cols, r);
			dSymTuple ds = Misc.getdSyms(dCols, r);
			Set<dSymTuple> set = map.get(s);
			if (set != null) {
				Iterator<dSymTuple> dIt = set.iterator();
				while(dIt.hasNext()) {
					dSymTuple ds2 = dIt.next();
					if (!ds.equals(ds2)) {
						dIt.remove();
					}
				}
				if (set.isEmpty()) {
					map.remove(s);
				}
			}		
			if (map.isEmpty()) {
				return 0;		// no more witness needed
			}
		}
		
		Set<dSymTuple> set = map.get(syms);
		if (set == null) {
			return 0;
		} else {
			int count = 0;
			Iterator<dSymTuple> dIt = set.iterator();
			while(dIt.hasNext()) {
				dSymTuple ds2 = dIt.next();
				if (!dsyms.equals(ds2)) {
					count++;
				}
			}
			return count;
		}
		
	}
	
	@Override
	public Map<SymTuple, Set<dSymTuple>> getSymTupMap(ColGroup cols,
			dColGroup dCols) {
		
		List<SymTuple> symList = this.isMixed ? Misc.getSymTuples(cols, this.V) :
			new ArrayList<SymTuple>(Helper.createAllSymTuples(this.t, this.v));
		Set<dSymTuple> dSymSet = this.isMixed ? Misc.getDSymTupleSet(dCols, this.V) : Misc.getDSymTupSet(symList);
		
		Map<SymTuple, Set<dSymTuple>> map = Misc.getSymTupleMap(symList, dSymSet, dCols.contains(cols), new int[]{0});
		
		for (int[] r : this.array) {
			SymTuple s = Misc.getSyms(cols, r);
			dSymTuple ds = Misc.getdSyms(dCols, r);
			Set<dSymTuple> set = map.get(s);
			if (set != null) {
				Iterator<dSymTuple> dIt = set.iterator();
				while(dIt.hasNext()) {
					dSymTuple ds2 = dIt.next();
					if (!ds.equals(ds2)) {
						dIt.remove();
					}
				}
				if (set.isEmpty()) {
					map.remove(s);
				}
			}		
		}
		return map;
	}
	
	
	@Override
	public void reSampleCols(Set<Integer> s) {
		Iterator<int[]> it = this.array.iterator();
		while(it.hasNext()) {
			int[] r = it.next();
			for (int i : s){
				r[i] = this.isMixed ? this.rand.nextInt(this.V[i]) : this.rand.nextInt(this.v);
			}
		}
		
	}

	@Override
	public void addRandomRows(int n) {
		for (int i = 0; i < n; i++) {
			this.addRow(this.randomRow());
		}		
	}
	
	private int[] randomRow() {
		int[] row = new int[this.k];
		for (int i = 0; i < this.k; i++) {
			row[i] = this.isMixed ? this.rand.nextInt(this.V[i]) : this.rand.nextInt(this.v);
		}
		return row;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("RowDA\n");
		for (int[] r : this.array) {
			s.append(Arrays.toString(r) + "\n");
		}
		return s.toString();
	}
	
	@Override
	public void writeToFile(String fName) throws IOException {
		StringBuilder header = new StringBuilder();
		header.append("(" + this.d + "," + this.t + ")-" + (this.isMixed ? "MDA" : "DA") + "(" + this.getNumRows() + ";" 
				+ this.k + ",");
		if (this.isMixed) {
			header.append("{" + this.V[0]);
			for (int i = 1; i < this.k; i++) {
				header.append("," + this.V[i]);
			}	
			header.append("}");
		} else {
			header.append(this.v);
		}
		header.append(")");
		Runner.writeLine(fName, header.toString());
		
		for (int[] r : this.array) {
			StringBuilder row = new StringBuilder();
			for (int a : r) {
				row.append(a + ",");
			}
			Runner.writeLine(fName, row.toString());
		}
		
	}

	
	public static void main(String[] args) {
		RowDA da = new RowDA(1,2,3,2);
		
		da.addRow(new int[]{0,0,1});
		da.addRow(new int[]{1,1,0});
		da.addRow(new int[]{1,0,0});
		da.addRow(new int[]{0,1,1});
		da.addRow(new int[]{0,1,0});
		
		System.out.println(da.isDetectingArray());

	}

}
