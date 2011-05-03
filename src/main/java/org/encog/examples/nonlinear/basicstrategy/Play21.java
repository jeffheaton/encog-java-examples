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
import org.encog.ml.genetic.BasicGeneticAlgorithm;
import org.encog.ml.genetic.GeneticAlgorithm;
import org.encog.ml.genetic.crossover.Splice;
import org.encog.ml.genetic.mutate.MutateShuffle;
import org.encog.ml.genetic.population.BasicPopulation;
import org.encog.ml.genetic.population.Population;

public class Play21 {

	public static final int POPULATION_SIZE = 1000;
	public static final double MUTATION_PERCENT = 0.1;
	public static final double PERCENT_TO_MATE = 0.24;
	public static final double MATING_POPULATION_PERCENT = 0.5;
	public static final int CUT_LENGTH = 50;
	
	public static void test() {
		/*
		 * for(int i=0;i<52;i++) { System.out.print(identifyCard(i));
		 * System.out.print(" "); System.out.print(hasSoftValue(i));
		 * System.out.print(" "); System.out.print(softValue(i));
		 * System.out.print(" "); System.out.print(hardValue(i));
		 * 
		 * System.out.println(); }
		 */

		Table table = new Table(1, new Dealer());
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		for (int i = 0; i < 10; i++) {
			table.play();
		}

	}
	
	private static void initPopulation(GeneticAlgorithm ga)
	{
		ScorePlayer score =  new ScorePlayer();
		ga.setCalculateScore(score);
		Population population = new BasicPopulation(POPULATION_SIZE);
		ga.setPopulation(population);

		for (int i = 0; i < POPULATION_SIZE; i++) {

			Player player = new Player(1000);
			player.randomize();
			final PlayerGenome genome = new PlayerGenome(ga, player);
			ga.getPopulation().add(genome);
			ga.calculateScore(genome);
		}
		population.sort();
	}

	public static void main(String[] args) {
		GeneticAlgorithm genetic = new BasicGeneticAlgorithm();
		initPopulation(genetic);
		genetic.setMutationPercent(MUTATION_PERCENT);
		genetic.setPercentToMate(PERCENT_TO_MATE);
		genetic.setMatingPopulation(MATING_POPULATION_PERCENT);
		genetic.setCrossover(new Splice(CUT_LENGTH));
		genetic.setMutate(new MutateShuffle());
		
		boolean done = false;
		int iteration = 0;
		
		while(!done)
		{
			iteration++;
			genetic.iteration();
			double thisSolution = genetic.getPopulation().getBest().getScore();
			System.out.println(iteration + ": " + thisSolution);
		}
	}

}
