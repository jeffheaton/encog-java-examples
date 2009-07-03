/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
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
 */

package org.encog.examples.neural.xorgaussian;

import org.encog.neural.activation.ActivationGaussian;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.logging.Logging;

public class XorGaussian {
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	/**
	 * XOR example, using a gaussian activation function. Gaussian is not a
	 * really good choice for XOR, this network will not perform well.
	 * 
	 * @param args
	 */
	public static void main(final String args[]) {

		Logging.stopConsoleLogging();

		final BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationGaussian(0, 1.0, 0.5),
				true, 2));
		network.addLayer(new BasicLayer(new ActivationGaussian(0, 1.0, 0.5),
				true, 3));
		network.addLayer(new BasicLayer(new ActivationGaussian(0, 1.0, 0.5),
				true, 1));
		network.getStructure().finalizeStructure();
		network.reset();

		final NeuralDataSet trainingSet = new BasicNeuralDataSet(
				XorGaussian.XOR_INPUT, XorGaussian.XOR_IDEAL);

		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);

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
