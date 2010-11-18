/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
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
package org.encog.examples.neural.xorunbiased;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.logging.Logging;

/**
 * Neural networks in Encog can be created without bias values (weight
 * matrix only). Encog can still learn the XOR, even without biases. Usually
 * you will want bias values, however some network types require
 * unbiased layers.
 * 
 * @author jeff
 * 
 */
public class XorUnBiased {
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) {

		Logging.stopConsoleLogging();

		final BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 2));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 3));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		network.getStructure().finalizeStructure();
		network.reset();

		final NeuralDataSet trainingSet = new BasicNeuralDataSet(
				XorUnBiased.XOR_INPUT, XorUnBiased.XOR_IDEAL);

		// train the neural network
		final Train train = new Backpropagation(network, trainingSet, 0.1, 0.0);

		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while ((epoch < 500000) && (train.getError() > 0.001));

		// test the neural network
		System.out.println("Neural Network Results:");
		for (final NeuralDataPair pair : trainingSet) {
			final NeuralData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + ","
					+ pair.getInput().getData(1) + ", actual="
					+ output.getData(0) + ",ideal="
					+ pair.getIdeal().getData(0));
		}
	}
}
