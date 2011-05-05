/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
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
package org.encog.examples.neural.xor;



import java.util.Arrays;

import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.mathutil.randomize.Randomizer;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.flat.train.prop.TrainFlatNetworkResilient;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.simple.EncogUtility;

public class XORDisplay {

	public final static int ITERATIONS = 10;

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void displayWeights(FlatNetwork network) {
		System.out.println("Weights:"
				+ Arrays.toString(network.getWeights()));
	}
	
	public static void evaluate(FlatNetwork network, MLDataSet trainingSet )
	{
		double[] output = new double[1];
		for(MLDataPair pair: trainingSet ) {
			network.compute(pair.getInput().getData(), output);
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output[0] + ",ideal=" + pair.getIdeal().getData(0));
		}
		
	}

	public static FlatNetwork createNetwork() {
		BasicNetwork network = EncogUtility
				.simpleFeedForward(2, 4, 0, 1, false);
		Randomizer randomizer = new ConsistentRandomizer(-1, 1);
		randomizer.randomize(network);
		return network.getStructure().getFlat().clone();
	}

	public static void main(String[] args) {
		
		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

		FlatNetwork network = createNetwork();

		System.out.println("Starting Weights:");
		displayWeights(network);
		evaluate(network,trainingSet);

		final TrainFlatNetworkResilient train = new TrainFlatNetworkResilient(
				network, trainingSet);

		for (int iteration = 1; iteration <= ITERATIONS; iteration++) {
			train.iteration();

			System.out.println();
			System.out.println("*** Iteration #" + iteration);
			System.out.println("Error: " + train.getError());
			evaluate(network,trainingSet);
			
			System.out.println("LastGrad:"
					+ Arrays.toString(train.getLastGradient()));
			System.out.println("Updates :"
					+ Arrays.toString(train.getUpdateValues()));

			displayWeights(network);
		}
	}
}
