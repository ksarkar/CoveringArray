/**
 * Copyright (C) 2013-2016 Kaushik Sarkar <ksarkar1@asu.edu>
 * 
 * This file is part of CoveringArray project.
 * 
 * CoveringArray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * CoveringArray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with CoveringArray.  If not, see <http://www.gnu.org/licenses/>.
 */ 

package edu.asu.ca.kaushik.algorithms.randomized.lll;

import java.io.IOException;
import java.util.Date;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSetIterator;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.outputformatter.CAWriter;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

/**
 * Original LLL algorithm
 * @author ksarkar1
 *
 */

public class LLLNaive implements CAGenAlgo {

	@Override
	public void setNumSamples(int numSamples) {
		// dummy method
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		LLLCA ca = new LLLCA(t, k, v);
		InteractionSetIterator iSetIt = new InteractionSetIterator(t, k, v);
		boolean allCovered;
		int iteration = 0;
		int interaction = 0;
		do {
			System.out.println("Iteration: " + iteration );
			interaction = 0;
			
			iSetIt.rewind();
			allCovered = true;
			while(iSetIt.hasNext()){
				Interaction i = iSetIt.next();
				if (!ca.isCovered(i)) {
					ColGroup cols = i.getCols();
					ca.reSample(cols);
					allCovered = false;
					System.out.println("uncovered interaction: " + interaction);
					break;
				}
				interaction++;
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

		System.out.println("LLL-naive");
		
		System.out.println(new Date());
		c.setCAGenAlgo(new LLLNaive());
		CA ca = c.generateCA(t, k, v);
		System.out.println(new Date());
		
		writer.writeCA(ca, "data\\out\\lll\\6-k-3\\" + t + "-" + k + "-" + v + "-naive.txt");
		
		System.out.println("LLL-naive Done");
	}

}
