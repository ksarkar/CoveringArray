package edu.asu.ca.kaushik.algorithms.derandomized;

import edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased.TrieIGEx;
import edu.asu.ca.kaushik.algorithms.structures.IGSimple;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;

public class CEIntM1 extends CondExpIteration{
	
	public CEIntM1(){
		super();
		super.name = new String("CEIntM1");
	}
	
	@Override
	public InteractionSet createIntersectionSet(int t, int k, int v) {
		return new IGSimple(t, k, v);
	}
	
	/*@Override
	public Integer[] selectNextRow(InteractionSet ig, int k, int v){
		assert(ig instanceof IGSimple);
		Integer[] newRow = super.makeStarredRow(k, v);
		while (!allColsFixed(newRow, v)){
			Interaction interaction = ig.selectInteraction(newRow);
			if (interaction != null) {
				newRow = super.updateRow(newRow, interaction);
			} else {
				fillAllCols(newRow, v);
			}			
		}
		return newRow;		
	}*/

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
