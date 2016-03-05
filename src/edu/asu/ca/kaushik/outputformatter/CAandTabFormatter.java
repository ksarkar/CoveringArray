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
