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
package org.encog.examples.neural.lunar;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.FanInRandomizer;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.pattern.FeedForwardPattern;

public class LunarLander {
	
	public static BasicNetwork createNetwork()
	{
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(3);
		pattern.addHiddenLayer(50);
		pattern.setOutputNeurons(1);
		pattern.setActivationFunction(new ActivationTANH());
		BasicNetwork network = (BasicNetwork)pattern.generate();
		network.reset();
		return network;
	}
	
	public static void main(String args[])
	{
		BasicNetwork network = createNetwork();
		
		MLTrain train;
		
		if( args.length>0 && args[0].equalsIgnoreCase("anneal"))
		{
			train = new NeuralSimulatedAnnealing(
					network, new PilotScore(), 10, 2, 100);
		}
		else
		{
			train = new NeuralGeneticAlgorithm(
					network, new FanInRandomizer(), 
					new PilotScore(),500, 0.1, 0.25);
		}
		
		int epoch = 1;

		for(int i=0;i<50;i++) {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Score:" + train.getError());
			epoch++;
		} 

		System.out.println("\nHow the winning network landed:");
		network = (BasicNetwork)train.getMethod();
		NeuralPilot pilot = new NeuralPilot(network,true);
		System.out.println(pilot.scorePilot());
		Encog.getInstance().shutdown();
	}
}
