package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.concurrency.calc.ConcurrentCalculate;
import org.encog.engine.data.EngineIndexableSet;
import org.encog.engine.network.flat.FlatNetwork;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.simple.EncogUtility;

public class BenchmarkErrorCalc {
	
	public static final int TRAINING_SIZE = 1000;
	public static final int INPUT_SIZE = 10;
	public static final int IDEAL_SIZE = 10;
	public static final int HIDDEN1_SIZE = 50;
	public static final int HIDDEN2_SIZE = 50;
	
	
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
		double e1 = calc.calculateError();
		
		Encog.getInstance().initCL();
		calc.initCL();
		double e2 = calc.calculateError();
		
		System.out.println("CPU-Only Error:" + e1);
		System.out.println("CPU&GPU Error:" + e2);
	}
}
