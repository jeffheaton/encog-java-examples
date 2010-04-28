/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.neural.xorpartial;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.Layer;
import org.encog.neural.networks.synapse.Synapse;
import org.encog.util.logging.Logging;
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

		Logging.stopConsoleLogging();

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
		EncogUtility.evaluate(network, trainingSet);
	}
}
