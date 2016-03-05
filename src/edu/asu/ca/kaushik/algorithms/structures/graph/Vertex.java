package edu.asu.ca.kaushik.algorithms.structures.graph;

import java.util.ArrayList;
import java.util.Arrays;
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
		int[] thisCols = this.intrctn.getCols().getCols();
		int[] thisSyms = this.intrctn.getSyms().getSyms();
		
		int[] thatCols = v.getInteraction().getCols().getCols();
		int[] thatSyms = v.getInteraction().getSyms().getSyms();
		
		int t = thisCols.length;
		for (int i = 0; i < t; i++) {
			for (int j = 0; j < t; j++) {
				if (thisCols[i] == thatCols[j]) { // same columns
					if (thisSyms[i] != thatSyms[j]) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void addNeighbor(Vertex v) {
		this.neighbors.add(v);
	}
	
	public int greedyColor() {
		//List<Integer> colors = new ArrayList<Integer>();
		Set<Integer> colors = new HashSet<Integer>();
		for (Vertex n : this.neighbors) {
			Integer nCol = n.getColor();
			if (nCol != -1) {
				colors.add(nCol);
			}
		}
		
		int max = -1;
		for (Integer c : colors) {
			if (c > max) {
				max = c;
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
