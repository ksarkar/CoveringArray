package edu.asu.ca.kaushik.algorithms.randomized;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;

/**
 * Generates Multiple CAs randomly and then chooses the best one
 * @author ksarkar1
 *
 */

public class UniformRandom extends RandCAGenAlgo {
	private Random rand;
	
	public UniformRandom(){
		super();
		super.name = new String("Uniform Random");
		this.rand = new Random(1234L);
	}
	
	@Override
	protected Integer[] createRandRow(InteractionGraph ig, int k, int v) {
		Integer[] randRow = new Integer[k];
		
		for (int i = 0; i < k; i++){
			randRow[i] = new Integer(this.rand.nextInt(v));
		}
		
		//System.out.println(ig.toString());
		//System.out.println("row:\n" + getStringRow(randRow) + "\n\n");
		
		return randRow;
	}
	
	public static void main(String[] args){
		UniformRandom ur = new UniformRandom();
		ur.setNumSamples(1000);
		CA ca = ur.generateCA(2, 3, 2);
		
		System.out.println(ca.getNumRows());
		System.out.println(ca);
	}
	
}
