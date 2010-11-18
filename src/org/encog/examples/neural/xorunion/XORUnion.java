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
package org.encog.examples.neural.xorunion;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.data.union.UnionNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
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
