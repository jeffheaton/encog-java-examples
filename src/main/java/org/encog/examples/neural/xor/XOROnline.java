/*
 * Encog(tm) Java Examples v3.3
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2014 Heaton Research, Inc.
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
package org.encog.examples.neural.xor;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.simple.EncogUtility;

/**
 * XOR: This example trains a neural network using online training.  You can use online training
 * for either resilient or back propagation training. Online training updates the weights after 
 * each training set element. This is in opposition to batch training that does not update the 
 * weights until the entire training set has been evaluated.
 * 
 * Online training is slower than batch for several reasons.  First, the weights are updated much
 * more frequently, and this takes time.  Secondly, Encog cannot run the training multi-threaded.
 * This decreases the effectiveness on multi-core machines.
 * 
 * To use online training, simply set the batch size to one.
 * 
 * train.setBatchSize(1);
 * 
 * To use a batch size of 200, simply use.
 * 
 * train.setBatchSize(200);
 * 
 * To use a batch size equal to the training size (the default), use:
 * 
 * train.setBatchSize(0);
 * 
 */
public class XOROnline {

	/**
	 * The input necessary for XOR.
	 */
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for XOR.
	 */
	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };
	
	/**
	 * The main method.
	 * @param args No arguments are used.
	 */
	public static void main(final String args[]) {
		
		// Create a neural network, using the utility.
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 2, 0, 1, false);
		network.reset();

		// Create training data.
		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		
		// Train the neural network.
		final Backpropagation train = new Backpropagation(network, trainingSet, 0.07, 0.02);
		train.setBatchSize(1);
		
		// Evaluate the neural network.
		EncogUtility.trainToError(train, 0.01);
		EncogUtility.evaluate(network, trainingSet);
		
		// Shut down Encog.
		Encog.getInstance().shutdown();
	}
}
