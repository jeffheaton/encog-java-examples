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

package org.encog.examples.neural.xorunion;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.data.union.UnionNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.strategy.Greedy;
import org.encog.util.logging.Logging;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming.  This example shows how to construct an Encog neural
 * network to predict the output from the XOR operator.  This example
 * uses resilient propagation (RPROP) to train the neural network.
 * RPROP is the best general purpose supervised training method provided by
 * Encog.
 * 
 * @author $Author$
 * @version $Revision$
 */
public class XORUnion {

	public static double XOR_INPUT1[][] = { { 0.0, 0.0 } };
	public static double XOR_INPUT2[][] = { { 1.0, 0.0 } };
	public static double XOR_INPUT3[][] = { { 0.0, 1.0 } };
	public static double XOR_INPUT4[][] = { { 1.0, 1.0 } };

	public static double XOR_IDEAL1[][] = { { 0.0 } };
	public static double XOR_IDEAL2[][] = { { 1.0 } };
	public static double XOR_IDEAL3[][] = { { 1.0 } };
	public static double XOR_IDEAL4[][] = { { 0.0 } };

	public static void main(final String args[]) {
		
		Logging.stopConsoleLogging();
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(1));
		network.getStructure().finalizeStructure();
		network.reset();

		NeuralDataSet trainingSet1 = new BasicNeuralDataSet(XOR_INPUT1, XOR_IDEAL1);
		NeuralDataSet trainingSet2 = new BasicNeuralDataSet(XOR_INPUT2, XOR_IDEAL2);
		NeuralDataSet trainingSet3 = new BasicNeuralDataSet(XOR_INPUT3, XOR_IDEAL3);
		NeuralDataSet trainingSet4 = new BasicNeuralDataSet(XOR_INPUT4, XOR_IDEAL4);
		
		UnionNeuralDataSet union = new UnionNeuralDataSet(2,1);
		union.addSubset(trainingSet1);
		union.addSubset(trainingSet2);
		union.addSubset(trainingSet3);
		union.addSubset(trainingSet4);
		
		// train the neural network
		final Train train = new ResilientPropagation(network, union);

		
		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01);

		// test the neural network
		System.out.println("Neural Network Results:");
		for(NeuralDataPair pair: union ) {
			final NeuralData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}
	}
}
