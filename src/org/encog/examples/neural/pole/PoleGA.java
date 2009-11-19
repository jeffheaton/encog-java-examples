package org.encog.examples.neural.pole;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.networks.training.genetic.TrainingSetNeuralChromosome;
import org.encog.util.randomize.Randomizer;

public class PoleGA extends NeuralGeneticAlgorithm {
	
	public PoleGA(final BasicNetwork network,
			final Randomizer randomizer, final NeuralDataSet training,
			final int populationSize, final double mutationPercent,
			final double percentToMate) {

		super();
		getGenetic().setMutationPercent(mutationPercent);
		getGenetic().setMatingPopulation(percentToMate * 2);
		getGenetic().setPopulationSize(populationSize);
		getGenetic().setPercentToMate(percentToMate);

		setTraining(training);

		getGenetic().setChromosomes(
				new TrainingSetNeuralChromosome[getGenetic()
						.getPopulationSize()]);
		for (int i = 0; i < getGenetic().getChromosomes().length; i++) {
			final BasicNetwork chromosomeNetwork = (BasicNetwork) network
					.clone();
			randomizer.randomize(chromosomeNetwork);

			final PoleChromosome c = 
				new PoleChromosome(
					this, chromosomeNetwork);
			getGenetic().setChromosome(i, c);
		}
		getGenetic().sortChromosomes();
		getGenetic().defineCutLength();
	}
}
