package edu.asu.ca.kaushik.algorithms.twostage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.algorithms.structures.graph.ConflictGraph;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class TwoStageOnlineColoring extends TwoStage {
	private int times;
	ConflictGraph graph;
	
	/**
	 * 
	 * @param times how many times of v^t interactions will be left uncovered in the first stage.
	 * @param firstStage What randomized algorithm to use in stage 1. Valid values: 0 -- uniform random, 
	 * 1 -- re-sample only the last offending interaction, 2 -- re-sample all the offending interactions
	 */
	public TwoStageOnlineColoring(int times, int firstStage) {
		super(firstStage);
		this.times = times;
		this.graph = new ConflictGraph();
	}

	@Override
	public String getName() {
		return new String("TwoStageOnlineColoring-" + this.times + "-times");
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
	protected void cover(List<Interaction> notCovered) {
		for (Interaction inter : notCovered) {
			this.graph.addVertexAndGreedyColor(inter);
		}
	}
	
	@Override
	protected void reset() {
		this.graph = new ConflictGraph();
	}

	@Override
	protected void secondStage(ListCAExt remCA) {
		this.graph.convertColoringToCA(remCA);
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
		
		algoList.add(new TwoStageOnlineColoring(3, 2));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\two-stage"
				, "two-stage-simple");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
