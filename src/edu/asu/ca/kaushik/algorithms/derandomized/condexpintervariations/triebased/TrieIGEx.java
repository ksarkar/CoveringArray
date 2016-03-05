package edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased;

import java.util.Arrays;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;

public class TrieIGEx extends TrieIG {
	
	
	
	public TrieIGEx() {
		super();
		// TODO Auto-generated constructor stub
	}


	public TrieIGEx(int t, int k, int v) {
		super(t, k, v);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected List<int[]> getCandidates(ColGroup cols) {
		return Helper.createAllTuples(super.getT(), super.getV());
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<int[]> c = Helper.createAllTuples(3,2);
		for (int[] a : c) {
			System.out.println(Arrays.toString(a));
		}

	}

}
