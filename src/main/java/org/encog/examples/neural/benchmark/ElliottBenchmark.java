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
import org.encog.engine.network.activation.ActivationElliottSymmetric;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.Format;
import org.encog.util.Stopwatch;
import org.encog.util.benchmark.EncoderTrainingFactory;

/**
 * Benchmark shows how Elliott activation function can outperform TANH and Sigmoid.
 * Elliott typically needs more iterations, however the calculation time is much faster
 * than TANH/Sigmoid.
 */
public class ElliottBenchmark {

	public static final int INPUT_OUTPUT = 25;
	public static final int HIDDEN = 5;
	public static final int SAMPLE_SIZE = 50;
	public static final double TARGET_ERROR = 0.01;


	public static int evaluate(BasicNetwork network, MLDataSet training) {
		ResilientPropagation rprop = new ResilientPropagation(network, training);
		int iterations = 0;
		
		for(;;) {
			rprop.iteration();
			iterations++;
			if( rprop.getError()<TARGET_ERROR ) {
				return iterations;
			}
			
			if( iterations>1000) {
				iterations = 0;
				return -1;
			}
		}		
	}

	public static void evaluateNetwork(BasicNetwork network, MLDataSet training) {
		double total = 0;
		int seed = 0;
		int completed = 0;
		
		Stopwatch sw = new Stopwatch();
		
		while(completed<SAMPLE_SIZE) {
			new ConsistentRandomizer(-1,1,seed).randomize(network);
			int iter = evaluate(network, training);
			if( iter==-1 ) {
				seed++;
			} else {
				total += iter;
				seed++;
				completed++;
			}
		}
		
		sw.stop();
		
		
		System.out.println(network.getActivation(1).getClass().getSimpleName() + ": time=" 
				+ Format.formatInteger((int)sw.getElapsedMilliseconds()) 
				+ "ms, Avg Iterations: " 
				+ Format.formatInteger((int)(total / SAMPLE_SIZE)));		
		
	}
	
	public static BasicNetwork createTANH() {
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,INPUT_OUTPUT));
		network.addLayer(new BasicLayer(new ActivationTANH(),true,HIDDEN));
		network.addLayer(new BasicLayer(new ActivationTANH(),false,INPUT_OUTPUT));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	public static BasicNetwork createElliott() {
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,INPUT_OUTPUT));
		network.addLayer(new BasicLayer(new ActivationElliottSymmetric(),true,HIDDEN));
		network.addLayer(new BasicLayer(new ActivationElliottSymmetric(),false,INPUT_OUTPUT));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}

	public static void main(final String args[]) {

		System.out.println("Average iterations needed (lower is better)");
		
		MLDataSet training = EncoderTrainingFactory.generateTraining(INPUT_OUTPUT, false, -1, 1);

		evaluateNetwork(createTANH(), training);
		evaluateNetwork(createElliott(), training);
		
		Encog.getInstance().shutdown();
	}
}
