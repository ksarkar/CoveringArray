package edu.asu.ca.kaushik.algorithms.twostage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.twostage.TwoStage;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

/**
 * Two-stage algorithm with: first stage -- random (depends on flag); second stage -- simple (one row per uncovered interaction)
 * @author ksarkar1
 *
 */
public class TwoStageSimple extends TwoStage {
	private int n; // number of rows in the partial array of first stage
	private List<Interaction> uncovInts; // to keep track of all the uncovered interactions
	
	/**
	 * 
	 * @param n
	 * @param firstStage What randomized algorithm to use in stage 1. Valid values: 0 -- uniform random, 
	 * 1 -- re-sample only the last offending interaction, 2 -- re-sample all the offending interactions
	 */
	public TwoStageSimple(int n, int firstStage) {
		super(firstStage);
		this.n = n;
		this.uncovInts = new ArrayList<Interaction>();
	}
	
	public TwoStageSimple(int firstStage) {
		this(0, firstStage);
	}
	
	@Override
	public String getName() {
		return new String("TwoStageSimple");
	}
	
	private int twoStageSimpleBound(int t, int k, int v) {
		double kChooset = CombinatoricsUtils.binomialCoefficientDouble(k, t);
		double vpowt = Math.pow(v, t);
		double denom = Math.log(vpowt / (vpowt - 1));
		double nume = Math.log(kChooset * vpowt * denom);
		return (int)Math.ceil(nume/denom);
	}
	
	@Override
	protected int partialArraySize(int t, int k, int v) {
		return (this.n == 0) ? this.twoStageSimpleBound(t, k, v) : this.n;
	}

	@Override
	protected void secondStage(ListCAExt remCA) {
		int k = remCA.getK();
		int t = remCA.getT();
		
		for (Interaction interaction : this.uncovInts) {
			int[] cols = interaction.getCols().getCols();
			int[] syms = interaction.getSyms().getSyms();
			Integer[] row = new Integer[k];
			for (int i = 0; i < k; i++) {
				row[i] = 0;
			}
			for (int i = 0;i < t; i++) {
				row[cols[i]] = syms[i];
			}
			
			remCA.addRow(row);
		}		
		
	}
	
	@Override
	protected void cover(List<Interaction> notCovered) {
		this.uncovInts.addAll(notCovered);
	}
	
	@Override
	protected void reset() {
		this.uncovInts = new ArrayList<Interaction>();	
	}

	public static void main(String[] args) throws IOException {
		
		int t = 0,k1 = 0,k2 = 0,v = 0;
		
		if (args.length == 4) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
		} else {
			System.err.println("Need four arguments- t, v, kStart and kEnd");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new TwoStageSimple(2));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\two-stage"
				, "two-stage-simple");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}


