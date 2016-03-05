package edu.asu.ca.kaushik.algorithms.derandomized;

import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Helper;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.algorithms.structures.SymTuple;

public class BalancedDensityUniProb extends CondExpEntries {

	public BalancedDensityUniProb() {
		super();
		super.name = new String("BalancedDensityUniProb");
	}
	
	@Override
	protected double computeExpectedCoverage(ColGroup colGr, Integer[] row, int v,
			InteractionSet ig) {
		List<Integer> starredCols = new ArrayList<Integer>();
		int t = colGr.getLen();
		int[] syms = new int[t];
		int[] cols = colGr.getCols();
		
		for (int i = 0; i < t; i++){
			int s = row[cols[i]].intValue();
			if ( s == v){
				starredCols.add(new Integer(i));
			}
			else {
				syms[i] = s;
			}
		}
		
		List<SymTuple> allBlankEntries = Helper.createAllSymTuples(starredCols.size(), v);
		
		double count = 0;
		for (SymTuple tuple : allBlankEntries) {
			int[] entries = tuple.getSyms();
			for (int i = 0; i < entries.length; i++){
				syms[starredCols.get(i).intValue()] = entries[i];
			}
			
			if (ig.contains(new Interaction(colGr, new SymTuple(syms)))){
				count = count + 1.0d;
			}
		}
		
		// time complexity measurement
		this.comp = this.comp + allBlankEntries.size();
		
		int score = ig.getNumUncovInt(colGr);
		
		return (count * score / allBlankEntries.size());
	}

}
