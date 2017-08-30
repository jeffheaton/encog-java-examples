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
import org.encog.neural.freeform.FreeformLayer;
import org.encog.neural.freeform.FreeformNetwork;
import org.encog.neural.freeform.training.FreeformBackPropagation;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.Format;
import org.encog.util.Stopwatch;

public class FreeformBenchmark {

	public static final int ROW_COUNT = 100000;
	public static final int INPUT_COUNT = 10;
	public static final int OUTPUT_COUNT = 1;
	public static final int HIDDEN_COUNT = 20;
	public static final int ITERATIONS = 10;

	public static long benchmarkBasicNetwork(double[][] input, double[][] output) {
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
	
	public static long benchmarkFreeform(double[][] input, double[][] output) {
		FreeformNetwork network = new FreeformNetwork();
		FreeformLayer inputLayer = network.createInputLayer(INPUT_COUNT);
		FreeformLayer hiddenLayer1 = network.createLayer(HIDDEN_COUNT);
		FreeformLayer outputLayer = network.createOutputLayer(OUTPUT_COUNT);
		
		network.connectLayers(inputLayer, hiddenLayer1, new ActivationSigmoid(), 1.0, false);
		network.connectLayers(hiddenLayer1, outputLayer, new ActivationSigmoid(), 1.0, false);
		
		network.reset();

		MLDataSet trainingSet = new BasicMLDataSet(input, output);

		// train the neural network
		MLTrain train = new FreeformBackPropagation(network, trainingSet, 0.7, 0.7);

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

		for(int i=0;i<10;i++) {
			long time1 = benchmarkBasicNetwork(input, output);
			long time2 = benchmarkFreeform(input, output);
			
			StringBuilder line = new StringBuilder();
			line.append("Benchmark: BasicNetwork:");
			line.append(Format.formatInteger((int)time1));
			line.append(", Freeform: ");
			line.append(Format.formatInteger((int)time2));
			
			System.out.println(line.toString());
		}
		
		Encog.getInstance().shutdown();
	}
}
