package org.encog.examples.neural.xorgaussian;

import org.encog.neural.activation.ActivationGaussian;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.backpropagation.Backpropagation;
import org.encog.util.Logging;

public class XorGaussian {
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
		{ 0.0, 1.0 }, { 1.0, 1.0 } };

public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

public static void main(final String args[]) {
	
	Logging.stopConsoleLogging();
	
	BasicNetwork network = new BasicNetwork();
	network.addLayer(new BasicLayer(new ActivationGaussian(0,1.0,0.5),2));
	network.addLayer(new BasicLayer(new ActivationGaussian(0,1.0,0.5),3));
	network.addLayer(new BasicLayer(new ActivationGaussian(0,1.0,0.5),1));
	network.getStructure().finalizeStructure();
	network.reset();

	NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
	
	// train the neural network
	final Train train = new Backpropagation(network, trainingSet,
			0.000001, 0.0);

	int epoch = 1;

	do {
		train.iteration();
		System.out
				.println("Epoch #" + epoch + " Error:" + train.getError());
		epoch++;
	} while ((epoch < 500000) && (train.getError() > 0.001));

	// test the neural network
	System.out.println("Neural Network Results:");
	for(NeuralDataPair pair: trainingSet ) {
		final NeuralData output = network.compute(pair.getInput());
		System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
				+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
	}
}
}
