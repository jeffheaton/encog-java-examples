package org.encog.examples.neural.xorlma;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.lma.LevenbergMarquardtTraining;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming. This example shows how to construct an Encog neural network to
 * predict the output from the XOR operator a feedforward network.
 * This example trains using Levenberg Marquardt Training.
 */
public class XorLMA {

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) {
		
		Logging.stopConsoleLogging();
		
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 10, 0, 1, false);

		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		
		// train the neural network
		final Train train = new LevenbergMarquardtTraining(network, trainingSet);

		EncogUtility.trainToError(train, network, trainingSet, 0.01);
		
		System.out.println("Neural Network Results:");
		EncogUtility.evaluate(network, trainingSet);
	}
}
