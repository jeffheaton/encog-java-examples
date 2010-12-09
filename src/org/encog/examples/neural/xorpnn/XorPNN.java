package org.encog.examples.neural.xorpnn;

import java.util.Arrays;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.training.pnn.TrainBasicPNN;
import org.encog.neural.pnn.BasicPNN;
import org.encog.neural.pnn.PNNKernelType;
import org.encog.neural.pnn.PNNOutputMode;
import org.encog.util.logging.Logging;


public class XorPNN {
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
		{ 0.0, 1.0 }, { 1.0, 1.0 } };

public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

public static void main(final String args[]) {
	
	Logging.stopConsoleLogging();
	PNNOutputMode mode = PNNOutputMode.Regression;
	
	BasicPNN network = new BasicPNN(
			PNNKernelType.Gaussian, 
			mode, 
			2,
			1);
	
	BasicNeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
	

	System.out.println("Learning...");
	
	TrainBasicPNN train = new TrainBasicPNN(network,trainingSet);
	train.learn();
	
	for (final NeuralDataPair pair : trainingSet) {
		final NeuralData output = network.compute(pair.getInput());
		System.out.println(pair.getInput().getData(0) + ","
				+ pair.getInput().getData(1) + ", actual="
				+ output.getData(0) + ",ideal="
				+ pair.getIdeal().getData(0));
	}	
	}
}
