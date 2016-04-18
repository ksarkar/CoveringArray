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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GroupLLLCA implements CA {
	
	protected int t;
	protected int k;
	protected int v;
	protected int N;
	
	private List<SymTuple> symTups;
	private Map<SymTuple,Boolean> covered;
	
	private Random random;
	private Group g;
	
	protected int[][] ca;

	public GroupLLLCA(int t, int k, int v, int n, int gType, Random random) {
		this.t = t;
		this.k = k;
		this.v = v;
		this.N = n;
		
		switch(gType) {
		case 0 : this.g = new Trivial(v);
				break;
		case 1 : this.g = new Cyclic(v);
				break;
		case 2 : this.g = new Frobenius(v);
				break;
		default :
				System.out.println("Invalid group type");
				System.exit(1);
		}
		
		this.random = random;
		
		ca = new int[this.N][this.k];
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.k; j++) {
				this.ca[i][j] = this.random.nextInt(this.v);
			}
		}
		
		this.symTups = this.g.getAllOrbits(this.t);
		this.covered = new HashMap<SymTuple,Boolean>();
		for (SymTuple syms : symTups) {
			covered.put(syms, false);
		}
	}
	
	public void resetCovered() {
		for (SymTuple syms : symTups) {
			covered.put(syms, false);
		}		
	}

	/**
	 * 
	 * @param colGr
	 * @return List of orbits not covered on the column t-set colGr
	 */
	public List<Interaction> notCovered(ColGroup colGr) {
		int[] cols = colGr.getCols();
		assert(cols.length == this.t);
		int[] syms = new int[this.t];
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.t; j++) {
				syms[j] = ca[i][cols[j]];
			}
			SymTuple symTup = new SymTuple(syms);
			SymTuple symT = this.g.getOrbit(symTup);
			this.covered.put(symT, true);
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

	public void reSample(ColGroup colGroup) {
		int[] cols = colGroup.getCols();
		for (int c : cols) {
			this.sample(c);
		}		
	}
	
	private void sample(int col) {
		for (int i = 0; i < this.N; i++) {
			this.ca[i][col] = this.random.nextInt(this.v);
		}
	}

	@Override
	public int getT() {
		return t;
	}

	@Override
	public int getK() {
		return k;
	}

	@Override
	public int getV() {
		return v;
	}

	@Override
	public int getNumRows() {
		return this.N;
	}
	
	public Group getGroup() {
		return this.g;
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
				assert false;
			}
			
		};
	}

	@Override
	public Instrumentation getInstrumentation() {
		assert false;
		return null;
	}

}
