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
package org.encog.examples.neural.benchmark;

import org.encog.mathutil.randomize.FanInRandomizer;
import org.encog.mathutil.randomize.GaussianRandomizer;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.mathutil.randomize.Randomizer;
import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.simple.EncogUtility;

/**
 * There are several ways to init the weights in an Encog neural network. This
 * example benhmarks each of the methods that Encog offers. A simple neural
 * network is created for the XOR operator and is trained a number of times with
 * each of the randomizers. The score for each randomizer is display, the score
 * is the average amount of error improvement, higher is better.
 */
public class WeightInitialization {

	public static final int SAMPLE_SIZE = 1000;
	public static final int ITERATIONS = 50;

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static double evaluate(BasicNetwork network, MLDataSet training) {
		ResilientPropagation rprop = new ResilientPropagation(network, training);
		double startingError = network.calculateError(training);
		for (int i = 0; i < ITERATIONS; i++) {
			rprop.iteration();
		}
		double finalError = network.calculateError(training);
		return startingError - finalError;
	}

	public static double evaluateRandomizer(Randomizer randomizer,
			BasicNetwork network, MLDataSet training) {
		double total = 0;
		for (int i = 0; i < SAMPLE_SIZE; i++) {
			randomizer.randomize(network);
			total += evaluate(network, training);
		}
		return total / SAMPLE_SIZE;
	}

	public static void main(final String args[]) {

		RangeRandomizer rangeRandom = new RangeRandomizer(-1, 1);
		NguyenWidrowRandomizer nwrRandom = new NguyenWidrowRandomizer(-1, 1);
		FanInRandomizer fanRandom = new FanInRandomizer();
		GaussianRandomizer gaussianRandom = new GaussianRandomizer(0, 1);

		System.out.println("Error improvement, higher is better.");
		BasicMLDataSet training = new BasicMLDataSet(XOR_INPUT,
				XOR_IDEAL);
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 10, 0, 1, true);

		System.out.println("Range random: "
				+ evaluateRandomizer(rangeRandom, network, training));
		System.out.println("Nguyen-Widrow: "
				+ evaluateRandomizer(nwrRandom, network, training));
		System.out.println("Fan-In: "
				+ evaluateRandomizer(fanRandom, network, training));
		System.out.println("Gaussian: "
				+ evaluateRandomizer(gaussianRandom, network, training));
	}
}
