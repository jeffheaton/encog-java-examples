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

package org.encog.examples.neural.forest.som;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodGaussianMulti;
import org.encog.neural.pattern.SOMPattern;
import org.encog.persist.EncogPersistedCollection;

public class TrainNetwork {
	public static BasicNetwork generateNetwork(NeuralDataSet trainingSet)
	{
		SOMPattern pattern = new SOMPattern();		 	
		pattern.setInputNeurons(trainingSet.getInputSize());
		pattern.setOutputNeurons(Constant.OUTPUT_COUNT*Constant.OUTPUT_COUNT);
		BasicNetwork result = pattern.generate();
		result.reset();
		return result;
	}
	
	public void train(boolean useGUI)
	{
		System.out.println("Converting training file to binary...");
		EncogPersistedCollection encog = new EncogPersistedCollection(Constant.TRAINED_NETWORK_FILE);
		
		//EncogUtility.convertCSV2Binary(Constant.NORMALIZED_FILE, Constant.BINARY_FILE, norm.getNetworkInputLayerSize(),norm.getNetworkOutputLayerSize(), false);
		BufferedNeuralDataSet trainingSet = new BufferedNeuralDataSet(Constant.BINARY_FILE);
		
		System.out.println("Beginning training...");
		BasicNetwork network = generateNetwork(trainingSet);

		NeighborhoodGaussianMulti neighborhood = new NeighborhoodGaussianMulti(Constant.OUTPUT_COUNT,Constant.OUTPUT_COUNT);
		CompetitiveTraining train = new CompetitiveTraining(
				network,
				0.1,
				trainingSet,
				neighborhood);
		train.setForceWinner(false);
				
		int iteration = 0;

		train.setAutoDecay(10, 0.00000003, 0.00000001, 6, 2);
		for(iteration = 0;iteration<10;iteration++)
		{
			train.iteration();
			train.autoDecay();
			System.out.println("Iteration: " + iteration + ", Error:" + train.getError()+"," + train.toString());
		}

		System.out.println("Training complete, saving network...");
		encog.add(Constant.TRAINED_NETWORK_NAME, network);
	}

}
