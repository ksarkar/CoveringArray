package edu.asu.ca.kaushik.algorithms.randomized;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;


public class PropRand extends RandCAGenAlgo {
	
	private Random rand;

	public PropRand() {
		super();
		super.name = new String("Proportional Random");
		this.rand = new Random(1234L);
	}

	@Override
	protected Integer[] createRandRow(InteractionGraph ig, int k, int v) {
		ig.setRand(this.rand);
		Integer[] randRow = createUniformRandRow(k, v);
		
		Interaction interaction = ig.getUniformRandInteraction();
		int[] cols = interaction.getCols().getCols();
		int[] syms = interaction.getSyms().getSyms();
		
		for (int i = 0; i < cols.length; i++){
			randRow[cols[i]] = syms[i];
		}
		
		//System.out.println(ig.toString());
		//System.out.println(interaction.toString());
		//System.out.println("row:\n" + getStringRow(randRow) + "\n\n");
		
		return randRow;
	}
	
	private Integer[] createUniformRandRow(int k, int v) {
		Integer[] randRow = new Integer[k];
		
		for (int i = 0; i < k; i++){
			randRow[i] = new Integer(this.rand.nextInt(v));
		}
		return randRow;
	}

}
