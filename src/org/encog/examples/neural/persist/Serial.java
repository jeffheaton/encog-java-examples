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

package org.encog.examples.neural.persist;

import java.io.IOException;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.logging.Logging;
import org.encog.util.obj.SerializeObject;

public class Serial {

	public static final String FILENAME = "encogexample.ser";

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public void trainAndSave() throws IOException {
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
		SerializeObject.save(FILENAME, network);
	}

	public void loadAndEvaluate() throws IOException, ClassNotFoundException {
		System.out.println("Loading network");
		BasicNetwork network = (BasicNetwork) SerializeObject.load(FILENAME);
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		double e = network.calculateError(trainingSet);
		System.out
				.println("Loaded network's error is(should be same as above): "
						+ e);
	}

	public static void main(String[] args) {
		Logging.stopConsoleLogging();

		try {
			Serial program = new Serial();
			program.trainAndSave();
			program.loadAndEvaluate();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}
}
