/*
  * Encog Neural Network and Bot Library for Java
  * http://www.heatonresearch.com/encog/
  * http://code.google.com/p/encog-java/
  * 
  * Copyright 2008, Heaton Research Inc., and individual contributors.
  * See the copyright.txt in the distribution for a full listing of 
  * individual contributors.
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
  */
package org.encog.examples.nonlinear.tsp.genetic;

import org.encog.examples.nonlinear.tsp.City;
import org.encog.neural.NeuralNetworkError;
import org.encog.neural.genetic.GeneticAlgorithm;



/**
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * TSPGeneticAlgorithm: An implementation of the genetic algorithm that
 * attempts to solve the traveling salesman problem.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class TSPGeneticAlgorithm extends GeneticAlgorithm<TSPChromosome> {

	public TSPGeneticAlgorithm(final City cities[], final int populationSize,
			final double mutationPercent, final double percentToMate,
			final double matingPopulationPercent, final int cutLength)
			throws NeuralNetworkError {
		this.setMutationPercent(mutationPercent);
		this.setMatingPopulation(matingPopulationPercent);
		this.setPopulationSize(populationSize);
		this.setPercentToMate(percentToMate);
		this.setCutLength(cutLength);
		this.setPreventRepeat(true);

		setChromosomes(new TSPChromosome[getPopulationSize()]);
		for (int i = 0; i < getChromosomes().length; i++) {

			final TSPChromosome c = new TSPChromosome(this, cities);
			setChromosome(i, c);
		}
		sortChromosomes();
	}

}
