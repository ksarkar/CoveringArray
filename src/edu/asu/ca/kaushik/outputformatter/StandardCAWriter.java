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

package edu.asu.ca.kaushik.outputformatter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpIteration;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class StandardCAWriter implements CAWriter {

	@Override
	public void writeCA(CA ca, String fileName) throws IOException {
		int numRows = ca.getNumRows();
		int t = ca.getT();
		int k = ca.getK();
		int v = ca.getV();
		
		Iterator<Integer[]> caIterator = ca.iterator();
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, false));
			this.writeCA(out, numRows, t, k, v, caIterator);
			
		} finally {
			if (out != null){
				out.close();
			}
		}		
	}

	private void writeCA(PrintWriter out, int numRows, int t, int k, int v,
			Iterator<Integer[]> caIterator) {
		out.println(numRows + " " + t + " " + k + " " + v);
		while(caIterator.hasNext()) {
			writeRow(out, caIterator.next());
		}
		
	}

	private void writeRow(PrintWriter out, Integer[] row) {
		for(Integer i : row){
			out.print(i + " ");
		}
		out.println();
	}
	
	public static void main(String[] args) throws IOException {
		int t = 4;
		int k = 20;
		int v = 3;
		
		CAGeneration c = new CAGeneration();
		System.out.println("density");
		c.setCAGenAlgo(new CondExpEntries());
//		System.out.println("CondExpInteraction");
//		c.setCAGenAlgo(new CondExpIteration());
		StandardCAWriter writer = new StandardCAWriter();
//		writer.writeCA(c.generateCA(t,k,v), 
//				"data\\out\\explicit-arrays\\" + t + "-k-" + v + "\\" + t + "-" + k + "-" + v + "-cond-exp-int.txt");
		writer.writeCA(c.generateCA(t,k,v), 
				"data\\out\\explicit-arrays\\" + t + "-k-" + v + "\\" + t + "-" + k + "-" + v + "-density.txt");
	}

}
