/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
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

package org.encog.examples.neural.predict.market;

import java.io.File;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

/**
 * Load the training data from an Encog file, produced during the
 * "build training step", and attempt to train.
 * 
 * @author jeff
 * 
 */
public class MarketTrain {

	public static void main(final String args[]) {
		Logging.stopConsoleLogging();

		final File file = new File(Config.FILENAME);

		if (!file.exists()) {
			System.out.println("Can't read file: " + file.getAbsolutePath());
			return;
		}

		final EncogPersistedCollection encog = new EncogPersistedCollection(
				file);
		final NeuralDataSet trainingSet = (NeuralDataSet) encog
				.find(Config.MARKET_TRAIN);

		final BasicNetwork network = (BasicNetwork) encog
				.find(Config.MARKET_NETWORK);

		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);
		// final Train train = new Backpropagation(network, trainingSet, 0.0001,
		// 0.0);

		int epoch = 1;
		final long startTime = System.currentTimeMillis();
		int left = 0;

		do {
			final int running = (int) ((System.currentTimeMillis() - startTime) / 60000);
			left = Config.TRAINING_MINUTES - running;
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:"
					+ (train.getError() * 100.0) + "%," + " Time Left: " + left
					+ " Minutes");
			epoch++;
		} while ((left >= 0) && (train.getError() > 0.001));

		network.setDescription("Trained neural network");
		encog.add(Config.MARKET_NETWORK, network);

	}
}
