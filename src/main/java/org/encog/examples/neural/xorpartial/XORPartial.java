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
package org.encog.examples.neural.xorpartial;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.Layer;
import org.encog.util.simple.EncogUtility;


/**
 * Partial neural networks. Encog allows you to remove any neuron connection in
 * a fully connected neural network. This example creates a 2x10x10x1 neural
 * network to learn the XOR. Several connections are removed prior to training.
 */
public class XORPartial {

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) {

		BasicNetwork network = EncogUtility.simpleFeedForward(2, 10, 10, 1,
				false);
		network.reset();

		// Remove a few connections (does not really matter which, the network 
		// will train around them).
		network.enableConnection(0, 0, 0, false);
		network.enableConnection(0, 1, 3, false);
		network.enableConnection(1, 1, 1, false);
		network.enableConnection(1, 4, 4, false);
		
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);

		EncogUtility.trainToError(network, trainingSet, 0.01);

		System.out.println("Final output:");
		EncogUtility.evaluate(network, trainingSet);
		
		System.out
		.println("Training should leave hidden neuron weights at zero.");
		System.out.println("First removed neuron weight:" + network.getWeight(0, 0, 0) );
		System.out.println("Second removed neuron weight:" + network.getWeight(0, 1, 3) );
		System.out.println("Third removed neuron weight:" + network.getWeight(1, 1, 1) );
		System.out.println("Fourth removed neuron weight:" + network.getWeight(1, 4, 4) );
	}
}
