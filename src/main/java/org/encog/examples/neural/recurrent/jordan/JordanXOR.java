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
package org.encog.examples.neural.recurrent.jordan;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.examples.neural.util.TemporalXOR;
import org.encog.mathutil.error.ErrorCalculation;
import org.encog.mathutil.error.ErrorCalculationMode;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.CalculateScore;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.JordanPattern;

/**
 * Implement an Jordan style neural network with Encog. This network attempts to
 * predict the next value in an XOR sequence, taken one at a time. A regular
 * feedforward network would fail using a single input neuron for this task. The
 * internal state stored by an Jordan neural network allows better performance.
 * Jordan networks are typically used for temporal neural networks. A Jordan
 * network has a single context layer connected to the output layer.
 * 
 */
public class JordanXOR {

	static BasicNetwork createJordanNetwork() {
		// construct an Jordan type network
		JordanPattern pattern = new JordanPattern();
		pattern.setActivationFunction(new ActivationSigmoid());
		pattern.setInputNeurons(1);
		pattern.addHiddenLayer(6);
		pattern.setOutputNeurons(1);
		return (BasicNetwork)pattern.generate();
	}

	static BasicNetwork createFeedforwardNetwork() {
		// construct a feedforward type network
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setActivationFunction(new ActivationSigmoid());
		pattern.setInputNeurons(1);
		pattern.addHiddenLayer(2);
		pattern.setOutputNeurons(1);
		return (BasicNetwork)pattern.generate();
	}

	public static void main(final String args[]) {
		
		ErrorCalculation.setMode(ErrorCalculationMode.RMS);
		
		final TemporalXOR temp = new TemporalXOR();
		final MLDataSet trainingSet = temp.generate(120);

		final BasicNetwork jordanNetwork = JordanXOR.createJordanNetwork();
		final BasicNetwork feedforwardNetwork = JordanXOR
				.createFeedforwardNetwork();

		final double jordanError = JordanXOR.trainNetwork("Jordan", jordanNetwork,
				trainingSet);
		final double feedforwardError = JordanXOR.trainNetwork("Feedforward",
				feedforwardNetwork, trainingSet);		

		System.out.println("Best error rate with Jordan Network: " + jordanError);
		System.out.println("Best error rate with Feedforward Network: "
				+ feedforwardError);
		System.out
				.println("Jordan should be able to get into the 30% range,\nfeedforward should not go below 50%.\nThe recurrent Elment net can learn better in this case.");
		System.out
				.println("If your results are not as good, try rerunning, or perhaps training longer.");
	}

	public static double trainNetwork(final String what,
			final BasicNetwork network, final MLDataSet trainingSet) {
		// train the neural network
		CalculateScore score = new TrainingSetScore(trainingSet);
		final MLTrain trainAlt = new NeuralSimulatedAnnealing(
				network, score, 10, 2, 100);

		final MLTrain trainMain = new Backpropagation(network, trainingSet,0.000001, 0.0);

		((Propagation)trainMain).setNumThreads(1);
		final StopTrainingStrategy stop = new StopTrainingStrategy();
		trainMain.addStrategy(new Greedy());
		trainMain.addStrategy(new HybridStrategy(trainAlt));
		trainMain.addStrategy(stop);

		int epoch = 0;
		while (!stop.shouldStop()) {
			trainMain.iteration();
			System.out.println("Training " + what + ", Epoch #" + epoch
					+ " Error:" + trainMain.getError());
			epoch++;
		}
		return trainMain.getError();
	}
}
