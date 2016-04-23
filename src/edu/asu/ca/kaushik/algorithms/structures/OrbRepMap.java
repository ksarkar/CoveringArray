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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrbRepMap implements OrbRepSet {
	private Group group;
	
	private int t;
	private int k;
	private int v;
	
	private Map<ColGroup, Set<SymTuple>> map; 
	

	public OrbRepMap(int t, int k, int v, int groupType) {
		switch(groupType) {
		case 0 : this.group = new Trivial(v);
				break;
		case 1 : this.group = new Cyclic(v);
				break;
		case 2 : this.group = new Frobenius(v);
				break;
		default :
				System.out.println("Invalid group type");
				System.exit(1);
		}
		
		this.t = t;
		this.k = k;
		this.v = v;
		
		ColGrIterator colIt = new ColGrLexIterator(t, k);
		List<SymTuple> symTuples = this.group.getAllOrbits(t);
		
		this.map = new HashMap<ColGroup, Set<SymTuple>>();
		
		colIt.rewind();
		while (colIt.hasNext()) {
			ColGroup cols = colIt.next();
			this.map.put(cols, new HashSet<SymTuple>(symTuples));
		}
		
	}

	public OrbRepMap(List<Interaction> orbits, Group g, int t, int k, int v) {
		this.group = g;
		
		this.t = t;
		this.k = k;
		this.v = v;
		
		this.map = new HashMap<ColGroup, Set<SymTuple>>();
		Iterator<Interaction> it = orbits.iterator();
		while (it.hasNext()) {
			Interaction i = it.next();
			ColGroup c = i.getCols();
			SymTuple s = i.getSyms();
			Set<SymTuple> set = new HashSet<SymTuple>();
			if (this.map.containsKey(c)) {
				set = this.map.remove(c);
			}
			set.add(s);
			this.map.put(c, set);
		}
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public int deleteOrbits(Integer[] newRandRow) {
		int coverage = 0;
		
		Iterator<ColGroup>	colGrIt = this.map.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			int[] indices = colGr.getCols();
			
			int[] syms = new int[this.t];
			for (int i = 0; i < this.t; i++) {
				syms[i] = newRandRow[indices[i]].intValue();
			}
			SymTuple tuple = this.group.getOrbit(new SymTuple(syms));
			
			Set<SymTuple> set = this.map.get(colGr); 
			set.remove(tuple);
			
			if (set.isEmpty()) {
				colGrIt.remove();
			}
		}
		
		return coverage;
	}

	@Override
	public boolean containsOrbit(Interaction interaction) {
		ColGroup colGr = interaction.getCols();
		SymTuple tuple = interaction.getSyms();
		
		if (this.map.containsKey(colGr)){
			Set<SymTuple> tuples = this.map.get(colGr);
			if (tuples.contains(tuple)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Iterator<ColGroup> getColGrIterator() {
		return this.map.keySet().iterator();
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
	public Group getGroup() {
		return this.group;
	}

	@Override
	public Set<SymTuple> getOrbits(ColGroup colGr) {
		return this.map.get(colGr);
	}

}
