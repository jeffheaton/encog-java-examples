package org.encog.examples.neural.gui.maze;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.networks.training.genetic.TrainingSetNeuralChromosome;
import org.encog.util.randomize.Randomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MouseGA extends NeuralGeneticAlgorithm {


	public MouseGA(			
			final int populationSize, 
			final double mutationPercent,
			final double percentToMate,
			final EvaluateMouse eval) {

		super();
		getGenetic().setMutationPercent(mutationPercent);
		getGenetic().setMatingPopulation(percentToMate * 2);
		getGenetic().setPopulationSize(populationSize);
		getGenetic().setPercentToMate(percentToMate);

		getGenetic().setChromosomes(
				new MouseChromosome[getGenetic()
						.getPopulationSize()]);
		for (int i = 0; i < getGenetic().getChromosomes().length; i++) {
			
			NeuralMouse mouse = MouseFactory.generateMouse(null);
			MouseChromosome mc = new MouseChromosome(this,mouse,eval);
			mc.updateGenes();
			getGenetic().setChromosome(i, mc);
		}
		getGenetic().sortChromosomes();
		getGenetic().defineCutLength();
	}

}
