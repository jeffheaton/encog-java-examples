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
package org.encog.examples.neural.forest;

import org.encog.EncogError;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.platformspecific.j2se.TrainingDialog;
import org.encog.util.simple.EncogUtility;

public class TrainNetwork {

	private ForestConfig config;
	
	public TrainNetwork(ForestConfig config) {
		this.config = config;
	}

	public void train(boolean useGUI) {
		// load, or create the neural network
		BasicNetwork network  = null;
		
		if( !config.getTrainedNetworkFile().exists() ) {
			throw new EncogError("Can't find neural network file, please generate data");
		}
		
		network = (BasicNetwork)EncogDirectoryPersistence.loadObject(config.getTrainedNetworkFile());
						
		// convert training data
		System.out.println("Converting training file to binary");

		EncogUtility.convertCSV2Binary(config.getNormalizedDataFile(),
				config.getBinaryFile(), network.getInputCount(), 
				network.getOutputCount(), false);
		BufferedMLDataSet trainingSet = new BufferedMLDataSet(
				config.getBinaryFile());


		if (useGUI) {
			TrainingDialog.trainDialog(network, trainingSet);
		} else {
			EncogUtility.trainConsole(network, trainingSet,
					config.getTrainingMinutes());
		}

		System.out.println("Training complete, saving network...");
		EncogDirectoryPersistence.saveObject(config.getTrainedNetworkFile(), network);
	}

}
