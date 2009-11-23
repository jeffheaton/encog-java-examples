package org.encog.examples.neural.lunar;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.genetic.NeuralChromosome;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.networks.training.genetic.ScoreBasedGA;
import org.encog.util.randomize.Randomizer;

public class PilotGA extends ScoreBasedGA {
	
	public PilotGA(final BasicNetwork network,
			final Randomizer randomizer,
			final int populationSize, final double mutationPercent,
			final double percentToMate) {
		super(network,randomizer,populationSize,mutationPercent,percentToMate);
	}

	@Override
	public double calculateScore(BasicNetwork network) {
		NeuralPilot pilot = new NeuralPilot(network, false);
		return pilot.scorePilot();
	}
}
