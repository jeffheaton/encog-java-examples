/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.examples.neural.bam;

import org.encog.Encog;
import org.encog.ml.data.specific.BiPolarNeuralData;
import org.encog.neural.bam.BAM;
import org.encog.neural.networks.NeuralDataMapping;

/**
 * Simple class to recognize some patterns with a Bidirectional
 * Associative Memory (BAM) Neural Network.
 * This is based on a an example by Karsten Kutza, 
 * written in C on 1996-01-24.
 * http://www.neural-networks-at-your-fingertips.com/bam.html
 * 
 * I translated it to Java and adapted it to use Encog for neural
 * network processing.  I mainly kept the patterns from the 
 * original example.
 *
 */
public class BidirectionalAssociativeMemory {

	public static final String[] NAMES = { "TINA ", "ANTJE", "LISA " };

	public static final String[] NAMES2 = { "TINE ", "ANNJE", "RITA " };

	public static final String[] PHONES = { "6843726", "8034673", "7260915" };
	
	public static final int IN_CHARS	= 5;
	public static final int OUT_CHARS   = 7;

	public static final int BITS_PER_CHAR = 6;
	public static final char FIRST_CHAR = ' ';

	public static final int INPUT_NEURONS = (IN_CHARS  * BITS_PER_CHAR);
	public static final int OUTPUT_NEURONS = (OUT_CHARS * BITS_PER_CHAR);
           
	public BiPolarNeuralData stringToBipolar(String str)
	{
		BiPolarNeuralData result = new BiPolarNeuralData(str.length()*BITS_PER_CHAR);
		int currentIndex = 0;
		for(int i=0;i<str.length();i++)
		{
			char ch = Character.toUpperCase(str.charAt(i));
			int idx = ch-FIRST_CHAR;
			
			int place = 1;
			for( int j=0;j<BITS_PER_CHAR;j++)
			{
				boolean value = (idx&place)>0;
				result.setData(currentIndex++,value);
				place*=2;
			}
			
		}
		return result;
	}
	
	public String bipolalToString(BiPolarNeuralData data)
	{
		StringBuilder result = new StringBuilder();
		
		int j,a,p;
		   
		  for (int i=0; i<(data.size() / BITS_PER_CHAR); i++) {
		    a = 0;
		    p = 1;
		    for (j=0; j<BITS_PER_CHAR; j++) {
		    	if( data.getBoolean(i*BITS_PER_CHAR+j) )
		    		a+=p;

		      p *= 2;  
		    }
		    result.append((char)(a + FIRST_CHAR));
		  }

		
		return result.toString();
	}
	
	public BiPolarNeuralData randomBiPolar(int size)
	{
		BiPolarNeuralData result = new BiPolarNeuralData(size);
		for(int i=0;i<size;i++)
		{
			if(Math.random()>0.5)
				result.setData(i,-1);
			else
				result.setData(i,1);
		}
		return result;
	}
	
	public String mappingToString(NeuralDataMapping mapping)
	{
		StringBuilder result = new StringBuilder();
		result.append( bipolalToString((BiPolarNeuralData)mapping.getFrom()) );
		result.append(" -> ");
		result.append( bipolalToString((BiPolarNeuralData)mapping.getTo()) );
		return result.toString();
	}
	
	public void runBAM(BAM logic, NeuralDataMapping data )
	{
		StringBuilder line = new StringBuilder();
		line.append(mappingToString(data));
		logic.compute(data);
		line.append("  |  ");
		line.append(mappingToString(data));
		System.out.println(line.toString());
	}
	
	public void run()
	{		
		BAM logic = new BAM(INPUT_NEURONS, OUTPUT_NEURONS);
		
		// train
		for(int i=0;i<NAMES.length;i++)
		{
			logic.addPattern(
					stringToBipolar(NAMES[i]), 
					stringToBipolar(PHONES[i]));
		}
		
		// test
		for(int i=0;i<NAMES.length;i++)
		{	
			NeuralDataMapping data = new NeuralDataMapping(
					stringToBipolar(NAMES[i]),
					randomBiPolar(OUT_CHARS*BITS_PER_CHAR));	
			runBAM(logic, data);
		}
		
		System.out.println();
		
		for(int i=0;i<PHONES.length;i++)
		{	
			NeuralDataMapping data = new NeuralDataMapping(
					stringToBipolar(PHONES[i]),
					randomBiPolar(IN_CHARS*BITS_PER_CHAR) );	
			runBAM(logic, data);
		}
		
		System.out.println();
		
		for(int i=0;i<NAMES.length;i++)
		{	
			NeuralDataMapping data = new NeuralDataMapping(
					stringToBipolar(NAMES2[i]),
					randomBiPolar(OUT_CHARS*BITS_PER_CHAR));	
			runBAM(logic, data);
		}
		
		
	}

	public static void main(String[] args) {
		BidirectionalAssociativeMemory program = new BidirectionalAssociativeMemory();
		program.run();
		Encog.getInstance().shutdown();
	}
}
