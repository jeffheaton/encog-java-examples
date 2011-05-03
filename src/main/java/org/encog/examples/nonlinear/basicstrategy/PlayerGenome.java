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
package org.encog.examples.nonlinear.basicstrategy;

import org.encog.ml.genetic.GeneticAlgorithm;
import org.encog.ml.genetic.genes.CharGene;
import org.encog.ml.genetic.genome.BasicGenome;
import org.encog.ml.genetic.genome.Chromosome;

public class PlayerGenome extends BasicGenome {

	private Player player;
	private Chromosome chromosome;
	private int length;
	
	public PlayerGenome(GeneticAlgorithm geneticAlgorithm, Player player) {		
		this.player = player;
		this.chromosome = new Chromosome();
		setOrganism(this.player);
		
		this.length = player.getRules().length;
		
		for(int i=0;i<length;i++)
		{
			this.chromosome.add(new CharGene());
		}		
	}

	@Override
	public void decode() {
		for(int i=0;i<length;i++)
		{
			this.player.getRules()[i] = ((CharGene)this.chromosome.get(i)).getValue();
		}		
	}

	@Override
	public void encode() {
		for(int i=0;i<length;i++)
		{
			((CharGene)this.chromosome.get(i)).setValue(this.player.getRules()[i]);
		}
	}


}
