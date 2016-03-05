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
import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;

/**
 * Abstract class for randomized CA generation algorithms.
 * Each concrete randomized CA generation algorithm must subclass this
 * and implement its own createRandRow()
 * 
 * @author ksarkar1
 *
 */

public abstract class RandCAGenAlgo implements CAGenAlgo {
	protected int numSamples = 10000;
	protected String name;

	@Override
	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;

	}

	@Override
	public CA generateCA(int t, int k, int v) {
		CA ca = new ListCA(t, k, v);
		int min = Integer.MAX_VALUE;
		
		for (int i = 0; i < this.numSamples; i++){
			CA randCa = generateRandCA(t, k, v);
			if (randCa.getNumRows() < min){
				min = randCa.getNumRows();
				ca = randCa;
			}
			this.printMessage(i);
		}
		
		return ca;	
	}
	
	@Override
	public CA completeCA(PartialCA partialCA) {
		// TODO
		return null;
	}
	
	@Override
	public String getName(){
		return this.name;
	}

	private void printMessage(int i) {
		if (i == Math.floor(this.numSamples / 4)){
			System.out.println("25% work done...");
		}
		else if (i == Math.floor(this.numSamples / 2)){
			System.out.println("50% work done...");
		}
		else if (i == Math.floor(3 * this.numSamples / 4)){
			System.out.println("75% work done...");
		}
		
	}

	public CA generateRandCA(int t, int k, int v) {
		ListCA ca = new ListCA(t, k, v);
		InteractionGraph ig = new InteractionGraph(t, k, v);
		
		while(!ig.isEmpty()) {
			Integer[] newRandRow = createRandRow(ig, k, v);
			ig.deleteInteractions(newRandRow, null);
			ca.addRow(newRandRow);		
		}
		
		return ca;
	}
	
	protected String getStringRow(Integer[] newRow) {
		String s = new String();
		s = s + "[ ";
		for (Integer i : newRow) {
			s = s + i.toString() + ", ";
		}
		s = s + "]";
		return s;
	}

	abstract protected Integer[] createRandRow(InteractionGraph ig, int k, int v);
	

}
