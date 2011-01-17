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
package org.encog.examples.neural.forest.som;

import org.encog.mathutil.rbf.RBFEnum;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.SOMPattern;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;
import org.encog.persist.EncogPersistedCollection;

public class TrainNetwork {
	public static SOM generateNetwork(NeuralDataSet trainingSet)
	{
		SOMPattern pattern = new SOMPattern();		 	
		pattern.setInputNeurons(trainingSet.getInputSize());
		pattern.setOutputNeurons(Constant.OUTPUT_COUNT*Constant.OUTPUT_COUNT);
		SOM result = (SOM)pattern.generate();
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
		SOM network = generateNetwork(trainingSet);

		NeighborhoodRBF neighborhood = new NeighborhoodRBF(RBFEnum.Gaussian,Constant.OUTPUT_COUNT,Constant.OUTPUT_COUNT);
		BasicTrainSOM train = new BasicTrainSOM(
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
