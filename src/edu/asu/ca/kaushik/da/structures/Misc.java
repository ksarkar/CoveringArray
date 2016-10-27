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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class Misc {
	
	public static List<ColGroup> getColGrs(int t, int k) {
		List<ColGroup> l = new ArrayList<ColGroup>();
		ColGrIterator colIt = new ColGrLexIterator(t, k);
		while (colIt.hasNext()) {
			l.add(colIt.next());
		}
		return l;
	}
	
	public static List<dColGroup> getdColGrs(List<ColGroup> colGrs) {
		//TODO modify for general d
		List<dColGroup> l = new ArrayList<dColGroup>();
		ColGroup[] dColGrs = new ColGroup[1];
		Iterator<ColGroup> colIt = colGrs.iterator();
		
		while(colIt.hasNext()) {
			dColGrs[0] = colIt.next();
			l.add(new dColGroup(dColGrs));
		}
		return l;		
	}
	
	public static Set<dSymTuple> getDSymTupSet(List<SymTuple> syms) {
		//TODO modify for general d
		Set<dSymTuple> set = new HashSet<dSymTuple>();
		SymTuple[] tups = new SymTuple[1];
		Iterator<SymTuple> sIt = syms.iterator();
		while (sIt.hasNext()) {
			tups[0] = sIt.next();
			set.add(new dSymTuple(tups));
		}
		return set;
	}
	
	public static Set<dSymTuple> getDSymTupleSet(dColGroup dCols, int[] v) {
		// TODO Modify for general d
		ColGroup[] dc = dCols.getColGroups();
		List<SymTuple> l = getSymTuples(dc[0], v);
		return Misc.getDSymTupSet(l);
	}	

	public static Map<dColGroup, Map<SymTuple, Set<dSymTuple>>> getdColGrMap(ColGroup cols, 
			List<dColGroup> dColGrs, List<SymTuple> syms, Set<dSymTuple> dSyms, int[] entries) {
		
		entries[0] = 0;
		Map<dColGroup, Map<SymTuple, Set<dSymTuple>>> m 
			= new HashMap<dColGroup, Map<SymTuple, Set<dSymTuple>>>();
		Iterator<dColGroup> dColIt = dColGrs.iterator();
		while(dColIt.hasNext()) {
			dColGroup dCols = dColIt.next();
			m.put(dCols, getSymTupleMap(syms, dSyms, dCols.contains(cols), entries));
		}
		return m;
	}

	public static Map<SymTuple, Set<dSymTuple>> getSymTupleMap(List<SymTuple> syms, Set<dSymTuple> dSyms, 
			boolean isIn, int[] numEntries) {
		Map<SymTuple, Set<dSymTuple>> m = new HashMap<SymTuple, Set<dSymTuple>>();
		Iterator<SymTuple> sIt = syms.iterator();
		while(sIt.hasNext()) {
			SymTuple s = sIt.next();
			Set<dSymTuple> newSet = new HashSet<dSymTuple>(dSyms);
			if (isIn) {
				removeDSymTups(s, newSet);
			}
			numEntries[0] = numEntries[0] + newSet.size();
			m.put(s, newSet);
		}
		return m;
	}

	private static void removeDSymTups(SymTuple s, Set<dSymTuple> newSet) {
		// TODO modify it for general d
		// currently works for only d = 1
		SymTuple[] a = new SymTuple[1];
		a[0] = s;
		dSymTuple ds = new dSymTuple(a);
		newSet.remove(ds);		
	}
	
	public static Map<dColGroup, Map<SymTuple, Set<dSymTuple>>> getdMixedColGrMap(
			ColGroup c, List<dColGroup> dColGrs, int[] V, SymTupleSetMap setMap, int[] numEntries) {
		//TODO modify for general d
		Map<dColGroup, Map<SymTuple, Set<dSymTuple>>> m 
			= new HashMap<dColGroup, Map<SymTuple, Set<dSymTuple>>>();
		
		int[] l1 = Misc.getLevels(c, V);
		List<SymTuple> syms = setMap.getSymTups(new SymTuple(l1));
				
		numEntries[0] = 0;
		Iterator<dColGroup> dColIt = dColGrs.iterator();
		while(dColIt.hasNext()) {
			dColGroup dCols = dColIt.next();
			ColGroup[] dc = dCols.getColGroups();
			int[] l2 = Misc.getLevels(dc[0], V);
			List<SymTuple> syms2 = setMap.getSymTups(new SymTuple(l2));
			Set<dSymTuple> dSyms = Misc.getDSymTupSet(syms2);
			m.put(dCols, getSymTupleMap(syms, dSyms, dCols.contains(c), numEntries));
		}
		return m;
	}
	
	public static dSymTuple getdSyms(dColGroup dCols, int[] row) {
		ColGroup[] dc = dCols.getColGroups();
		int d = dc.length;
		SymTuple[] ds = new SymTuple[d];
		for (int i = 0; i < d; i++) {
			ds[i] = getSyms(dc[i], row);
		}
		return new dSymTuple(ds);
	}

	public static SymTuple getSyms(ColGroup cols, int[] row) {
		int[] c = cols.getCols();
		int t = c.length;
		int[] s = new int[t];
		
		for (int i = 0; i < t; i++) {
			s[i] = row[c[i]];
		}
		return new SymTuple(s);
	}

	public static List<SymTuple> getSymTuples(ColGroup cols, int[] v) {
		int[] l = getLevels(cols, v);
		return getSymTupList(l);
		
	}
	
	public static List<SymTuple> getSymTupList(int[] levels) {
		int[] s = new int[levels.length];
		s[levels.length-1]--;
		int[] max = getMax(levels);
		List<SymTuple> syms = new ArrayList<SymTuple>();
		do {
			increment(s, levels);
			syms.add(new SymTuple(s));
		} while(!Arrays.equals(s, max));
		return syms;
	}

	private static void increment(int[] s, int[] l) {
		int t = s.length;
		s[t-1]++;
		
		for (int i = t-1; s[i] >= l[i]; i--) {
			s[i] = 0;
			s[i-1]++;
		}	
	}

	private static int[] getMax(int[] l) {
		int t = l.length;
		int[] max = new int[t];
		for (int i = 0; i < t; i++) {
			max[i] = l[i] - 1;
		}
		return max;
	}

	public static int[] getLevels(ColGroup cols, int[] v) {
		int[] c = cols.getCols();
		int t = c.length;
		int[] l = new int[t];
		for (int i = 0; i < t; i++) {
			l[i] = v[c[i]];
		}
		return l;
	}
	
	public static int[] makeStarredRow(int k, int v) {
		int[] newRow = new int[k];
		for (int i = 0; i < k; i++){
			newRow[i] = v;
		}
		
		return newRow;
	}
	
	public static int count(Map<SymTuple, Set<dSymTuple>> map, SymTuple syms,
			dSymTuple dsyms) {
		
		if (map.isEmpty()) {
			return 0;		// no more witness needed
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
	
	public static int findMax(int[] v) {
		int max = -1;
		for (int i = 0; i < v.length; i++) {
			if (v[i] > max) {
				max = v[i];
			}
		}
		return max;
	}
	
	public static void setEntries(int[] row, int[] freeIndices, int[] entries) {
		int freeIndNum = freeIndices.length;
		for (int i = 0; i < freeIndNum; i++) {
			row[freeIndices[i]] = entries[i];
		}
	}

	
	
	public static int[] getAldacoLevels() {
		int[] v = new int[75];
		int i = 0;
		
		for (int j = 0; j < 28; j++,i++) {
			v[i] = 2;
		}
		
		for (int j = 0; j < 9; j++,i++) {
			v[i] = 3;
		}
		
		for (int j = 0; j < 6; j++,i++) {
			v[i] = 4;
		}
		
		for (int j = 0; j < 4; j++,i++) {
			v[i] = 5;
		}
		
		for (int j = 0; j < 10; j++,i++) {
			v[i] = 6;
		}
		
		for (int j = 0; j < 5; j++,i++) {
			v[i] = 7;
		}
		
		for (int j = 0; j < 4; j++,i++) {
			v[i] = 8;
		}
		
		for (int j = 0; j < 1; j++,i++) {
			v[i] = 9;
		}
		
		for (int j = 0; j < 8; j++,i++) {
			v[i] = 10;
		}
		
		return v;
		
	}
	
	public static void main(String[] args) {
		ColGroup c = new ColGroup(new int[]{0,1});
		int[] v = new int[]{2,3,4};
		List<SymTuple> syms = getSymTuples(c,v);
		System.out.println(syms.size());
		/*for (SymTuple s : syms) {
			System.out.println(s);
		}*/
		
		ColGroup c1 = new ColGroup(new int[]{0,1});
		dColGroup dc = new dColGroup(new ColGroup[]{c1});
		System.out.println(getSymTupleMap(syms, getDSymTupleSet(dc,v), dc.contains(c), new int[]{0}));
		
		System.out.println(Arrays.toString(getAldacoLevels()));
	}

}
