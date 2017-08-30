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
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.benchmark.EncoderTrainingFactory;

public class ActivationFunctions {

	public static final int INPUT_OUTPUT = 25;
	public static final int HIDDEN = 5;
	public static final int SAMPLE_SIZE = 50;
	public static final double TARGET_ERROR = 0.01;


	public static double evaluate(BasicNetwork network, MLDataSet training) {
		ResilientPropagation rprop = new ResilientPropagation(network, training);
		int iterations = 0;
		int resetCount = 0;
		
		for(;;) {
			rprop.iteration();
			iterations++;
			if( rprop.getError()<TARGET_ERROR ) {
				return iterations;
			}
			
			if( iterations>1000) {
				iterations = 0;
				network.reset();
				resetCount++;
				if( resetCount>20 ) {
					return Double.NaN;
				}
			}
		}		
	}

	public static double evaluateAF(boolean tanh, double inputMin, double inputMax, double outputMin, double outputMax) {
		
		MLDataSet training = EncoderTrainingFactory.generateTraining(INPUT_OUTPUT, tanh, inputMin, inputMax, outputMin, outputMax);
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,INPUT_OUTPUT));
		network.addLayer(new BasicLayer(tanh?new ActivationTANH():new ActivationSigmoid(),true,HIDDEN));
		network.addLayer(new BasicLayer(tanh?new ActivationTANH():new ActivationSigmoid(),false,INPUT_OUTPUT));
		network.getStructure().finalizeStructure();
				
		double total = 0;
		for (int i = 0; i < SAMPLE_SIZE; i++) {
			network.reset();
			double v = evaluate(network, training);
			if( Double.isNaN(v) ) {
				return Double.NaN;
			}
			total += v;
		}
		return total / SAMPLE_SIZE;
	}

	public static void main(final String args[]) {
		System.out.println("Average iterations needed (lower is better)");
		System.out.println("Input -1 to +1, Output: -1 to +1");
		System.out.println("Sigmoid: " + evaluateAF(false,-1,1,-1,1));
		System.out.println("TANH: " + evaluateAF(true,-1,1,-1,1));
		System.out.println("Input -1 to +1, Output: 0 to +1");
		System.out.println("Sigmoid: " + evaluateAF(false,-1,1,0,1));
		System.out.println("TANH: " + evaluateAF(true,-1,1,0,1));
		System.out.println("Input 0 to +1, Output: 0 to +1");
		System.out.println("Sigmoid: " + evaluateAF(false,0,1,0,1));
		System.out.println("TANH: " + evaluateAF(true,0,1,0,1));
		System.out.println("Input 0 to +1, Output: -1 to +1");
		System.out.println("Sigmoid: " + evaluateAF(false,0,1,-1,1));
		System.out.println("TANH: "    + evaluateAF(true,0,1,-1,1));
		
		Encog.getInstance().shutdown();
	}
}
