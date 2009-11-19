package org.encog.examples.neural.pole;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.genetic.NeuralChromosome;

public class PoleChromosome extends NeuralChromosome {

	private PoleGA genetic;
	
	public PoleChromosome(
			final PoleGA genetic,
			final BasicNetwork network) {
		setGeneticAlgorithm(genetic.getGenetic());
		this.genetic = genetic;
		setNetwork(network);

		initGenes(network.getWeightMatrixSize());
		updateGenes();
	}
	
	@Override
	public void calculateCost() {
		// TODO Auto-generated method stub
		
	}

}
