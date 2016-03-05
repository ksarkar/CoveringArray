package edu.asu.ca.kaushik.algorithms.randomized;

import java.util.Arrays;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;

public class AllColBalancedRow extends RandRowCAGenAlgo {
	private Random rand;
	private double devFracStart;
	private double devFracEnd;
	private double step;
	
	public AllColBalancedRow() {
		super();
		super.name = new String("AllColBalancedRow");
		this.rand = new Random(1234L);
		this.devFracStart = 0.05d; 
		this.devFracEnd = 0.5d;
		this.step = 0.05d;
		super.maxNumSamples = 100;
	}

	@Override
	protected boolean selectRandRow(InteractionGraph ig, 
			Integer[] nextRow) {
		int t = ig.getT();
		int k = ig.getK();
		int v = ig.getV();
		
		assert (k == nextRow.length);
		
		int[] numUncovIntD = ig.getUncovDist();
		int[] lowCovCol = new int[k];
		int[] highCovCol = new int[k];
		int[] av = new int[k];
		for (int i = 0; i < k; i++) {
			av[i] = computeAverage(numUncovIntD[i], t, v);
		}
		
		Integer[] maxRow = null;
		int max = Integer.MIN_VALUE;
		for (double d = this.devFracStart; d <= this.devFracEnd; d = d + this.step) {
			for (int i = 0; i < k; i++) {
				lowCovCol[i] = (int)Math.ceil((1.0d - d) * av[i]);
				highCovCol[i] = (int)Math.ceil((1.0d + d) * av[i]);
			}
			
			max = Integer.MIN_VALUE;
			int[] covD = new int[k];
			for (int i = 0; i < super.maxNumSamples; i++){
				makeRandRow(nextRow, v);
				int coverage = ig.getCoverage(nextRow, covD);
				
				if (isGoodRow(covD, lowCovCol, highCovCol)) {
					return true;
				}
				
				if (coverage > max) {
					max = coverage;
					maxRow = Arrays.copyOf(nextRow, nextRow.length);
				}
			}
		}
		
		assert (maxRow != null);
		for (int i = 0; i < k; i++){
			nextRow[i] = maxRow[i];
		}
		return false;
	}

	private boolean isGoodRow(int[] covD, int[] lowCovCol, int[] highCovCol) {
		int k = covD.length;
		for (int i = 0; i < k; i++) {
			if ((covD[i] < lowCovCol[i]) || (covD[i] > highCovCol[i])) {
				return false;
			}
		}
		return true;
	}

	private void makeRandRow(Integer[] nextRow, int v) {
		int k = nextRow.length;
		
		for (int i = 0; i < k; i++){
			nextRow[i] = new Integer(this.rand.nextInt(v));
		}
		
	}
	
	private int computeAverage(long numUncovInt, int t, int v) {
		return (int)Math.ceil(numUncovInt / Math.pow(v, t));
	}
}
