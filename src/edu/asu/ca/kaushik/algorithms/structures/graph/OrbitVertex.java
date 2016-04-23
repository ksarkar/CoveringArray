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
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.GroupElement;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;

public class OrbitVertex {
	private Interaction orbit;
	private List<OrbitVertex> neighbors;
	private boolean fixed;
	
	public OrbitVertex(Interaction orb) {
		this.orbit = orb;
		this.neighbors = new ArrayList<OrbitVertex>();
		this.fixed = false;
	}
	
	public Interaction getOrbit() {
		return this.orbit;
	}
	
	public boolean isFixed() {
		return this.fixed;
	}
	
	public void setOrbit(Interaction orbRep) {
		this.orbit = orbRep;
	}

	public void addNeighbor(OrbitVertex v) {
		this.neighbors.add(v);		
	}
	
	public List<OrbitVertex> getNeighbors() {
		return this.neighbors;
	}
	
	public int decideEdge(OrbitVertex u, Group g) {
		int isAdj = 0;
		ColGroup cg1 = this.orbit.getCols();
		ColGroup cg2 = u.orbit.getCols();
		
		if (cg1.intersects(cg2)) {
			if (this.fixed && u.isFixed()) {
				if (!this.orbit.isCompatible(u.getOrbit())) {
					this.addNeighbor(u);
					u.addNeighbor(this);
					isAdj = 1;
				}
			} else {
				OrbitVertex v1 = null;
				OrbitVertex v2 = null;
				if (u.isFixed()) {
					v1 = this;
					v2 = u;
				} else {
					v1 = u;
					v2 = this;
				}
				
				GroupElement e = v1.getOrbit().convert(v2.getOrbit(), g);
				if (e == null) {
					v1.addNeighbor(v2);
					v2.addNeighbor(v1);
					isAdj = 1;
				} else {
					Interaction nint = g.act(e, v1.getOrbit());
					v1.setOrbit(nint);
					v1.fixed = true;
					v2.fixed = true;
				}				
			}
		}
		
		
		return isAdj;
	}
}
