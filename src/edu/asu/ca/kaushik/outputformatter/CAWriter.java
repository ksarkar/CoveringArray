package edu.asu.ca.kaushik.outputformatter;

import java.io.IOException;

import edu.asu.ca.kaushik.algorithms.structures.CA;

public interface CAWriter {
	public void writeCA(CA ca, String fileName) throws IOException;
}
