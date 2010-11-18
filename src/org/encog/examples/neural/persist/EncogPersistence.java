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
package org.encog.examples.neural.persist;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class EncogPersistence {

	public static final String FILENAME = "encogexample.eg";

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public void trainAndSave() {
		System.out.println("Training XOR network to under 1% error rate.");
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(1));
		network.getStructure().finalizeStructure();
		network.reset();

		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);

		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);

		do {
			train.iteration();
		} while (train.getError() > 0.009);

		double e = network.calculateError(trainingSet);
		System.out.println("Network traiined to error: " + e);

		System.out.println("Saving network");
		final EncogPersistedCollection encog = new EncogPersistedCollection(
				FILENAME);
		encog.create();
		encog.add("network", network);
	}

	public void loadAndEvaluate() {
		System.out.println("Loading network");

		final EncogPersistedCollection encog = new EncogPersistedCollection(
				FILENAME);
		BasicNetwork network = (BasicNetwork) encog.find("network");

		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		double e = network.calculateError(trainingSet);
		System.out
				.println("Loaded network's error is(should be same as above): "
						+ e);
	}

	public static void main(String[] args) {
		Logging.stopConsoleLogging();

		try {
			EncogPersistence program = new EncogPersistence();
			program.trainAndSave();
			program.loadAndEvaluate();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}
}
