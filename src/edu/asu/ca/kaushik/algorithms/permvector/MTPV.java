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

package edu.asu.ca.kaushik.algorithms.permvector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.permvector.utils.CPHF;
import edu.asu.ca.kaushik.algorithms.permvector.utils.SCPHF;
import edu.asu.ca.kaushik.algorithms.structures.ArrayCA;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGrIterator2;
import edu.asu.ca.kaushik.algorithms.structures.ColGrLexIterator;
import edu.asu.ca.kaushik.algorithms.structures.ColGroup;
import edu.asu.ca.kaushik.algorithms.structures.PartialCA;
import edu.asu.ca.kaushik.outputformatter.OutputFormatter;
import edu.asu.ca.kaushik.outputformatter.TableOutputFormatter;
import edu.asu.ca.kaushik.scripts.Runner;

public class MTPV implements CAGenAlgo {
	
	private String type; // two allowed types: Sherwood, and GenLin (for General Linear Group)
	
	public MTPV(String type) {
		this.type = type;
	}
	
	public MTPV() {
		this("Sherwood");
	}

	@Override
	public CA generateCA(int t, int k, int v) {
		assert(k >= 2 * t);
		System.out.println("CPHF size: " + ((this.type.equals("GenLin")) ? CPHF.CPHF_LLL(t, k, v) : SCPHF.SCPHF_LLL(t,k,v)));
		
		CPHF c = (this.type.equals("GenLin")) ? new CPHF(t,k,v) : new SCPHF(t,k,v);
		
		ColGrIterator clGrIt = new ColGrLexIterator(t, k);
		boolean isCPHF;
		int colGrNum;
		int iteration = 1;
		
		do {
			System.out.println("Iteration: " + iteration );
			colGrNum = 0;
			
			clGrIt.rewind();
			isCPHF = true;
			while(clGrIt.hasNext()){
				ColGroup cols = clGrIt.next();
				if (!c.containsCoveringTuple(cols)) {
					c.reSample(cols);
					isCPHF = false;
					System.out.println("uncovered interaction in coloumn group: " + colGrNum);
					break;
				}
				colGrNum++;
			}
			iteration++;
		} while(!isCPHF);
		
		System.out.println("CPHF constructed.");
		
		CA ca = c.convertToCA();
		
		/*Iterator<Integer[]> caIt = ca.iterator();
		while (caIt.hasNext()) {
			System.out.println(Arrays.toString(caIt.next()));
		}*/
		
		/*ArrayCA aca = (ArrayCA)ca;
		ColGroup cols = new ColGroup(new int[0]);
		System.out.println("\nThis " + (aca.isCompleteCA(cols) ? "is a CA" 
				: "is not a CA\n"));*/
		
		return ca;
	}

	@Override
	public CA completeCA(PartialCA partialCA) {
		// Dummy method.
		return null;
	}
	
	@Override
	public void setNumSamples(int numSamples) {
		// Dummy method.

	}

	@Override
	public String getName() {
		return "MTPV-" + this.type;
	}
	
	public static void main(String[] args) throws IOException {
		final int t, v, k1, k2;
		final boolean isGenLin;
		
		if (args.length == 5) {
			t = Integer.parseInt(args[0]);
			v = Integer.parseInt(args[1]);
			k1 = Integer.parseInt(args[2]);
			k2 = Integer.parseInt(args[3]);
			isGenLin = new String("GenLin").equalsIgnoreCase(args[4]);
		} else {
			t = v = k1 = k2 = 0;
			isGenLin = false;
			System.err.println("Need four arguments- t, v, kStart, kEnd and isGenLin [GenLin or Sherwood]");
			System.exit(1);
		}
		
		List<CAGenAlgo> algoList = new ArrayList<CAGenAlgo>();
		
		algoList.add(new MTPV(isGenLin ? "GenLin" : "Sherwood"));
		
		OutputFormatter formatter = new TableOutputFormatter("data\\out\\tables\\perm-vec"
				, "MTPV");
		
		Iterator<Integer> kIt = new Iterator<Integer>() {
			
			int high = 0;
			int k = k1;

			@Override
			public void forEachRemaining(Consumer<? super Integer> arg0) {
				// dummy
				
			}

			@Override
			public boolean hasNext() {
				while (k <= k2) {
					int size = isGenLin ? CPHF.CPHF_LLL(t,k,v) : SCPHF.SCPHF_LLL(t,k,v);
					if (size > high) {
						high = size;
						return true;
					}
					k++;
				}
				
				return false;
			}

			@Override
			public Integer next() {
				return k++;
			}

			@Override
			public void remove() {
				// dummy
				
			}
			
		};
		
		/*while(kIt.hasNext()) {
			int k = kIt.next();
			System.out.println("k = " + k + ", n = " + (isGenLin ? CPHF.CPHF_LLL(t,k,v) : SCPHF.SCPHF_LLL(t,k,v)));
		}*/
		
		Runner runner = new Runner(formatter);
		//runner.setParam(t, v, k1, k2);
		runner.setParam(t, v, kIt);
		runner.setAlgos(algoList);
		runner.run();
	}

}
