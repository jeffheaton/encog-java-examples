package org.encog.examples.neural.gui.basicstrategy;

import org.encog.solve.genetic.Chromosome;

public class BlackJackChromosome extends Chromosome<Character> {

	private Player player;
	
	public BlackJackChromosome(Player player)
	{
		this.player = player;
		//this.setGenes(player.getRules());
		calculateScore();
	}
	
	@Override
	public void calculateScore() {
		this.setScore(this.player.getMoney());
		
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

}
