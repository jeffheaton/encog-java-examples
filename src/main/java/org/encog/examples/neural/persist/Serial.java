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
package org.encog.examples.neural.persist;

import java.io.File;
import java.io.IOException;

import org.encog.Encog;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.obj.SerializeObject;
import org.encog.util.simple.EncogUtility;

/**
 * This example shows how to use Java serialization to store a neural network.
 * This file is not cross platform and cannot be shared with Encog C#.  Additionally
 * future versions of Encog may not be compatable with this file.
 *
 */
public class Serial {

	public static final String FILENAME = "encogexample.ser";

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public void trainAndSave() throws IOException {
		System.out.println("Training XOR network to under 1% error rate.");
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 3, 0, 1, false);
		
		// randomize consistent so that we get weights we know will converge
		(new ConsistentRandomizer(-1,1,100)).randomize(network);

		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

		// train the neural network
		final MLTrain train = new ResilientPropagation(network, trainingSet);

		do {
			train.iteration();
		} while (train.getError() > 0.009);

		double e = network.calculateError(trainingSet);
		System.out.println("Network traiined to error: " + e);

		System.out.println("Saving network");
		SerializeObject.save(new File(FILENAME), network);
	}

	public void loadAndEvaluate() throws IOException, ClassNotFoundException {
		System.out.println("Loading network");
		BasicNetwork network = (BasicNetwork) SerializeObject.load(new File(FILENAME));
		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		double e = network.calculateError(trainingSet);
		System.out
				.println("Loaded network's error is(should be same as above): "
						+ e);
	}

	public static void main(String[] args) {
		try {
			Serial program = new Serial();
			program.trainAndSave();
			program.loadAndEvaluate();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			Encog.getInstance().shutdown();
		}

	}
}
