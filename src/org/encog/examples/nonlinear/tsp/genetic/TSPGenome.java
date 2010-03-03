/*
 * Encog(tm) Examples v2.4
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

package org.encog.examples.nonlinear.tsp.genetic;

import org.encog.examples.nonlinear.tsp.City;
import org.encog.neural.NeuralNetworkError;
import org.encog.solve.genetic.BasicGenome;
import org.encog.solve.genetic.Chromosome;
import org.encog.solve.genetic.IntegerGene;
import org.encog.solve.genetic.genes.Gene;



/**
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * TSPChromosome: A chromosome that is used to attempt to solve the 
 * traveling salesman problem.  A chromosome is a list of cities.
 */
public class TSPGenome extends BasicGenome {

	private City cities[];
	private Chromosome pathChromosome;

	public TSPGenome(final TSPGeneticAlgorithm owner, final City cities[]) {
		super(owner);

		this.cities = cities;

		final int genes[] = new int[this.cities.length];
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
		
		this.pathChromosome = new Chromosome();
		this.getChromosomes().add(this.pathChromosome);
		
		for(int i=0;i<genes.length;i++)
		{
			IntegerGene gene = new IntegerGene();
			gene.setValue(genes[i]);
			this.pathChromosome.getGenes().add(gene);
		}
				
		calculateScore();

	}
	
	public void calculateScore() throws NeuralNetworkError {
		double cost = 0.0;	
		
		for (int i = 0; i < this.cities.length - 1; i++) {
			IntegerGene gene1 = (IntegerGene)this.pathChromosome.getGene(i);
			IntegerGene gene2 = (IntegerGene)this.pathChromosome.getGene(i+1);
			
			final double dist = this.cities[gene1.getValue()]
					.proximity(this.cities[gene2.getValue()]);
			cost += dist;
		}
		setScore(cost);

	}

	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
}
