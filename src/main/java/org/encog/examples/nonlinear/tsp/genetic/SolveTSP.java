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
import org.encog.ml.genetic.BasicGeneticAlgorithm;
import org.encog.ml.genetic.GeneticAlgorithm;
import org.encog.ml.genetic.crossover.SpliceNoRepeat;
import org.encog.ml.genetic.genes.Gene;
import org.encog.ml.genetic.genes.IntegerGene;
import org.encog.ml.genetic.genome.CalculateGenomeScore;
import org.encog.ml.genetic.mutate.MutateShuffle;
import org.encog.ml.genetic.population.BasicPopulation;
import org.encog.ml.genetic.population.Population;

/**
 * SolveTSP with a genetic algorithm.  The Encog API includes a generic
 * genetic algorithm problem solver.  This example shows how to use it
 * to find a solution to the Traveling Salesman Problem (TSP).  This 
 * example does not use any sort of neural network.
 * @author 
 *
 */
public class SolveTSP {

	public static final int CITIES = 50;
	public static final int POPULATION_SIZE = 1000;
	public static final double MUTATION_PERCENT = 0.1;
	public static final double PERCENT_TO_MATE = 0.24;
	public static final double MATING_POPULATION_PERCENT = 0.5;
	public static final int CUT_LENGTH = CITIES/5;
	public static final int MAP_SIZE = 256;
	public static final int MAX_SAME_SOLUTION = 50;
	
	private GeneticAlgorithm genetic;
	private City cities[];

	/**
	 * Place the cities in random locations.
	 */
	private void initCities() {
		cities = new City[CITIES];
		for (int i = 0; i < cities.length; i++) {
			int xPos = (int) (Math.random() * MAP_SIZE);
			int yPos = (int) (Math.random() * MAP_SIZE);

			cities[i] = new City(xPos, yPos);
		}
	}
	
	private void initPopulation(GeneticAlgorithm ga)
	{
		CalculateGenomeScore score =  new TSPScore(cities);
		ga.setCalculateScore(score);
		Population population = new BasicPopulation(POPULATION_SIZE);
		ga.setPopulation(population);

		for (int i = 0; i < POPULATION_SIZE; i++) {

			final TSPGenome genome = new TSPGenome(ga, cities);
			ga.getPopulation().add(genome);
			ga.calculateScore(genome);
		}
		population.claim(ga);
		population.sort();
	}


	/**
	 * Display the cities in the final path.
	 */
	public void displaySolution() {

		boolean first = true;
		
		for(Gene gene : genetic.getPopulation().getBest().getChromosomes().get(0).getGenes() )
		{
			if( !first )
				System.out.print(">");
			System.out.print( ""+ ((IntegerGene)gene).getValue());
			first = false;
		}
		
		System.out.println("");
	}

	/**
	 * Setup and solve the TSP.
	 */
	public void solve() {
		StringBuilder builder = new StringBuilder();

		initCities();

		genetic = new BasicGeneticAlgorithm();
		
		initPopulation(genetic);
		genetic.setMutationPercent(MUTATION_PERCENT);
		genetic.setPercentToMate(PERCENT_TO_MATE);
		genetic.setMatingPopulation(MATING_POPULATION_PERCENT);
		genetic.setCrossover(new SpliceNoRepeat(CITIES/3));
		genetic.setMutate(new MutateShuffle());

		int sameSolutionCount = 0;
		int iteration = 1;
		double lastSolution = Double.MAX_VALUE;

		while (sameSolutionCount < MAX_SAME_SOLUTION) {
			genetic.iteration();

			double thisSolution = genetic.getPopulation().getBest().getScore();

			builder.setLength(0);
			builder.append("Iteration: ");
			builder.append(iteration++);
			builder.append(", Best Path Length = ");
			builder.append(thisSolution);

			System.out.println(builder.toString());

			if (Math.abs(lastSolution - thisSolution) < 1.0) {
				sameSolutionCount++;
			} else {
				sameSolutionCount = 0;
			}

			lastSolution = thisSolution;
		}

		System.out.println("Good solution found:");
		displaySolution();

	}

	/**
	 * Program entry point.
	 * @param args Not used.
	 */
	public static void main(String args[]) {
		SolveTSP solve = new SolveTSP();
		solve.solve();
	}

}
