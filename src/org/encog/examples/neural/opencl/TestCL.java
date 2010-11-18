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
package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.network.train.prop.OpenCLTrainingProfile;
import org.encog.engine.opencl.EncogCLDevice;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.strategy.RequiredImprovementStrategy;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;


public class TestCL {

	public static final int INPUT_SIZE = 100;
	public static final int HIDDEN1 = 200;
	public static final int HIDDEN2 = 40;
	public static final int IDEAL_SIZE = 5;

	public static final int TRAINING_SIZE = 10000;
	
	public static void main(final String args[]) {
		Logging.stopConsoleLogging();
		
		NeuralDataSet trainingSet = RandomTrainingFactory.generate(1000,
				TRAINING_SIZE, INPUT_SIZE, IDEAL_SIZE, -1, 1);
		
		BasicNetwork network = EncogUtility.simpleFeedForward(INPUT_SIZE, HIDDEN1, HIDDEN2, IDEAL_SIZE, true);
		network.reset();
		
		Encog.getInstance().initCL();

		
		// train the neural network
		EncogCLDevice device = Encog.getInstance().getCL().chooseDevice();
		OpenCLTrainingProfile profile = new OpenCLTrainingProfile(device);

		System.out.println("OpenCL device used: " + profile.getDevice().toString());

		final Propagation train = new ResilientPropagation(network, trainingSet, profile);
		
		EncogUtility.trainToError(train, network, trainingSet, 0.01);

	}
}
