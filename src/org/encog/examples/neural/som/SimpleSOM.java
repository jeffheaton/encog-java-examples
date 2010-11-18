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
package org.encog.examples.neural.som;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodSingle;
import org.encog.util.logging.Logging;

/**
 * Implement a simple SOM using Encog.  It learns to recognize two patterns.
 * @author jeff
 *
 */
public class SimpleSOM {
	
	public static double SOM_INPUT[][] = { 
		{ -1.0, -1.0, 1.0, 1.0 }, 
		{ 1.0, 1.0, -1.0, -1.0 } };
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		
		// create the training set
		NeuralDataSet training = new BasicNeuralDataSet(SOM_INPUT,null);
		
		// Create the neural network.
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationLinear(),false,4));
		network.addLayer(new BasicLayer(new ActivationLinear(),false,2));
		network.getStructure().finalizeStructure();
		(new RangeRandomizer(-0.5, 0.5)).randomize(network);
		
		CompetitiveTraining train = new CompetitiveTraining(
				network,
				0.7,
				training,
				new NeighborhoodSingle());
				
		int iteration = 0;
		
		for(iteration = 0;iteration<=10;iteration++)
		{
			train.iteration();
			System.out.println("Iteration: " + iteration + ", Error:" + train.getError());
		}
		
		NeuralData data1 = new BasicNeuralData(SOM_INPUT[0]);
		NeuralData data2 = new BasicNeuralData(SOM_INPUT[1]);
		System.out.println("Pattern 1 winner: " + network.winner(data1));
		System.out.println("Pattern 2 winner: " + network.winner(data2));
	}
}
