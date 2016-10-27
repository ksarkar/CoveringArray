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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class CovTab implements CovTabIface {
	private int d, t, k, v;
	private int[] V;
	private boolean isMixed;
	private List<ColGroup> colGrs;
	private List<dColGroup> dColGrs;
	private List<SymTuple> symTups;
	private Set<dSymTuple> dSymTups;
	
	Map<ColGroup, Map<dColGroup, Map<SymTuple, Set<dSymTuple>>>> map;
	
	private long entryCount = 0;
	
	public CovTab(int d, int t, int k, int v) {
		this.d = d;
		this.t = t;
		this.k = k;
		this.v = v;
		
		if (this.d != 1) {
			System.err.println("Sorry! does not work for any case other than d=1.");
			System.exit(1);
		}
		
		this.colGrs = Misc.getColGrs(this.t, this.k);
		this.dColGrs = Misc.getdColGrs(this.colGrs);
		this.symTups = new ArrayList<SymTuple>(Helper.createAllSymTuples(this.t, this.v)); 
		this.dSymTups = Misc.getDSymTupSet(this.symTups);
		
		this.map = new HashMap<ColGroup, Map<dColGroup, Map<SymTuple, Set<dSymTuple>>>>();
		
		int[] entries = new int[]{0};
		Iterator<ColGroup> colIt = this.colGrs.iterator();
		while(colIt.hasNext()) {
			ColGroup c = colIt.next();
			this.map.put(c, Misc.getdColGrMap(c, dColGrs, symTups, dSymTups, entries));
			this.entryCount = this.entryCount + entries[0];
		}
		
		//long numInts = (long)CombinatoricsUtils.binomialCoefficientDouble(this.k, this.t) * (long)Math.pow(this.v, this.t);
		//this.entryCount = numInts * (numInts - 1);
		System.out.println("Table generated; Number of entries: " + this.entryCount);
	}
	
	public CovTab(int d, int t, int k, int[] v) {
		this.d = d;
		this.t = t;
		this.k = k;
		this.V = v;
		this.isMixed = true;
		
		if (this.d != 1) {
			System.err.println("Sorry! does not work for any case other than d=1.");
			System.exit(1);
		}
		
		this.colGrs = Misc.getColGrs(this.t, this.k);
		this.dColGrs = Misc.getdColGrs(this.colGrs);
		
		this.map = new HashMap<ColGroup, Map<dColGroup, Map<SymTuple, Set<dSymTuple>>>>();
		
		int[] numEntries = new int[]{0};
		SymTupleSetMap symTupSet = new SymTupleSetMap();
		Iterator<ColGroup> colIt = this.colGrs.iterator();
		while(colIt.hasNext()) {
			ColGroup c = colIt.next();
			this.map.put(c, Misc.getdMixedColGrMap(c, dColGrs, this.V, symTupSet, numEntries));
			this.entryCount = this.entryCount + numEntries[0];
		}
		System.out.println("Table generated.");
		System.out.flush();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}
	
	@Override
	public long countCovered(int[] row) {
		Iterator<ColGroup> colIt = this.map.keySet().iterator();
		
		long cov = 0;
		while (colIt.hasNext()) {
			ColGroup cols = colIt.next();
			SymTuple syms = Misc.getSyms(cols, row);
			
			Map<dColGroup, Map<SymTuple, Set<dSymTuple>>> map1 = this.map.get(cols);
			Iterator<dColGroup> dColIt = map1.keySet().iterator();
			while (dColIt.hasNext()) {
				dColGroup dCols = dColIt.next();
				dSymTuple dSyms = Misc.getdSyms(dCols, row);
				
				Map<SymTuple, Set<dSymTuple>> map2 = map1.get(dCols);
				Set<dSymTuple> dSymSet = map2.get(syms);
				if (dSymSet != null) {
					Iterator<dSymTuple> dSymIt = dSymSet.iterator();
					while (dSymIt.hasNext()) {
						dSymTuple dst = dSymIt.next();
						if (!dSyms.equals(dst)) {
							cov++;
						}
					}
				}
			}
		}
		
		return cov;		
	}
	
	@Override
	public long deleteCovered(int[] newRow) {
		Iterator<ColGroup> colIt = this.map.keySet().iterator();
		
		long cov = 0;
		while (colIt.hasNext()) {
			ColGroup cols = colIt.next();
			SymTuple syms = Misc.getSyms(cols, newRow);
			
			Map<dColGroup, Map<SymTuple, Set<dSymTuple>>> map1 = this.map.get(cols);
			Iterator<dColGroup> dColIt = map1.keySet().iterator();
			while (dColIt.hasNext()) {
				dColGroup dCols = dColIt.next();
				dSymTuple dSyms = Misc.getdSyms(dCols, newRow);
				
				Map<SymTuple, Set<dSymTuple>> map2 = map1.get(dCols);
				Set<dSymTuple> dSymSet = map2.get(syms);
				if (dSymSet != null) {
					Iterator<dSymTuple> dSymIt = dSymSet.iterator();
					while (dSymIt.hasNext()) {
						dSymTuple dst = dSymIt.next();
						if (!dSyms.equals(dst)) {
							cov++;
							dSymIt.remove();
						}
					}
					if (dSymSet.isEmpty()) {
						map2.remove(syms);
					}
					if (map2.isEmpty()) {
						dColIt.remove();
					}	
				}
			}
			
			if (map1.isEmpty()) {
				colIt.remove();
			}
		}
		
		this.entryCount = this.entryCount - cov;
		return cov;	
	}

	@Override
	public int getk() {
		return this.k;
	}

	@Override
	public int getv() {
		return this.v;
	}
	
	@Override
	public int[] getV() {
		return this.V;
	}
	
	@Override
	public long getEntryCount() {
		return this.entryCount;
	}	
	
	public static void main(String[] args) {
		CovTab ct = new CovTab(1,2,3,2);
		System.out.println(ct.map);
		
		ColGroup c1 = new ColGroup(new int[]{0,2});
		SymTuple s1 = new SymTuple(new int[]{0,0});
		
		ColGroup c2 = new ColGroup(new int[]{0,1});
		SymTuple s2 = new SymTuple(new int[]{0,1});
		
		System.out.println(ct.deleteCovered(new int[]{0, 0, 0}));
		System.out.println(ct.map);
		
		/*Iterator<dSymTuple> it = ct.map.get(c1).get(new dColGroup(new ColGroup[]{c2})).get(s1).iterator();
		dSymTuple d = new dSymTuple(new SymTuple[]{s2});
		while(it.hasNext()) {
			dSymTuple d1 = it.next();
			if (!d.equals(d1)) {
				it.remove();
			}
		}
		
		System.out.println(ct.map);*/
	}

}
