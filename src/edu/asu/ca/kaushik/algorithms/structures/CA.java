package edu.asu.ca.kaushik.algorithms.structures;

import java.util.Iterator;
import java.util.List;

/* 
 * abstract interface for a structure representing a covering array
 */

public interface CA {
	public int getT();
	public int getK();
	public int getV();
	public int getNumRows();
	public Iterator<Integer[]> iterator();
	
	public Instrumentation getInstrumentation();
}
