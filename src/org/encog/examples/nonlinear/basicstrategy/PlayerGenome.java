package org.encog.examples.nonlinear.basicstrategy;

import org.encog.solve.genetic.GeneticAlgorithm;
import org.encog.solve.genetic.genes.CharGene;
import org.encog.solve.genetic.genome.BasicGenome;
import org.encog.solve.genetic.genome.Chromosome;

public class PlayerGenome extends BasicGenome {

	private Player player;
	private Chromosome chromosome;
	private int length;
	
	public PlayerGenome(GeneticAlgorithm geneticAlgorithm, Player player) {
		super(geneticAlgorithm);
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
