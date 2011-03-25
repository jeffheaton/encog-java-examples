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
package org.encog.examples.neural.forest.feedforward;

import java.io.File;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.simple.EncogUtility;

public class TrainNetwork {

	public static BasicNetwork generateNetwork(NeuralDataSet trainingSet) {
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				trainingSet.getInputSize()));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				Constant.HIDDEN_COUNT));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				trainingSet.getIdealSize()));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}

	public void train(boolean useGUI) {
		BufferedNeuralDataSet dataFile = new BufferedNeuralDataSet(new File(Constant.BINARY_FILE));
        NeuralDataSet trainingSet = dataFile.loadToMemory();
        int inputSize = trainingSet.getInputSize();
        int idealSize = trainingSet.getIdealSize();

        BasicNetwork network = EncogUtility.simpleFeedForward(inputSize, Constant.HIDDEN_COUNT, 0, idealSize, true);

        if (useGUI)
        {
            EncogUtility.trainDialog(network, trainingSet);
        }
        else
        {
            EncogUtility.trainConsole(network, trainingSet, Constant.TRAINING_MINUTES);
        }
        
        EncogDirectoryPersistence.saveObject(new File(Constant.TRAINED_NETWORK_FILE), network);

        System.out.println("Training complete, saving network...");
	}

}
