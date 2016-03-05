package edu.asu.ca.kaushik.algorithms.randomized.lll;

import java.io.IOException;
import java.util.Date;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.outputformatter.CAWriter;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

/**
 * Improved (fast) violation (covering) checking of interactions
 * @author ksarkar1
 *
 */

public class LLL implements CAGenAlgo {

	@Override
	public void setNumSamples(int numSamples) {
		// dummy method
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		LLLCA ca = new LLLCA(t, k, v);
		ColGrIterator clGrIt = new ColGrIterator(t, k);
		boolean allCovered;
		int iteration = 0;
		int colGrNum = 0;
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			clGrIt.rewind();
			allCovered = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				ca.resetCovered();
				if (!ca.isAllCovered(cols)) {
					ca.reSample(cols);
					allCovered = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					break;
				}
				colGrNum++;
			}
			iteration++;
		} while(!allCovered);
		
		return ca;
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		// dummy method
		return null;
	}

	@Override
	public String getName() {
		return new String("LLL");
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		CAWriter writer = new StandardCAWriter();
		CAGeneration c = new CAGeneration();
		
		int t = 6;
		int k = 20;
		int v = 3;
		
		/*
		if (args.length == 3) {
			t = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
			v = Integer.parseInt(args[2]);
		} else {
			System.err.println("Need three arguments- t, k, and v");
			System.exit(1);
		}
*/
		System.out.println("LLL");
		
		System.out.println(new Date());
		c.setCAGenAlgo(new LLL());
		CA ca = c.generateCA(t, k, v);
		System.out.println(new Date());
		
		writer.writeCA(ca, "data\\out\\lll\\6-k-3\\" + t + "-" + k + "-" + v + ".txt");
		
		System.out.println("LLL Done");
	}

}
