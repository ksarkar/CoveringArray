package edu.asu.ca.kaushik.inputreader;

import java.io.FileNotFoundException;

import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;

public interface InputCAReader {
	public PartialCA readCA(String fileName) throws FileNotFoundException;
}
