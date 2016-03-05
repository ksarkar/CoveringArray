package edu.asu.ca.kaushik.algorithms.structures.graph;

import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;

public class ConflictGraph {
	
	private List<Vertex> vertices;
	private int numEdges;
	
	/**
	 * 
	 * @param intList List(set) of interactions involved in the conflict graph.
	 */
	public ConflictGraph(List<Interaction> intList) {
		this.vertices = new ArrayList<Vertex>();
		for (Interaction intrctn : intList) {
			vertices.add(new Vertex(intrctn));
		}
		
		this.numEdges = 0;
		this.addEdges();
	}

	public ConflictGraph() {
		this.vertices = new ArrayList<Vertex>();
		this.numEdges = 0;
	}

	public int getNumEdges() {
		return numEdges;
	}

	/**
	 * Builds the adjacency list initially
	 */
	private void addEdges() {
		for (Vertex u : this.vertices) {
			for (Vertex v : this.vertices) {
				if (!u.equals(v)) {
					if (u.isAdjacent(v)) {
						u.addNeighbor(v);
						this.numEdges = this.numEdges + 1;
					}
				}
			}
		}
		
		this.numEdges = this.numEdges / 2;
	}
	
	/**
	 * Adds a new vertex for inter and colors the vertex greedily
	 * @param inter the new vertex to added and colored
	 */
	public void addVertexAndGreedyColor(Interaction inter) {
		Vertex v = new Vertex(inter);
		int edges = 0;
		for (Vertex u : this.vertices) {
			if (v.isAdjacent(u)) {
				v.addNeighbor(u); // Note: we don't add v as a neighbor of u
				edges++; // each edge is counted exactly once
			}
		}
		this.vertices.add(v);
		this.numEdges = this.numEdges + edges;
		
		v.greedyColor();
	}
	
	public int getMaxDeg() {
		int maxDeg = -1;
		for (Vertex v : this.vertices) {
			int deg = v.getDegree();
			if (deg > maxDeg) {
				maxDeg = deg;
			}
		}
		return maxDeg;
	}
	
	private int getMinDeg() {
		int minDeg = this.vertices.size();
		for (Vertex v : this.vertices) {
			int deg = v.getDegree();
			if (deg < minDeg) {
				minDeg = deg;
			}
		}
		return minDeg;
	}
	
	/**
	 * If this graph is colored then add rows to remCA to cover the interactions
	 * accordingly.
	 * @param remCA
	 */
	public void convertColoringToCA(ListCAExt remCA) {
		List<List<Vertex>> colorClasses = new ArrayList<List<Vertex>>();
		
		for (Vertex v : this.vertices) {
			int c = v.getColor();
			if (c >= colorClasses.size()) { 
				List<Vertex> newColor = new ArrayList<Vertex>();
				newColor.add(v);
				colorClasses.add(newColor);
			} else {
				colorClasses.get(c).add(v);
			}
		}
		
		this.buildCA(remCA, colorClasses);
	}

	/**
	 * Colors this graph using col(G) number of colors. Greedy coloring
	 * 
	 * @param remCA The coloring is represented by a covering array that covers all
	 * 				the interactions in the conflict graph
	 */
	public void color(ListCAExt ca) {
		Vertex[] slOrder = smallestLastOrder(this);
		List<List<Vertex>> colorClasses = new ArrayList<List<Vertex>>();
		
		int n = slOrder.length;
		for (int i = 0; i < n; i++) {
			int c = slOrder[i].greedyColor();
			if (c >= colorClasses.size()) { 
				List<Vertex> newColor = new ArrayList<Vertex>();
				newColor.add(slOrder[i]);
				colorClasses.add(newColor);
			} else {
				colorClasses.get(c).add(slOrder[i]);
			}
		}
		
		this.buildCA(ca, colorClasses);	
	}

	private static Vertex[] smallestLastOrder(ConflictGraph graph) {
		List<List<Vertex>> adjL = new ArrayList<List<Vertex>>();
		
		for (Vertex v : graph.vertices) {
			List<Vertex> list = new ArrayList<Vertex>();
			list.add(v);
			list.addAll(v.getNeighbors());
			adjL.add(list);
		}
		
		int n = adjL.size();
		Vertex[] slOrder = new Vertex[n];
		getSmallestLastOrder(n-1, slOrder, adjL);
		
		return slOrder;
	}

	private static void getSmallestLastOrder(int maxInd, Vertex[] slOrder,
			List<List<Vertex>> adjL) {
		
		for (int i = maxInd; i >= 0; i--) {
			Vertex minDeg = findDeleteMinDeg(adjL);
			slOrder[i] = minDeg;
		}
		return;
	}

	private static void getSmallestLastOrderRec(int n, Vertex[] slOrder,
			List<List<Vertex>> vertices) {
		
		if(n >= 0){
			Vertex minDeg = findDeleteMinDeg(vertices);
			slOrder[n] = minDeg;
			getSmallestLastOrder(n-1, slOrder, vertices);
		}
		return;
	}

	private static Vertex findDeleteMinDeg(List<List<Vertex>> vertices) {
		Vertex minDegVert = null;
		int min = vertices.size();
		int index = -1;
		int i = 0;
		for (List<Vertex> list: vertices) {
			int deg = list.size() - 1;
			if (deg < min) {
				min = deg;
				index = i;
			}
			i++;
		}
		
		List<Vertex> mAdjL = vertices.remove(index);
		minDegVert = mAdjL.get(0);
		
		for (List<Vertex> li : vertices) {
			li.remove(minDegVert);			
		}
		
		return minDegVert;
	}
	
	private void buildCA(ListCAExt ca, List<List<Vertex>> colorClasses) {
		int k = ca.getK();
		
		for (List<Vertex> colorClass: colorClasses) {
			ca.addRow(getRow(k, colorClass));
		}
		
		ca.setNumUncovInt(this.vertices.size());
		ca.setNumEdges(this.numEdges);
		ca.setMaxDeg(this.getMaxDeg());
		ca.setMinDeg(this.getMinDeg());
		ca.setNumColors(ca.getNumRows());
	}

	private static Integer[] getRow(int k, List<Vertex> colorClass) {
		Integer[] row = new Integer[k];
		for (int i = 0; i < k; i++) {
			row[i] = 0;
		}
		
		for (Vertex v : colorClass) {
			setSymbols(row, v);
		}
		
		return row;
	}

	private static void setSymbols(Integer[] row, Vertex v) {
		Interaction intrctn = v.getInteraction();
		
		int[] cols = intrctn.getCols().getCols();
		int[] syms = intrctn.getSyms().getSyms();
		
		int t = cols.length;
		
		for (int i = 0; i < t; i++) {
			row[cols[i]] = syms[i];
		}
		
	}

}
