package edu.asu.ca.kaushik.algorithms.structures;

import java.util.Date;

import org.apache.commons.math3.util.CombinatoricsUtils;

public class ColGrIterator1 implements ColGrIteratorInterface {
	long numGroups;
	long count;
	int[] cols;
	int t;
	int k;
	
	public ColGrIterator1(int t, int k) {
		assert(t <= k);
		
		this.t = t;
		this.k = k;
		this.numGroups = (long)CombinatoricsUtils.binomialCoefficientDouble(k, t);
		this.count = 1;
		this.cols = new int[t];
		this.initiateCols();
	}
	
	private void initiateCols() {
		for (int i = 0 ; i < this.t; i++) {
			this.cols[i] = i;
		}
		this.cols[this.t - 1]--;
	}

	@Override
	public boolean hasNext() {
		return (this.count <= this.numGroups) ? true : false;
	}

	@Override
	public ColGroup next() {
		do {
			this.incrementCounter();
		} while(!this.validCombination());
		
		this.count++;
		return new ColGroup(this.cols);
	}

	private void incrementCounter() {
		int ind = t-1;
		while (this.cols[ind] == this.k - 1) {
			this.cols[ind] = 0;
			ind--;
		}
		this.cols[ind]++;
	}
	
	private boolean validCombination() {
		for (int i = 0; i < this.t; i++) {
			for (int j = i-1; j >= 0; j--) {
				if (this.cols[i] <= this.cols[j]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void rewind() {
		this.count = 1;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int t = 6;
		int k = 52;
		
		System.out.println("Time efficient:");
		System.out.println(new Date());
		ColGrIteratorInterface ci = new ColGrIterator(t,k);
		long tot = 0;
		while(ci.hasNext()) {
			//System.out.println(ci.next());
			ci.next();
			tot++;
		}
		System.out.println(new Date());
		System.out.println("total = " + tot);
		
		System.out.println();
		
		System.out.println("Memory efficient:");
		System.out.println(new Date());
		ci = new ColGrIterator1(t,k);
		tot = 0;
		while(ci.hasNext()) {
			//System.out.println(ci.next());
			ci.next();
			tot++;
		}
		System.out.println(new Date());
		System.out.println("total = " + tot);

	}

	

}
