/*
 * Encog(tm) Artificial Intelligence Framework v2.3
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.neural.lunar;

import org.encog.math.randomize.FanInRandomizer;
import org.encog.neural.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.logging.Logging;

public class LunarLander {
	
	public static BasicNetwork createNetwork()
	{
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(3);
		pattern.addHiddenLayer(50);
		pattern.setOutputNeurons(1);
		pattern.setActivationFunction(new ActivationTANH());
		BasicNetwork network = pattern.generate();
		network.reset();
		return network;
	}
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		BasicNetwork network = createNetwork();
		
		Train train;
		
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
		network = train.getNetwork();
		NeuralPilot pilot = new NeuralPilot(network,true);
		System.out.println(pilot.scorePilot());
	}
}
