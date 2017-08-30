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
package org.encog.examples.neural.benchmark;

import org.encog.Encog;
import org.encog.mathutil.randomize.FanInRandomizer;
import org.encog.mathutil.randomize.GaussianRandomizer;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.mathutil.randomize.Randomizer;
import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.benchmark.EncoderTrainingFactory;
import org.encog.util.simple.EncogUtility;

/**
 * There are several ways to init the weights in an Encog neural network. This
 * example benhmarks each of the methods that Encog offers. A simple neural
 * network is created for the XOR operator and is trained a number of times with
 * each of the randomizers. The score for each randomizer is display, the score
 * is the average amount of error improvement, higher is better.
 */
public class WeightInitialization {

	public static final int INPUT_OUTPUT = 25;
	public static final int HIDDEN = 5;
	public static final int SAMPLE_SIZE = 50;
	public static final double TARGET_ERROR = 0.01;


	public static int evaluate(Randomizer randomizer, BasicNetwork network, MLDataSet training) {
		ResilientPropagation rprop = new ResilientPropagation(network, training);
		int iterations = 0;
		
		for(;;) {
			rprop.iteration();
			iterations++;
			if( rprop.getError()<TARGET_ERROR ) {
				return iterations;
			}
			
			if( iterations>1000) {
				iterations = 0;
				randomizer.randomize(network);
			}
		}		
	}

	public static double evaluateRandomizer(Randomizer randomizer,
			BasicNetwork network, MLDataSet training) {
		double total = 0;
		for (int i = 0; i < SAMPLE_SIZE; i++) {
			randomizer.randomize(network);
			total += evaluate(randomizer, network, training);
		}
		return total / SAMPLE_SIZE;
	}

	public static void main(final String args[]) {

		RangeRandomizer rangeRandom = new RangeRandomizer(-1, 1);
		NguyenWidrowRandomizer nwrRandom = new NguyenWidrowRandomizer();
		FanInRandomizer fanRandom = new FanInRandomizer();
		GaussianRandomizer gaussianRandom = new GaussianRandomizer(0, 1);

		System.out.println("Average iterations needed (lower is better)");
		MLDataSet training = EncoderTrainingFactory.generateTraining(INPUT_OUTPUT, false, -1, 1);
		BasicNetwork network = EncogUtility.simpleFeedForward(INPUT_OUTPUT, HIDDEN, 0, INPUT_OUTPUT, true);

		System.out.println("Range random: "
				+ evaluateRandomizer(rangeRandom, network, training));
		System.out.println("Nguyen-Widrow: "
				+ evaluateRandomizer(nwrRandom, network, training));
		System.out.println("Fan-In: "
				+ evaluateRandomizer(fanRandom, network, training));
		System.out.println("Gaussian: "
				+ evaluateRandomizer(gaussianRandom, network, training));
		
		Encog.getInstance().shutdown();
	}
}
