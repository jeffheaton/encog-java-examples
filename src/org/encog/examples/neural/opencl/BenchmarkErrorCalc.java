package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.concurrency.calc.ConcurrentCalculate;
import org.encog.engine.data.EngineIndexableSet;
import org.encog.engine.network.flat.FlatNetwork;
import org.encog.engine.util.Stopwatch;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.simple.EncogUtility;

public class BenchmarkErrorCalc {
	
	public static final int TRAINING_SIZE = 100000;
	public static final int INPUT_SIZE = 300;
	public static final int IDEAL_SIZE = 1;
	public static final int HIDDEN1_SIZE = 300;
	public static final int HIDDEN2_SIZE = 300;
	
	
	public static void main(String[] args)
	{
		ConcurrentCalculate calc = ConcurrentCalculate.getInstance();
		
		
		EngineIndexableSet training = RandomTrainingFactory.generate(1000,
				TRAINING_SIZE, INPUT_SIZE, IDEAL_SIZE, -1, 1);
		BasicNetwork network = EncogUtility.simpleFeedForward(training
				.getInputSize(), HIDDEN1_SIZE, HIDDEN2_SIZE, training.getIdealSize(), true);
		network.reset();
		FlatNetwork flat = network.getStructure().getFlat();
		
		calc.setTrainingData(training);
		calc.setNetwork(flat);
		
		Stopwatch sw1 = new Stopwatch();
		sw1.start();
		double e1 = calc.calculateError();
		sw1.stop();
		
		Stopwatch sw2 = new Stopwatch();
		Encog.getInstance().initCL();
		calc.initCL();
		sw2.start();
		double e2 = calc.calculateError();
		sw2.stop();
		
		System.out.println("CPU-Only Error:" + e1 + ",time=" + sw1.getElapsedMilliseconds());
		System.out.println("CPU&GPU Error:" + e2  + ",time=" + sw2.getElapsedMilliseconds());
	}
}
