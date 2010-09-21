package org.encog.examples.neural.benchmark;

import java.io.File;

import org.encog.engine.util.Format;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.basic.BasicNeuralDataPair;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.util.benchmark.EncogBenchmark;
import org.encog.util.benchmark.Evaluate;
import org.encog.util.benchmark.RandomTrainingFactory;

public class BinaryVsMemory {
	private static int evalMemory()
	{
		final BasicNeuralDataSet training = RandomTrainingFactory.generate(1000,
				10000, 10, 10, -1, 1);
		
		final long start = System.currentTimeMillis();
		final long stop = start + (10*Evaluate.MILIS);
		int record = 0;
		
		NeuralDataPair pair = BasicNeuralDataPair.createPair(10, 10);
		
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
		
		final BasicNeuralDataSet training = RandomTrainingFactory.generate(1000,
				10000, 10, 10, -1, 1);
		
		// create the binary file
		
		file.delete();
		BufferedNeuralDataSet training2 = new BufferedNeuralDataSet(file);
		training2.load(training);
		
		final long start = System.currentTimeMillis();
		final long stop = start + (10*Evaluate.MILIS);
		int record = 0;
		
		NeuralDataPair pair = BasicNeuralDataPair.createPair(10, 10);
		
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
	}

}
