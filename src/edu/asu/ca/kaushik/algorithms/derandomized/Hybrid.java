package edu.asu.ca.kaushik.algorithms.derandomized;
import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;


public class Hybrid implements CAGenAlgo {
	//private Random rand;
	protected String name;
	
	//time complexity computation
	protected int comp;
	
	protected CondExpEntries density;
	private CondExpIteration interaction;
	
	private long cutOff;
	private int factor;
	
	
	
	
	public Hybrid() {
		super();
		//this.rand = new Random(1234L);
		this.name = new String("Hybrid");
		
		//time complexity computation
		this.comp = 0;
		
		this.density = new CondExpEntries();
		this.interaction = new CondExpIteration();
		
		this.factor = 100;
		
	}

	

	@Override
	public void setNumSamples(int numSamples) {
		//dummy method
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		ListCA ca = new ListCA(t, k, v);
		InteractionGraph ig = new InteractionGraph(t, k, v);
		long numInteraction = ig.getNumInt();
		//this.cutOff = Math.round(numInteraction * 0.20);
		this.cutOff = this.setCutoff(t, k, v, this.factor);
		
		while(!ig.isEmpty()) {
			// time complexity measurement
			this.comp = 0;
			
			Integer[] newRandRow = null;
						
			if (numInteraction <= this.cutOff) {
				// time complexity measurement
				this.interaction.setComp(0);
				newRandRow = this.interaction.selectNextRow(ig, k, v);
				this.comp = this.interaction.getComp();
			} else {
				this.density.setComp(0);
				newRandRow = this.density.selectNextRow(ig, k, v);
				this.comp = this.density.getComp();
			}
			
			int coverage = ig.deleteInteractions(newRandRow, null);
			ca.addRow(newRandRow);	
			
			numInteraction = numInteraction - coverage;
			
			// time complexity measurement
			ca.addComp(this.comp);
			ca.addCoverage(coverage);
			
			//System.out.println(ig.toString());
			//System.out.println("row:\n" + Arrays.toString(newRandRow) + "\n\n"); 
		}
		
		return ca;
	}
	
	private long setCutoff(int t, int k, int v, int factor) {
		double cutOffSq = factor * v * this.choose(k - 1, t - 1) * Math.pow(v, t - 1); // funny!
		return Math.round(Math.sqrt(cutOffSq));
	}

	private long choose(int n, int k) {
		assert((n >= 0) && (k >= 0) && (k <= n));
		
		if (k == 0) {
			return 1;
		} else if (n == k) {
			return 1;
		} else {
			return this.choose(n - 1, k - 1) + this.choose(n - 1, k);
		}
	}



	@Override
	public String getName() {
		return this.name;
	}



	@Override
	public CA completeCA(PartialCA partialCA) {
		// TODO Auto-generated method stub
		return null;
	}

}
