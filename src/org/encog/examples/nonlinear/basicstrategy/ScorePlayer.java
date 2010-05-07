package org.encog.examples.nonlinear.basicstrategy;

import org.encog.examples.nonlinear.basicstrategy.blackjack.Dealer;
import org.encog.examples.nonlinear.basicstrategy.blackjack.Table;
import org.encog.solve.genetic.genome.CalculateGenomeScore;
import org.encog.solve.genetic.genome.Genome;

public class ScorePlayer implements CalculateGenomeScore {

	@Override
	public double calculateScore(Genome genome) {
		
		Player player = (Player)genome.getOrganism();
		player.setMoney(1000);
		
		Table table = new Table(1, new Dealer());
		table.addPlayer(player);
		
		for(int rounds = 0; rounds< 100; rounds++ )
		{
			table.play();
		}
		
		return player.getMoney();
	}

	@Override
	public boolean shouldMinimize() {
		return false;
	}

}
