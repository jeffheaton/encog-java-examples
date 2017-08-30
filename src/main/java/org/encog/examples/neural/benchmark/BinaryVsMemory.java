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
package org.encog.examples.neural.benchmark;

import java.io.File;

import org.encog.Encog;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.util.Format;
import org.encog.util.benchmark.Evaluate;
import org.encog.util.benchmark.RandomTrainingFactory;

public class BinaryVsMemory {
	private static int evalMemory()
	{
		final BasicMLDataSet training = RandomTrainingFactory.generate(1000,
				10000, 10, 10, -1, 1);
		
		final long start = System.currentTimeMillis();
		final long stop = start + (10*Evaluate.MILIS);
		int record = 0;
		
		MLDataPair pair = BasicMLDataPair.createPair(10, 10);
		
		int iterations = 0;
		while( System.currentTimeMillis()<stop ) {
			iterations++;
			training.getRecord(record++, pair);	
			if( record>=training.getRecordCount() )
				record = 0;
		}
		
		System.out.println("In 10 seconds, the memory dataset read " + 
				Format.formatInteger( iterations) + " records.");
		
		return iterations;
	}
	
	private static int evalBinary()
	{
		File file = new File("temp.egb");
		
		final BasicMLDataSet training = RandomTrainingFactory.generate(1000,
				10000, 10, 10, -1, 1);
		
		// create the binary file
		
		file.delete();
		BufferedMLDataSet training2 = new BufferedMLDataSet(file);
		training2.load(training);
		
		final long start = System.currentTimeMillis();
		final long stop = start + (10*Evaluate.MILIS);
		int record = 0;
		
		MLDataPair pair = BasicMLDataPair.createPair(10, 10);
		
		int iterations = 0;
		while( System.currentTimeMillis()<stop ) {
			iterations++;
			training2.getRecord(record++, pair);	
			if( record>=training2.getRecordCount() )
				record = 0;
		}
		
		System.out.println("In 10 seconds, the disk(binary) dataset read " + 
				Format.formatInteger( iterations) + " records.");
		file.delete();
		return iterations;
	}
	
	public static void main(String[] args)
	{
		int memory = evalMemory();
		int binary = evalBinary();
		System.out.println( "Memory is " + Format.formatInteger(memory/binary) + " times the speed of disk.");
		Encog.getInstance().shutdown();
	}

}
