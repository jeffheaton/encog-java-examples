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
package org.encog.examples.neural.predict.market;

import java.io.File;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.simple.EncogUtility;

/**
 * Load the training data from an Encog file, produced during the
 * "build training step", and attempt to train.
 * 
 * @author jeff
 * 
 */
public class MarketTrain {

	public static void train(File dataDir) {

		final File networkFile = new File(dataDir, Config.NETWORK_FILE);
		final File trainingFile = new File(dataDir, Config.TRAINING_FILE);

		// network file
		if (!networkFile.exists()) {
			System.out.println("Can't read file: " + networkFile.getAbsolutePath());
			return;
		}
		
		BasicNetwork network = (BasicNetwork)EncogDirectoryPersistence.loadObject(networkFile);

		// training file
		if (!trainingFile.exists()) {
			System.out.println("Can't read file: " + trainingFile.getAbsolutePath());
			return;
		}
		
		final MLDataSet trainingSet = EncogUtility.loadEGB2Memory(trainingFile);

		// train the neural network
		EncogUtility.trainConsole(network, trainingSet, Config.TRAINING_MINUTES);
						
		System.out.println("Final Error: " + network.calculateError(trainingSet));
		System.out.println("Training complete, saving network.");
		EncogDirectoryPersistence.saveObject(networkFile, network);
		System.out.println("Network saved.");
		
		Encog.getInstance().shutdown();

	}
}
