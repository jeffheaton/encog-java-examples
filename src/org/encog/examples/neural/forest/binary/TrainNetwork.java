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

package org.encog.examples.neural.forest.binary;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.examples.neural.forest.feedforward.Constant;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BinaryDataLoader;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.data.buffer.codec.CSVDataCODEC;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.logic.FeedforwardLogic;
import org.encog.normalize.DataNormalization;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.csv.CSVFormat;
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

		CSVDataCODEC codec = new CSVDataCODEC(Constant.NORMALIZED_FILE,CSVFormat.ENGLISH,false,norm.getNetworkInputLayerSize(), norm
				.getNetworkOutputLayerSize());
		BinaryDataLoader loader = new BinaryDataLoader(codec);
		loader.external2Binary(Constant.BINARY_FILE);
		
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
