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

package edu.asu.ca.kaushik.algorithms.twostage.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.structures.Group;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.ListCAExt;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class GroupTwoStageSimple extends GroupTwoStage {
	private List<Interaction> uncovOrbs;
	
	/**
	 * One row per uncovered orbit.
	 * @param f 
	 * @param s
	 * @param gId
	 */
	public GroupTwoStageSimple(int f, int s, int gId) {
		super(f, 1, s, gId);
		this.uncovOrbs = new ArrayList<Interaction>();
		super.name = super.name + "-simple";
	}

	@Override
	protected void initSecondStage(int t, int k, int v, Group group) {
		
	}

	@Override
	protected void cover(List<Interaction> notCovered) {
		this.uncovOrbs.addAll(notCovered);
	}

	@Override
	protected void reset() {
		this.uncovOrbs = new ArrayList<Interaction>();
	}

	@Override
	protected void secondStage(ListCAExt remCA) {
		int k = remCA.getK();
		int t = remCA.getT();
		
		for (Interaction interaction : this.uncovOrbs) {
			int[] cols = interaction.getCols().getCols();
			int[] syms = interaction.getSyms().getSyms();
			Integer[] row = new Integer[k];
			for (int i = 0; i < k; i++) {
				row[i] = 0;
			}
			for (int i = 0; i < t; i++) {
				row[cols[i]] = syms[i];
			}
			
			remCA.addRow(row);
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int t = 0, k1 = 0, k2 = 0, v = 0, times = 0, f = 0, s = 0, g = -1;
		
		if (args.length == 8) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
			times = Integer.parseInt(args[4]);
			f = Integer.parseInt(args[5]);
			s = Integer.parseInt(args[6]);
			g = Integer.parseInt(args[7]);
		} else {
			System.err.println("Need 8 arguments- t, v, kStart, kEnd, times, firstStage, slack percent and group type");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new GroupTwoStageSimple(f, s, g));
		System.out.println("times = " + times);
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\group-two-stage"
				, "gorup-two-stage-simple");
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgos(algoList);
		runner.run();
	}

}
