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
package org.encog.examples.neural.xorneat;

import org.encog.engine.network.activation.ActivationStep;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.training.NEATTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.CalculateScore;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.util.logging.Logging;

/**
 * XOR-NEAT: This example solves the classic XOR operator neural
 * network problem.  However, it uses a NEAT evolving network.
 * 
 * @author $Author$
 * @version $Revision$
 */
public class XorNEAT {
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) {
		
		Logging.stopConsoleLogging();

		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		
		CalculateScore score = new TrainingSetScore(trainingSet);
		// train the neural network
		ActivationStep step = new ActivationStep();
		step.setCenter(0.5);
		
		final NEATTraining train = new NEATTraining(
				score, 2, 1, 1000);
		train.setOutputActivationFunction(step);

		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while ((train.getError() > 0.001));

		NEATNetwork network = train.getNetwork();
		network.clearContext();
		// test the neural network
		System.out.println("Neural Network Results:");
		for(NeuralDataPair pair: trainingSet ) {
			final NeuralData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}
	}
}
