package edu.asu.ca.kaushik.algorithms.derandomized;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;


public class CondExpIteration extends CondExpEntries {
	
	public CondExpIteration(){
		super();
		super.name = new String("Cond Exp Iteration");
	}
	
	@Override
	public Integer[] selectNextRow(InteractionSet ig, int k, int v){
		assert(ig instanceof InteractionGraph);
		InteractionSet igCopy = new InteractionGraph((InteractionGraph)ig);
		Integer[] newRow = super.makeStarredRow(k, v);
		
		while (!allInteractionsSeen(newRow, v, igCopy)){
			Interaction interaction = igCopy.selectInteraction(newRow);
			
			// time complexity measurement
			super.comp = super.comp + igCopy.getComp();
			
			newRow = super.updateRow(newRow, interaction);
			
			igCopy.deleteFullyDeterminedInteractions(newRow);
			igCopy.deletIncompatibleInteractions(interaction);	
		}
		
		fillAllCols(newRow, v);
		return newRow;		
	}

	private static void fillAllCols(Integer[] newRow, int v) {
		for (int i = 0; i < newRow.length; i++) {
			if (newRow[i].intValue() == v){
				newRow[i] = new Integer(0);
			}
		}
	}

	private static boolean allInteractionsSeen(Integer[] newRow, int v,
			InteractionSet igCopy) {
		return igCopy.isEmpty() || allColsFixedP(newRow, v);
	}

	private static boolean allColsFixedP(Integer[] newRow, int v) {
		for (Integer i : newRow) {
			if (i.intValue() == v){
				return false;
			}
		}
		return true;
	}

}
