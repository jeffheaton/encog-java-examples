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

import org.encog.examples.nonlinear.basicstrategy.blackjack.Dealer;
import org.encog.examples.nonlinear.basicstrategy.blackjack.Table;
import org.encog.ml.genetic.genome.CalculateGenomeScore;
import org.encog.ml.genetic.genome.Genome;

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
