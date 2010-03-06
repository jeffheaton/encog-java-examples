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

import org.encog.solve.genetic.GeneticAlgorithm;
import org.encog.solve.genetic.crossover.SpliceNoRepeat;
import org.encog.solve.genetic.genes.Gene;
import org.encog.solve.genetic.genes.IntegerGene;
import org.encog.solve.genetic.genome.CalculateGenomeScore;
import org.encog.solve.genetic.mutate.MutateShuffle;
import org.encog.solve.genetic.population.BasicPopulation;
import org.encog.solve.genetic.population.Population;
import org.encog.examples.nonlinear.tsp.City;

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
	public static final int MAX_SAME_SOLUTION = 25;
	
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

		genetic = new GeneticAlgorithm();
		
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
