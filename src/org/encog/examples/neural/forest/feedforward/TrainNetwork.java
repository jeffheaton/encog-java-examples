/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
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
 */
package org.encog.examples.neural.forest.feedforward;

import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.logic.FeedforwardLogic;
import org.encog.normalize.DataNormalization;
import org.encog.persist.EncogPersistedCollection;
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
		network.setLogic(new FeedforwardLogic());
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}

	public void train(boolean useGUI) {
		System.out.println("Converting training file to binary");
		EncogPersistedCollection encog = new EncogPersistedCollection(
				Constant.TRAINED_NETWORK_FILE);
		DataNormalization norm = (DataNormalization) encog
				.find(Constant.NORMALIZATION_NAME);

		EncogUtility.convertCSV2Binary(Constant.NORMALIZED_FILE,
				Constant.BINARY_FILE, norm.getNetworkInputLayerSize(), norm
						.getNetworkOutputLayerSize(), false);
		BufferedNeuralDataSet trainingSet = new BufferedNeuralDataSet(
				Constant.BINARY_FILE);

		BasicNetwork network = (BasicNetwork) encog
				.find(Constant.TRAINED_NETWORK_NAME);
		if (network == null)
			network = EncogUtility.simpleFeedForward(norm
					.getNetworkInputLayerSize(), Constant.HIDDEN_COUNT, 0, norm
					.getNetworkOutputLayerSize(), false);

		if (useGUI) {
			EncogUtility.trainDialog(network, trainingSet);
		} else {
			EncogUtility.trainConsole(network, trainingSet,
					Constant.TRAINING_MINUTES);
		}

		System.out.println("Training complete, saving network...");
		encog.add(Constant.TRAINED_NETWORK_NAME, network);
	}

}
