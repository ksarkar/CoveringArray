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

package edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.CondExpInterSampled;
import edu.asu.ca.kaushik.algorithms.structures.InteractionSet;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class CEIntSeqEx extends CEIntSeq {

	public CEIntSeqEx() {
		super();
		super.name = "CEIntSeqEx";
	}
	
	@Override
	public InteractionSet createIntersectionSet(int t, int k, int v) {
		assert (k % t == 0);
		return new TrieIGEx(t, k, v);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
//		int t = 3;
//		int k = 30;
//		int v = 2;
//		
//		CAGeneration c = new CAGeneration();
//		System.out.println("CEIntSeqEx");
//		c.setCAGenAlgo(new CEIntSeqEx());
//		StandardCAWriter writer = new StandardCAWriter();
//		writer.writeCA(c.generateCA(t,k,v), 
//				"data\\out\\explicit-arrays\\" + t + "-k-" + v + "\\" + t + "-" + k + "-" + v + "CEIntSeqEx" + ".txt");
		
		int t = 4;
		int v = 2;
		
		int k1 = 8;
		int k2 = 100;
		int k_step = 4;
		
		//List<String> algoList = getAllDetAlgos();
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new CondExpEntries());
		algoList.add(new CEIntSeqEx());
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\CEISeq\\");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2, k_step);
		runner.setAlgos(algoList);
		runner.run();

	}

}
