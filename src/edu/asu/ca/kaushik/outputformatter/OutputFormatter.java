package edu.asu.ca.kaushik.outputformatter;
import java.io.IOException;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.structures.CA;


public interface OutputFormatter {
	
	void setAlgoNames(List<String> algoNames);

	void preprocess(int t, int v) throws IOException;

	void output(int k, List<CA> results) throws IOException;

}
