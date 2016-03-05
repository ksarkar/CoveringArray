package edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased;

import java.io.IOException;

import edu.asu.ca.kaushik.algorithms.derandomized.CondExpIteration;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class CEIntSeq extends CondExpIteration {
	
	public CEIntSeq() {
		super();
		super.name = new String("CEIntSeq");
	}

	@Override
	public InteractionSet createIntersectionSet(int t, int k, int v) {
		assert (k % t == 0);
		return new TrieIG(t, k, v);
	}
	
	@Override
	public Integer[] selectNextRow(InteractionSet ig, int k, int v){
		assert(ig instanceof TrieIG);
		Integer[] newRow = super.makeStarredRow(k, v);
		
		int t = ig.getT();
		int n = k / t;
		for (int i = 0; i < n; i++){
			Interaction interaction = ig.selectInteraction(newRow);
			newRow = super.updateRow(newRow, interaction);
		}
		return newRow;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int t = 2;
		int k = 4;
		int v = 2;
		
		CAGeneration c = new CAGeneration();
		System.out.println("CEIntSeq");
		c.setCAGenAlgo(new CEIntSeq());
		StandardCAWriter writer = new StandardCAWriter();
		writer.writeCA(c.generateCA(t,k,v), 
				"data\\out\\explicit-arrays\\" + t + "-k-" + v + "\\" + t + "-" + k + "-" + v + "CEIntSeq" + ".txt");

	}

}
