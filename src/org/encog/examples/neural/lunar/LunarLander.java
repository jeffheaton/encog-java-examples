package org.encog.examples.neural.lunar;

import org.encog.neural.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.logging.Logging;
import org.encog.util.randomize.FanInRandomizer;

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
