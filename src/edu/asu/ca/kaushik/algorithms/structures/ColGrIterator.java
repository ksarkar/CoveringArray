package edu.asu.ca.kaushik.algorithms.structures;

import java.util.List;

/**
 * This implementation is not memory efficient. During initialization it generates
 * all possible column groups and stores them in the memory as a list.
 * @author ksarkar1
 *
 */

public class ColGrIterator implements ColGrIteratorInterface {

	private List<ColGroup> colGrs;
	private int colGrInd;
	private int colGrSz;
	

	public ColGrIterator(int t, int k) {
		//TODO use true iterator
		this.colGrs = Helper.createAllColGroups(t, k);
		this.colGrInd = 0;
		this.colGrSz = this.colGrs.size();
	}

	public boolean hasNext() {
		return (this.colGrInd < this.colGrSz) ? true : false;
	}

	public ColGroup next() {
		ColGroup cols = this.colGrs.get(this.colGrInd);
		this.colGrInd++;
		
		return cols;
	}

	public void rewind() {
		this.colGrInd = 0;		
	}


}
