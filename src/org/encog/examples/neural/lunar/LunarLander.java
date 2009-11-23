package org.encog.examples.neural.lunar;

import org.encog.neural.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.genetic.TrainingSetNeuralGeneticAlgorithm;
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
		BasicNetwork model = createNetwork();
		final PilotGA train = new PilotGA(
				model, new FanInRandomizer(), 500, 0.1, 0.25);
				
		int epoch = 1;

		for(int i=0;i<50;i++) {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Score:" + train.getError());
			epoch++;
		} 

		System.out.println("\nHow the winning network landed:");
		BasicNetwork network = train.getNetwork();
		NeuralPilot pilot = new NeuralPilot(network,true);
		System.out.println(pilot.scorePilot());
	}
}
