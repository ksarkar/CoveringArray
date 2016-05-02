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

package edu.asu.ca.kaushik.algorithms.twostage.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.GroupElement;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class GTSOnlineGreedy extends GroupTwoStage {
	private ListCA rem; 
	private Group g;
	private int t,k,v;

	public GTSOnlineGreedy(int f, int times, int s, int gId) {
		super(f, times, s, gId);
		super.name = super.name + "-greedy-" + times + "-times";
	}

	@Override
	public void initSecondStage(int t, int k, int v, Group group) {
		this.t = t;
		this.k = k;
		this.v = v;
		this.rem = new ListCA(t,k,v);
		this.g = group;
	}

	@Override
	public void cover(List<Interaction> notCovered) {
		for (Interaction orbit : notCovered) {
			if (!greedyCover(orbit, this.rem, this.g)) {
				Integer[] row = getNewRow(this.k, this.v, orbit);
				this.rem.addRow(row);
			}
		}
	}
	
	public static boolean greedyCover(Interaction orbit, ListCA ca, Group grp) {
		Iterator<Integer[]> it = ca.iterator();
		while(it.hasNext()) {
			Integer[] row = it.next();
			Interaction nInt = makeCompatible(row, orbit, grp, ca.getV());
			if (nInt != null) {
				modify(row, nInt);
				return true;
			}
		}
		return false;
	}
	
	private static Interaction makeCompatible(Integer[] row, Interaction orbit, Group grp, int v) {
		int[] cols = orbit.getCols().getCols();
		int[] syms = orbit.getSyms().getSyms();
		int t = cols.length;
		List<Integer> conflict = new ArrayList<Integer>();
		for (int i = 0; i < t; i++) {
			if (row[cols[i]] != v) { // not a starred entry
				conflict.add(i);
			}
		}
		
		if (conflict.size() == 0) {
			return orbit;
		} else {
			int l = conflict.size();
			int[] s1 = new int[l];
			int[] s2 = new int[l];
			for (int i = 0; i < l; i++) {
				int ind = conflict.get(i);
				s1[i] = syms[ind];
				s2[i] = row[cols[ind]];
			}
			
			GroupElement ge = grp.convert(new SymTuple(s1), new SymTuple(s2));
			if (ge == null) {
				return null;
			} else {
				Interaction nInt = grp.act(ge, orbit);
				return nInt;
			}
		}
	}
	
	private static void modify(Integer[] row, Interaction inter) {
		int[] cols = inter.getCols().getCols();
		int[] syms = inter.getSyms().getSyms();
		int t = cols.length;
		for (int i = 0; i < t; i++) {
			row[cols[i]] = syms[i];
		}		
	}
	
	public static Integer[] getNewRow(int k, int v, Interaction inter) {
		Integer[] row = new Integer[k];
		for (int i = 0; i < k; i++) {
			row[i] = v; // put a star
		}
		modify(row, inter);
		return row;
	}

	@Override
	public void reset() {
		this.rem = new ListCA(this.t, this.k, this.v);
	}

	@Override
	public void secondStage(ListCAExt remCA) {
		Iterator<Integer[]> it = this.rem.iterator();
		while (it.hasNext()) {
			remCA.addRow(removeStarEntries(it.next(), this.v));
		}
	}

	public static Integer[] removeStarEntries(Integer[] next, int v) {
		int k = next.length;
		
		for (int i = 0; i < k; i++) {
			if (next[i] == v) {
				next[i] = 0;
			}
		}
		return next;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int t = 0, k1 = 0, k2 = 0, v = 0, times = 0, f = 0, s = 0, g = -1;
		
		if (args.length == 8) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
			times = Integer.parseInt(args[4]);
			f = Integer.parseInt(args[5]);
			s = Integer.parseInt(args[6]);
			g = Integer.parseInt(args[7]);
		} else {
			System.err.println("Need 8 arguments- t, v, kStart, kEnd, times, firstStage, slack percent and group type");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new GTSOnlineGreedy(f, times, s, g));
		System.out.println("times = " + times);
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\group-two-stage"
				, "group-two-stage-greedy");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
