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
package org.encog.examples.neural.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.flat.train.prop.TrainFlatNetworkBackPropagation;
import org.encog.util.Format;
import org.encog.util.Stopwatch;

public class AverageBenchmark {

	public static final int ROW_COUNT = 100000;
	public static final int INPUT_COUNT = 10;
	public static final int OUTPUT_COUNT = 1;
	public static final int HIDDEN_COUNT = 20;
	public static final int ITERATIONS = 10;
	public static final int AVG_COUNT = 20;

	public static long benchmarkEncogFlat(double[][] input, double[][] output) {
		FlatNetwork network = new FlatNetwork(input[0].length, HIDDEN_COUNT, 0,
				output[0].length, false);
		network.randomize();
		BasicMLDataSet trainingSet = new BasicMLDataSet(input, output);

		TrainFlatNetworkBackPropagation train = new TrainFlatNetworkBackPropagation(
				network, trainingSet, 0.7, 0.7);

		double[] a = new double[2];
		double[] b = new double[1];

		Stopwatch sw = new Stopwatch();
		sw.start();
		// run epoch of learning procedure
		for (int i = 0; i < ITERATIONS; i++) {
			train.iteration();
		}
		sw.stop();

		return sw.getElapsedMilliseconds();
	}

	static double[][] generate(int rows, int columns) {
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
		double[][] input = generate(ROW_COUNT, INPUT_COUNT);
		double[][] output = generate(ROW_COUNT, OUTPUT_COUNT);
		List<Long> previous = new ArrayList<Long>();

		for(;;) {
			long time = benchmarkEncogFlat(input, output);
			previous.add(time);
			
			StringBuilder line = new StringBuilder();
			line.append("Time: ");
			line.append(Format.formatInteger((int)time));
						
			if( previous.size()<=AVG_COUNT ) {
				line.append(", no average yet");
			} else {
				previous.remove(0);
				long avg = 0;
				for(long l: previous) {
					avg+=l;
				}
				line.append(", average over last ");
				line.append(previous.size());
				line.append(" is ");
				line.append(avg/previous.size());
			}
						
			System.out.println(line.toString());
		}
	}
}
