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
import java.util.Iterator;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.twostage.group.GTSOnlineGreedy;

public class OrbitConflictGraph {
	private List<OrbitVertex> vertices;
	private int numEdges;
	private Group g;

	public OrbitConflictGraph(List<Interaction> orbits, Group group) {
		this.g = group;
		
		this.vertices = new ArrayList<OrbitVertex>();
		for (Interaction orbit : orbits) {
			OrbitVertex v = new OrbitVertex(orbit);
			Iterator<OrbitVertex> vIt = this.vertices.iterator();
			while(vIt.hasNext()) {
				OrbitVertex u = vIt.next();
				this.numEdges = this.numEdges + v.decideEdge(u,this.g);
			}
			this.vertices.add(v);
		}
	}
	
	public int getNumEdges() {
		return this.numEdges;
	}

	public void color(ListCAExt remCA) {
		int k = remCA.getK();
		int v = remCA.getV();
		
		System.out.println("Creating smallest last order...");
		OrbitVertex[] slOrder = this.smallestLastOrder();
		
		System.out.println("greedy coloring...");
		for (OrbitVertex u : slOrder) {
			Interaction orbRep = u.getOrbit();
			if (!GTSOnlineGreedy.greedyCover(orbRep, remCA, this.g)) {
				Integer[] row = GTSOnlineGreedy.getNewRow(k, v, orbRep);
				remCA.addRow(row);
			}
		}		
	}

	private OrbitVertex[] smallestLastOrder() {
		int n = this.vertices.size();
		OrbitVertex[] slOrder = new OrbitVertex[n];
		
		for (int i = n - 1; i >= 0; i--) {
			OrbitVertex minDeg = this.findDeleteMinDeg();
			slOrder[i] = minDeg;
		}
		return slOrder;
	}

	private OrbitVertex findDeleteMinDeg() {
		OrbitVertex minDegVert = null;
		int min = this.vertices.size();
		int index = -1;
		int i = 0;
		for (OrbitVertex v : this.vertices) {
			int deg = v.getNeighbors().size();
			if (deg < min) {
				min = deg;
				index = i;
			}
			i++;
		}
		
		minDegVert = this.vertices.remove(index);
		
		List<OrbitVertex> neighbors = minDegVert.getNeighbors();
		for (OrbitVertex v : neighbors) {
			v.getNeighbors().remove(minDegVert);
		}
		
		return minDegVert;
	}
}
