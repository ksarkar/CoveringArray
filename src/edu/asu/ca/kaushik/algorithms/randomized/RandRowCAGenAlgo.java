package edu.asu.ca.kaushik.algorithms.randomized;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;


/**
 * Generates multiple candidates rows and then chooses one according to some
 * criterion
 * 
 * @author ksarkar1
 *
 */
public abstract class RandRowCAGenAlgo implements CAGenAlgo {
	
	protected int maxNumSamples = 100;
	protected String name;

	@Override
	public void setNumSamples(int numSamples) {
		this.maxNumSamples = numSamples;

	}

	@Override
	public CA generateCA(int t, int k, int v) {
		ListCA ca = new ListCA(t, k, v);
		InteractionGraph ig = new InteractionGraph(t, k, v);
		
		//int rowNum = 0;
		while(!ig.isEmpty()) {
			Integer[] nextRow = new Integer[k];
			boolean status = selectRandRow(ig, nextRow);
			
			int[] covD = new int[k];
			int coverage = ig.deleteInteractions(nextRow, covD);
			
			ca.addRow(nextRow);		
			ca.addCoverage(coverage);
			ca.addCovDist(covD);
			ca.addStatus(status);
			//rowNum++;
			
			/*
			if (status == true) {
				int coverage = ig.deleteInteractions(nextRow);
				int[] covD = ig.getCovDist();
				
				ca.addRow(Arrays.copyOf(nextRow, nextRow.length));		
				ca.addCoverage(coverage);
				ca.addCovDist(covD);
				ca.addStatus(status);
				rowNum++;
			} else {
				System.out.println(rowNum+1);
			}
			*/
		}
		
		return ca;
	}
	
	protected abstract boolean selectRandRow(InteractionGraph ig, Integer[] nextRow);

	@Override
	public CA completeCA(PartialCA partialCA) {
		// TODO
		return null;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
}
