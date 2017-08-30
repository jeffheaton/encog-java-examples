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
package org.encog.examples.neural.benchmark;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.RPROPType;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class TestConverge {

	/**
	 * The input necessary for XOR.
	 */
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for XOR.
	 */
	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };
	
	public static int COUNT = 1000;

	/**
	 * The main method.
	 * @param args No arguments are used.
	 */
	public static void main(final String args[]) {

		int failureCount = 0;
		
		for(int i=0;i<1000;i++) {
			// create a neural network, without using a factory
			BasicNetwork network = new BasicNetwork();
			network.addLayer(new BasicLayer(null, false, 2));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
			network.getStructure().finalizeStructure();
			network.reset();
			(new ConsistentRandomizer(0,0.5,i)).randomize(network);

			// create training data
			MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

			// train the neural network
			final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
			train.setRPROPType(RPROPType.iRPROPp);

			int epoch = 1;

			do {
				train.iteration();
				epoch++;
			} while (train.getError() > 0.01 && epoch<COUNT );
			
			if( epoch>900 ) {
				failureCount++;
			}
		}
		
		System.out.println("Failed to converge: " + failureCount + "/" + COUNT);
		Encog.getInstance().shutdown();
	}
}
