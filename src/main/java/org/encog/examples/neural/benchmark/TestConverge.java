package org.encog.examples.neural.benchmark;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.flat.train.prop.RPROPType;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class TestConverge {

	/**
	 * The input necessary for XOR.
	 */
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for XOR.
	 */
	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	/**
	 * The main method.
	 * @param args No arguments are used.
	 */
	public static void main(final String args[]) {

		int failureCount = 0;
		
		for(int i=0;i<1000;i++) {
			// create a neural network, without using a factory
			BasicNetwork network = new BasicNetwork();
			network.addLayer(new BasicLayer(null, false, 2));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
			network.getStructure().finalizeStructure();
			network.reset();
			(new ConsistentRandomizer(0,0.5,i)).randomize(network);

			// create training data
			MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

			// train the neural network
			final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
			train.setRPROPType(RPROPType.iRPROPp);

			int epoch = 1;

			do {
				train.iteration();
				epoch++;
			} while (train.getError() > 0.01 && epoch<1000 );
			
			if( epoch>900 ) {
				failureCount++;
			}
		}
		
		System.out.println("Failed: " + failureCount);
		Encog.getInstance().shutdown();
	}
}