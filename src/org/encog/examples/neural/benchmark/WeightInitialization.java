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

package org.encog.examples.neural.benchmark;

import org.encog.mathutil.randomize.FanInRandomizer;
import org.encog.mathutil.randomize.GaussianRandomizer;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.mathutil.randomize.Randomizer;
import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.logging.Logging;
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

	public static double evaluate(BasicNetwork network, NeuralDataSet training) {
		ResilientPropagation rprop = new ResilientPropagation(network, training);
		double startingError = network.calculateError(training);
		for (int i = 0; i < ITERATIONS; i++) {
			rprop.iteration();
		}
		double finalError = network.calculateError(training);
		return startingError - finalError;
	}

	public static double evaluateRandomizer(Randomizer randomizer,
			BasicNetwork network, NeuralDataSet training) {
		double total = 0;
		for (int i = 0; i < SAMPLE_SIZE; i++) {
			randomizer.randomize(network);
			total += evaluate(network, training);
		}
		return total / SAMPLE_SIZE;
	}

	public static void main(final String args[]) {

		Logging.stopConsoleLogging();
		RangeRandomizer rangeRandom = new RangeRandomizer(-1, 1);
		NguyenWidrowRandomizer nwrRandom = new NguyenWidrowRandomizer(-1, 1);
		FanInRandomizer fanRandom = new FanInRandomizer();
		GaussianRandomizer gaussianRandom = new GaussianRandomizer(0, 1);

		BasicNeuralDataSet training = new BasicNeuralDataSet(XOR_INPUT,
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
