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

package edu.asu.ca.kaushik.algorithms.structures.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.asu.ca.kaushik.algorithms.structures.Interaction;

public class Vertex {
	
	private Interaction intrctn;
	private List<Vertex> neighbors;
	private int color;

	public Vertex(Interaction intrctn) {
		this.intrctn = intrctn;
		this.neighbors = new ArrayList<Vertex>();
		this.color = -1;
	}

	public boolean isAdjacent(Vertex v) {
		return !this.intrctn.isCompatible(v.getInteraction());
	}
	
	public void addNeighbor(Vertex v) {
		this.neighbors.add(v);
	}
	
	public int greedyColor() {
		Set<Integer> colors = new HashSet<Integer>();
		int max = -1;
		for (Vertex n : this.neighbors) {
			Integer nCol = n.getColor();
			if (nCol != -1) {
				colors.add(nCol);
				if (nCol > max) {
					max = nCol;
				}
			}
		}
		
		for (int i = 0; i <= max; i++) {
			if (!colors.contains(i)) {
				this.color = i;
				return this.color;
			}
		}
		
		this.color = max + 1;
		return this.color;
	}

	public Interaction getInteraction() {
		return intrctn;
	}

	public List<Vertex> getNeighbors() {
		return neighbors;
	}

	public int getColor() {
		return color;
	}
	
	public int getDegree() {
		return this.neighbors.size();
	}
	
	@Override
	public String toString() {
		String s = "Vertex [Interaction=" + this.intrctn.toString() + "\n";
		s = s + "Neighbors:\n";
		for (Vertex v : this.neighbors) {
			s = s + "\tInteraction=" + v.getInteraction().toString() + "\n";
		}
				
		return s + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intrctn == null) ? 0 : intrctn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vertex)) {
			return false;
		}
		Vertex other = (Vertex) obj;
		if (intrctn == null) {
			if (other.intrctn != null) {
				return false;
			}
		} else if (!intrctn.equals(other.intrctn)) {
			return false;
		}
		return true;
	}	
	
}
