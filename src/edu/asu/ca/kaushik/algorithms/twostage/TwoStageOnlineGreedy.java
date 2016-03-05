package edu.asu.ca.kaushik.algorithms.twostage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class TwoStageOnlineGreedy extends TwoStage{
	private int times;
	private ListCA second;
	private int t;
	private int k;
	private int v;
	
	/**
	 * 
	 * @param times how many times of v^t interactions will be left uncovered in the first stage.
	 * @param firstStage What randomized algorithm to use in stage 1. Valid values: 0 -- uniform random, 
	 * 1 -- re-sample only the last offending interaction, 2 -- re-sample all the offending interactions
	 */
	public TwoStageOnlineGreedy(int times, int firstStage) {
		super(firstStage);
		this.times = times;
		//Should initialize second here. But don't know t,k,v yet.
	}

	@Override
	public String getName() {
		return new String("TwoStageOnlineGreedy-" + this.times + "-times");
	}

	@Override
	protected int partialArraySize(int t, int k, int v) {
		//Side effect; bad style
		this.second = new ListCA(t,k,v);
		this.t = t;
		this.k = k;
		this.v = v;
		
		// function proper
		double vpowt = Math.pow(v, t);
		double n1 = Math.ceil(Math.log(CombinatoricsUtils.binomialCoefficientDouble(k,t) * vpowt
				* Math.log(vpowt/(vpowt-1))) / Math.log(vpowt / (vpowt - 1)));
		double denom = Math.log(1 - (1/vpowt));
		double n = (Math.log(this.times) + n1 * denom) / denom;
		return (int)Math.ceil(n);
	}

	@Override
	protected void cover(List<Interaction> notCovered) {
		for (Interaction inter : notCovered) {
			if (!this.greedyCover(inter)) {
				Integer[] row = getNewRow(this.k, this.v, inter);
				this.second.addRow(row);
			}
		}
	}

	private boolean greedyCover(Interaction inter) {
		Iterator<Integer[]> it = this.second.iterator();
		while(it.hasNext()) {
			Integer[] row = it.next();
			if (!this.conflicts(row, inter)) {
				this.modify(row, inter);
				return true;
			}
		}
		return false;
	}

	private boolean conflicts(Integer[] row, Interaction inter) {
		int[] cols = inter.getCols().getCols();
		int[] syms = inter.getSyms().getSyms();
		int t = cols.length;
		for (int i = 0; i < t; i++) {
			if (row[cols[i]] != this.v) { // not a starred entry
				if (row[cols[i]] != syms[i]) {
					return true;
				}
			}
		}
		return false;
	}

	private void modify(Integer[] row, Interaction inter) {
		int[] cols = inter.getCols().getCols();
		int[] syms = inter.getSyms().getSyms();
		int t = cols.length;
		for (int i = 0; i < t; i++) {
			row[cols[i]] = syms[i];
		}		
	}

	private Integer[] getNewRow(int k, int v, Interaction inter) {
		Integer[] row = new Integer[k];
		for (int i = 0; i < k; i++) {
			row[i] = v; // put a star
		}
		this.modify(row, inter);
		return row;
	}

	@Override
	protected void reset() {		
		this.second = new ListCA(this.t,this.k,this.v);		
	}

	@Override
	protected void secondStage(ListCAExt remCA) {
		remCA.join(this.second);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
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
		
		algoList.add(new TwoStageOnlineGreedy(2, 1));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\two-stage"
				, "two-stage-simple-onlineGreedy");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}
}
