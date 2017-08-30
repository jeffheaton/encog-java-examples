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
package org.encog.examples.neural.xor;

import org.encog.Encog;
import org.encog.mathutil.randomize.XaiverRandomizer;
import org.encog.mathutil.randomize.generate.GenerateRandom;
import org.encog.mathutil.randomize.generate.MersenneTwisterGenerateRandom;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.sgd.StochasticGradientDescent;
import org.encog.neural.networks.training.propagation.sgd.update.MomentumUpdate;
import org.encog.util.Format;
import org.encog.util.simple.EncogUtility;

/**
 * XOR: This example trains a neural network using online training.  Online training is used in special cases where
 * you wish to train only a very small number of training examples per iteration.  Additionally, you would like
 * precise control over which of those training sets will actually be run.
 *
 * To do this, use the SGD trainer.  Call "process" for each supervised pair that you wish to train on.  Once
 * "process" has been called for everything that should be performed in the batch, call the "update" method.
 * The sgd.getError() will report only the error for the data set item that you just trained on.
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

        GenerateRandom rnd = new MersenneTwisterGenerateRandom(42);

		// Create a neural network, using the utility.
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 5, 0, 1, false);
        new XaiverRandomizer(42).randomize(network);

		// Create training data.
		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		
		// Train the neural network.
		final StochasticGradientDescent sgd = new StochasticGradientDescent(network, trainingSet);
        sgd.setLearningRate(0.1);
        sgd.setMomentum(0.9);
		sgd.setUpdateRule(new MomentumUpdate());

		double error = Double.POSITIVE_INFINITY;

        while(error>0.01) {
            // Train on a random element from the training set.
            // If you are not training from set, just construct your own BasicMLDataPair.
            int i = rnd.nextInt(4);
            MLDataPair pair = trainingSet.get(i);

            // Update the gradients based on this pair.
            sgd.process(pair);
            // Update the weights, based on the gradients
            sgd.update();

            // Calculate the overall error.  You might not want to do this every step on a large data set.
            error = network.calculateError(trainingSet);
            System.out.println("Step #" + sgd.getIteration() + ", Step Error: "
                + Format.formatDouble(sgd.getError(),2) + ", Global Error: "
                    + Format.formatDouble(error,2));
        }

		
		// Evaluate the neural network.
		EncogUtility.evaluate(network, trainingSet);
		
		// Shut down Encog.
		Encog.getInstance().shutdown();
	}
}
