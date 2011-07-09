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
		EncogUtility.trainConsole(network, trainingSet, 1);
						
		System.out.println("Final Error: " + network.calculateError(trainingSet));
		System.out.println("Training complete, saving network.");
		EncogDirectoryPersistence.saveObject(networkFile, network);
		System.out.println("Network saved.");
		
		Encog.getInstance().shutdown();

	}
}
