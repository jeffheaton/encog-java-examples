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
package org.encog.examples.neural.csv;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;
import org.encog.util.simple.TrainingSetUtil;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming. This example shows how to construct an Encog neural network to
 * predict the output from the XOR operator. This example uses resilient
 * propagation (RPROP) to train the neural network. RPROP is the best general
 * purpose supervised training method provided by Encog.
 * 
 * For the XOR example with RPROP I use 4 hidden neurons. XOR can get by on just
 * 2, but often the random numbers generated for the weights are not enough for
 * RPROP to actually find a solution. RPROP can have issues on really small
 * neural networks, but 4 neurons seems to work just fine.
 * 
 * This example reads the XOR data from a CSV file. This file should be
 * something like:
 * 
 * 0,0,0 
 * 1,0,1 
 * 0,1,1 
 * 1,1,0
 */
public class XORCSV {

	public static void main(final String args[]) {

		if (args.length == 0) {
			System.out.println("Usage:\n\nXORCSV [xor.csv]");
		} else {
			final MLDataSet trainingSet = TrainingSetUtil.loadCSVTOMemory(
					CSVFormat.ENGLISH, args[0], false, 2, 1);
			final BasicNetwork network = EncogUtility.simpleFeedForward(2, 4,
					0, 1, true);

			System.out.println();
			System.out.println("Training Network");
			EncogUtility.trainToError(network, trainingSet, 0.01);

			System.out.println();
			System.out.println("Evaluating Network");
			EncogUtility.evaluate(network, trainingSet);
		}
		Encog.getInstance().shutdown();
	}
}
