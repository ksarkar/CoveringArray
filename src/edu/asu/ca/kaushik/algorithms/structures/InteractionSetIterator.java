package edu.asu.ca.kaushik.algorithms.structures;

import java.util.List;

public class InteractionSetIterator {
	List<ColGroup> colGrs;
	List<SymTuple> symTups;
	int colGrInd;
	int symTupInd;
	int colGrSz;
	int symTupSz;

	public InteractionSetIterator(int t, int k, int v) {
		this.colGrs = Helper.createAllColGroups(t, k);
		this.symTups = Helper.createAllSymTuples(t, v);
		this.colGrInd = 0;
		this.symTupInd = 0;
		this.colGrSz = this.colGrs.size();
		this.symTupSz = this.symTups.size();
	}

	public boolean hasNext() {
		return (this.colGrInd < this.colGrSz) ? true : false;
	}

	public Interaction next() {
		Interaction interacton = new Interaction(this.colGrs.get(this.colGrInd), 
													this.symTups.get(symTupInd));
		if (this.symTupInd < this.symTupSz - 1) {
			this.symTupInd++;
		} else {
			this.colGrInd++;
			this.symTupInd = 0;
		}
		
		return interacton;
	}

	public void rewind() {
		this.colGrInd = this.symTupInd = 0;		
	}

}
