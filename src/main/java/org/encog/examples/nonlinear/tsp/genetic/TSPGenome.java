/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.examples.nonlinear.tsp.genetic;

import org.encog.examples.nonlinear.tsp.City;
import org.encog.ml.genetic.GeneticAlgorithm;
import org.encog.ml.genetic.genes.IntegerGene;
import org.encog.ml.genetic.genome.BasicGenome;
import org.encog.ml.genetic.genome.Chromosome;



/**
 * TSPChromosome: A chromosome that is used to attempt to solve the 
 * traveling salesman problem.  A chromosome is a list of cities.
 */
public class TSPGenome extends BasicGenome {

	private Chromosome pathChromosome;

	public TSPGenome(final GeneticAlgorithm owner, final City cities[]) {

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
