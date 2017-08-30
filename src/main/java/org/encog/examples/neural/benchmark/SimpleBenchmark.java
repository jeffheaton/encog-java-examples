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
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.Format;
import org.encog.util.Stopwatch;

public class SimpleBenchmark {

	public static final int ROW_COUNT = 100000;
	public static final int INPUT_COUNT = 100;
	public static final int OUTPUT_COUNT = 100;
	public static final int HIDDEN_COUNT = 200;
	public static final int ITERATIONS = 10;

	public static long BenchmarkEncog(double[][] input, double[][] output) {
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				input[0].length));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				HIDDEN_COUNT));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false,
				output[0].length));
		network.getStructure().finalizeStructure();
		network.reset();

		MLDataSet trainingSet = new BasicMLDataSet(input, output);

		// train the neural network
		MLTrain train = new Backpropagation(network, trainingSet, 0.7, 0.7);

		Stopwatch sw = new Stopwatch();
		sw.start();
		// run epoch of learning procedure
		for (int i = 0; i < ITERATIONS; i++) {
			train.iteration();
		}
		sw.stop();

		return sw.getElapsedMilliseconds();
	}

	static double[][] Generate(int rows, int columns) {
		double[][] result = new double[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				result[i][j] = Math.random();
			}
		}

		return result;
	}

	public static void main(String[] args) {

		// initialize input and output values
		double[][] input = Generate(ROW_COUNT, INPUT_COUNT);
		double[][] output = Generate(ROW_COUNT, OUTPUT_COUNT);

		for(;;) {
			long time1 = BenchmarkEncog(input, output);

			StringBuilder line = new StringBuilder();
			line.append("Benchmark: ");
			line.append(Format.formatInteger((int)time1));
			
			System.out.println(line.toString());
		}
		
		//Encog.getInstance().shutdown();
	}
}
