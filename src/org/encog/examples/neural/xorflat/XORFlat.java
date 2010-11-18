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
package org.encog.examples.neural.xorflat;

import org.encog.Encog;
import org.encog.engine.network.flat.FlatNetwork;
import org.encog.engine.network.train.prop.TrainFlatNetworkResilient;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.util.logging.Logging;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming.  This example shows how to construct an Encog neural
 * network to predict the output from the XOR operator.  This example uses
 * a flat neural network.
 */
public class XORFlat {

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) {
		
		Logging.stopConsoleLogging();
		
		FlatNetwork network = new FlatNetwork(2,4,0,1,false);
		network.randomize();
		
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		
		
		TrainFlatNetworkResilient train = new TrainFlatNetworkResilient(network,trainingSet);
		
		//Encog.getInstance().initCL();
		//train.setTargetDevice(Encog.getInstance().getCL().getDevices().get(0));
		
		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01 );

		double[] output = new double[1];
		// test the neural network
		System.out.println("Neural Network Results:");
		for(NeuralDataPair pair: trainingSet ) {
			double[] input = pair.getInput().getData();
			network.compute(input, output);
			System.out.println(input[0] + "," + input[1] + ":" + output[0]);
		}
	}
}
