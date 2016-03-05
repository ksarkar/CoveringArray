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
