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

import java.io.IOException;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.CA;

/**
 * Outputs results in the form of <k,N> table as well as the explicit CA
 * 
 * @author ksarkar1
 *
 */

public class CAandTabFormatter implements OutputFormatter {
	
	private OutputFormatter tab;
	private OutputFormatter ca;
	
	public CAandTabFormatter(String rootDirName) {
		this.tab = new TableOutputFormatter(rootDirName);
		this.ca = new ExplicitCAOutputFormatter(rootDirName);
	}

	@Override
	public void setAlgoNames(List<String> algoNames) {
		this.tab.setAlgoNames(algoNames);
		this.ca.setAlgoNames(algoNames);
	}

	@Override
	public void preprocess(int t, int v) throws IOException {
		this.tab.preprocess(t, v);
		this.ca.preprocess(t, v);
	}

	@Override
	public void output(int k, List<CA> results) throws IOException {
		this.tab.output(k, results);
		this.ca.output(k, results);
	}

}
