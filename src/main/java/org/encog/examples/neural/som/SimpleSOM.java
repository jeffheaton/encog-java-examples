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
package org.encog.examples.neural.som;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;

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
		// create the training set
		MLDataSet training = new BasicMLDataSet(SOM_INPUT,null);
		
		// Create the neural network.
		SOM network = new SOM(4,2);
		network.reset();
		
		BasicTrainSOM train = new BasicTrainSOM(
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
		
		MLData data1 = new BasicMLData(SOM_INPUT[0]);
		MLData data2 = new BasicMLData(SOM_INPUT[1]);
		System.out.println("Pattern 1 winner: " + network.classify(data1));
		System.out.println("Pattern 2 winner: " + network.classify(data2));
		Encog.getInstance().shutdown();
	}
}
