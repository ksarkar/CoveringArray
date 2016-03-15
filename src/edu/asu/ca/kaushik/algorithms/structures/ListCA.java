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
import java.util.Iterator;
import java.util.List;

/*
 * Concrete implementation of CA interface used by Biased Density algorithms
 */
public class ListCA implements PartialCA {
	private int t;
	private int k;
	private int v;
	private List<Integer[]> ca;
	
	private Instrumentation instru;
	
	public ListCA(int t, int k, int v) {
		this.t = t;
		this.k = k;
		this.v = v;
		this.ca = new ArrayList<Integer[]>();
		
		this.instru = new Instrumentation();
	}
	
	@Override
	public boolean addRow(Integer[] newRow) {
		assert(newRow.length == this.k);
		/*if (newRow.length != this.rowLen) {
			return false;
		}*/
		
		this.ca.add(newRow);
		return true;
	}
	
	@Override
	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	@Override
	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	@Override
	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	@Override
	public int getNumRows(){
		return ca.size();
	}
	
	@Override
	public Iterator<Integer[]> iterator() {
		return ca.iterator();
	}

	@Override
	public String toString() {
		String s =  this.getNumRows() + "\nCA [";
		for (Integer[] a : this.ca){
			for (Integer i : a){
				s = s + i.toString() + ", ";
			}
			s = s + "\n    ";
		}
		return s + "]";
	}

	public void addComp(int comp) {
		this.instru.addComp(comp);
	}

	public void addCoverage(int coverage) {
		this.instru.addCoverage(coverage);		
	}
	
	public void addCovDist(int[] covD) {
		this.instru.addCovDist(covD);
	}
	
	public void addStatus(boolean status) {
		this.instru.addStatus(status);
	}

	@Override
	public Instrumentation getInstrumentation() {
		return this.instru;
	}

	public void join(CA ca) {
		assert((this.t == ca.getT()) && (this.k == ca.getK()) && (this.v == this.getV()));
		
		Iterator<Integer[]> caIt = ca.iterator();
		
		while (caIt.hasNext()) {
			this.addRow(caIt.next());
		}
	}
	
	public Integer[] getRow(int i) {
		assert(i < this.ca.size());
		return this.ca.get(i);
	}
}
