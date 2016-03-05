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

package edu.asu.ca.kaushik.inputreader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpIteration;
import edu.asu.ca.kaushik.algorithms.derandomized.pe.CondExpIterPE;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;
import edu.asu.ca.kaushik.algorithms.structures.LLLCA;
import edu.asu.ca.kaushik.algorithms.structures.ListCA;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.outputformatter.StandardCAWriter;
import edu.asu.ca.kaushik.wrappers.CAGeneration;

public class StandardCAReader implements InputCAReader {

	@Override
	public PartialCA readCA(String fileName) throws FileNotFoundException {
		ListCA ca = null;
		Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader(fileName)));
            s.useLocale(Locale.US);
            s.useDelimiter("\n");
           
            if (s.hasNext()) {
            	String header = s.next();
            	ca = this.parseHeader(header);
            } else {
            	System.err.println("readCA: Empty CA file");
            	System.exit(1);
            }
            
            int k = ca.getK();
            int v = ca.getT();

            while (s.hasNext()) {
                String row = s.next();
                Integer[] newRow = this.parseRow(row, k, v);
                ca.addRow(newRow);
            }
        } finally {
            s.close();
        }
        
        return ca;
	}

	private ListCA parseHeader(String header) {
		Scanner hs =  new Scanner(header);
		int t = 0;
		int k = 0;
		int v = 0;
		
		if (hs.hasNext()) {
        	hs.nextInt();
        } else {
        	System.err.println("parseHeader: no row number");
        	System.exit(1);
        }
		
		if (hs.hasNext()) {
        	t = hs.nextInt();
        } else {
        	System.err.println("parseHeader: no t");
        	System.exit(1);
        }
		
		if (hs.hasNext()) {
        	k = hs.nextInt();
        } else {
        	System.err.println("parseHeader: no k");
        	System.exit(1);
        }
		
		if (hs.hasNext()) {
        	v = hs.nextInt();
        } else {
        	System.err.println("parseHeader: no v");
        	System.exit(1);
        }
		
		hs.close();
		
		return new ListCA(t, k, v);
	}

	private Integer[] parseRow(String row, int k, int v) {
		Scanner rs = new Scanner(row);
		Integer[] newRow = new Integer[k];
		int count = 0;
		while(rs.hasNext()) {
			if (count >= k) {
				System.err.println("parseRow: row too big");
	        	System.exit(1);
			}
			int symb = rs.nextInt();
			if (symb < v) {
				newRow[count] = new Integer(symb);
				count++;
			} else {
				System.err.println("parseRow: symbol value too big");
	        	System.exit(1);
			}
		}
		
		return newRow;
	}
	
	public static void main(String[] args) throws IOException {
		StandardCAReader reader = new StandardCAReader();
		StandardCAWriter writer = new StandardCAWriter();
		
		/*
		if (args.length != 2) {
			System.out.println("Usage: java -jar CACompletion.jar <input filename> <output filename>");
			System.exit(1);
		}
		
		CAGeneration c = new CAGeneration();
		
		System.out.println("density");
		c.setCAGenAlgo(new CondExpEntries());
		writer.writeCA(c.completeCA(reader.readCA(args[0])), args[1]);
		
		*/
		
		// writer.writeCA(c.completeCA(reader.readCA("data\\in\\5-k-2\\5_17_2_90%.txt")), 
		//		"data\\in\\5-k-2\\5_17_2_density.txt");
		
		/*
		System.out.println("CondExpInt");
		c.setCAGenAlgo(new CondExpIteration());
		writer.writeCA(c.completeCA(reader.readCA("data\\in\\5-k-2\\5_17_2_90%.txt")), 
				"data\\in\\5-k-2\\5_17_2_condExpInt.txt");
		*/
		
		//String inFile = "data\\out\\lll\\6-k-3\\6-30-3-LLL-coloring.txt";
		String inFile = "data\\out\\explicit-arrays\\3-k-2\\3-50-2-int-sampled-f1000.txt";
		PartialCA ca = reader.readCA(inFile);
		System.out.println("Reading input file done.");
		LLLCA nca = new LLLCA(ca);
		ColGroup cols = null;
		String res = nca.isCompleteCA(cols) ? "A" : "NOT A";
		System.out.println("Checking done!\n");
		
		System.out.println(inFile + " IS " + res + " CA");
		
	}

}
