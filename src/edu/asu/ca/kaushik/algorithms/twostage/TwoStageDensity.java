package edu.asu.ca.kaushik.algorithms.twostage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

/**
 * First stage: uniform random; second stage: density
 * @author ksarkar1
 *
 */
public class TwoStageDensity extends TwoStage {
	private int times;
	private List<Interaction> uncovInts; // to keep track of all the uncovered interactions
	
	/**
	 * 
	 * @param times how many times of v^t interactions will be left uncovered in the first stage.
	 * @param firstStage What randomized algorithm to use in stage 1. Valid values: 0 -- uniform random, 
	 * 1 -- re-sample only the last offending interaction, 2 -- re-sample all the offending interactions
	 */
	public TwoStageDensity(int times, int firstStage) {
		super(firstStage);
		this.times = times;
		this.uncovInts = new ArrayList<Interaction>();
	}

	@Override
	public String getName() {
		return new String("TwoStageDensity-" + this.times + "-times");
	}

	@Override
	protected int partialArraySize(int t, int k, int v) {
		double vpowt = Math.pow(v, t);
		double n1 = Math.ceil(Math.log(CombinatoricsUtils.binomialCoefficientDouble(k,t) * vpowt
				* Math.log(vpowt/(vpowt-1))) / Math.log(vpowt / (vpowt - 1)));
		double denom = Math.log(1 - (1/vpowt));
		double n = (Math.log(this.times) + n1 * denom) / denom;
		return (int)Math.ceil(n);
	}

	@Override
	protected void secondStage(ListCAExt remCA) {
		CondExpEntries densityAlgo = new CondExpEntries();
		InteractionGraph ig = new InteractionGraph(remCA.getT(), remCA.getK(), remCA.getV(), this.uncovInts);
		densityAlgo.constructCA(remCA, ig);
	}
	
	@Override
	protected void cover(List<Interaction> notCovered) {
		this.uncovInts.addAll(notCovered);
	}
	
	@Override
	protected void reset() {
		this.uncovInts = new ArrayList<Interaction>();	
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
		
		algoList.add(new TwoStageDensity(2, 2));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\two-stage"
				, "two-stage-simple");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
