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
import org.encog.solve.genetic.GeneticAlgorithm;
import org.encog.solve.genetic.genes.Gene;
import org.encog.solve.genetic.genes.IntegerGene;
import org.encog.solve.genetic.genome.BasicGenome;
import org.encog.solve.genetic.genome.Chromosome;



/**
 * TSPChromosome: A chromosome that is used to attempt to solve the 
 * traveling salesman problem.  A chromosome is a list of cities.
 */
public class TSPGenome extends BasicGenome {

	private Chromosome pathChromosome;

	public TSPGenome(final GeneticAlgorithm owner, final City cities[]) {
		super(owner);

		final int organism[] = new int[cities.length];
		final boolean taken[] = new boolean[cities.length];

		for (int i = 0; i < organism.length; i++) {
			taken[i] = false;
		}
		for (int i = 0; i < organism.length - 1; i++) {
			int icandidate;
			do {
				icandidate = (int) (Math.random() * organism.length);
			} while (taken[icandidate]);
			organism[i] = icandidate;
			taken[icandidate] = true;
			if (i == organism.length - 2) {
				icandidate = 0;
				while (taken[icandidate]) {
					icandidate++;
				}
				organism[i + 1] = icandidate;
			}
		}
		
		this.pathChromosome = new Chromosome();
		this.getChromosomes().add(this.pathChromosome);
		
		for(int i=0;i<organism.length;i++)
		{
			IntegerGene gene = new IntegerGene();
			gene.setValue(organism[i]);
			this.pathChromosome.getGenes().add(gene);
		}
		setOrganism(organism);
				
		encode();

	}

	@Override
	public void decode() {
		Chromosome chromosome = this.getChromosomes().get(0);
		int[] organism = new int[chromosome.size()];
		
		for(int i=0;i<chromosome.size();i++)
		{
			IntegerGene gene = (IntegerGene)chromosome.get(i);
			organism[i] = gene.getValue();
		}
		
		setOrganism(organism);
	}

	@Override
	public void encode() {
		Chromosome chromosome = this.getChromosomes().get(0);
		
		int[] organism = (int[])getOrganism();

		for(int i=0;i<chromosome.size();i++)
		{
			IntegerGene gene = (IntegerGene)chromosome.get(i);
			gene.setValue(organism[i]);
		}
	}
}
