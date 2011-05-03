/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
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

/*		Logging.stopConsoleLogging();

		BasicNetwork network = EncogUtility.simpleFeedForward(2, 10, 10, 1,
				false);
		network.reset();

		// obtain some of the synapses that we wish to remove connections from
		Layer inputLayer = network.getLayer(BasicNetwork.TAG_INPUT);
		Synapse inputToHidden1 = inputLayer.getNext().get(0);
		Synapse hidden1ToHidden2 = inputToHidden1.getToLayer().getNext().get(0);
		Synapse hidden2ToOutput = inputToHidden1.getToLayer().getNext().get(0);

		// remove the connection from input neuron 0 to hidden1 neuron 1.
		network.enableConnection(inputToHidden1, 0, 1, false);

		// remove the connection from hidden1 neuron 2 to hidden2 neuron 3.
		network.enableConnection(hidden1ToHidden2, 2, 3, false);

		// remove the connection from hidden2 neuron 3 to output neuron 4.
		network.enableConnection(hidden2ToOutput, 3, 4, false);

		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);

		EncogUtility.trainToError(network, trainingSet, 0.01);

		System.out
				.println("Training should leave hidden neuron weights at zero.");
		System.out.println("First removed neuron weight:"
				+ inputToHidden1.getMatrix().get(0, 1));
		System.out.println("First removed neuron weight:"
				+ hidden1ToHidden2.getMatrix().get(2, 3));
		System.out.println("First removed neuron weight:"
				+ hidden2ToOutput.getMatrix().get(3, 4));
		System.out.println("Final output:");
		EncogUtility.evaluate(network, trainingSet);*/
	}
}
