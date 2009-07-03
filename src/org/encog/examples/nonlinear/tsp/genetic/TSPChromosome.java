/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
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
import org.encog.solve.genetic.Chromosome;



/**
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * TSPChromosome: A chromosome that is used to attempt to solve the 
 * traveling salesman problem.  A chromosome is a list of cities.
 */
public class TSPChromosome extends Chromosome<Integer> {

	protected City cities[];

	TSPChromosome(final TSPGeneticAlgorithm owner, final City cities[]) {
		this.setGeneticAlgorithm(owner);
		this.cities = cities;

		final Integer genes[] = new Integer[this.cities.length];
		final boolean taken[] = new boolean[cities.length];

		for (int i = 0; i < genes.length; i++) {
			taken[i] = false;
		}
		for (int i = 0; i < genes.length - 1; i++) {
			int icandidate;
			do {
				icandidate = (int) (Math.random() * genes.length);
			} while (taken[icandidate]);
			genes[i] = icandidate;
			taken[icandidate] = true;
			if (i == genes.length - 2) {
				icandidate = 0;
				while (taken[icandidate]) {
					icandidate++;
				}
				genes[i + 1] = icandidate;
			}
		}
		setGenes(genes);
		calculateCost();

	}

	@Override
	public void calculateCost() throws NeuralNetworkError {
		double cost = 0.0;
		for (int i = 0; i < this.cities.length - 1; i++) {
			final double dist = this.cities[getGene(i)]
					.proximity(this.cities[getGene(i + 1)]);
			cost += dist;
		}
		setCost(cost);

	}

	@Override
	public void mutate() {
		final int length = this.getGenes().length;
		final int iswap1 = (int) (Math.random() * length);
		final int iswap2 = (int) (Math.random() * length);
		final Integer temp = getGene(iswap1);
		setGene(iswap1, getGene(iswap2));
		setGene(iswap2, temp);
	}

}
