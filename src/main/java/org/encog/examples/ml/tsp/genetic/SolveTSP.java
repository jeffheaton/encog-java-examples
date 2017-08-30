/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
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
package org.encog.examples.ml.tsp.genetic;

import org.encog.examples.ml.tsp.City;
import org.encog.ml.CalculateScore;
import org.encog.ml.ea.population.BasicPopulation;
import org.encog.ml.ea.population.Population;
import org.encog.ml.ea.species.BasicSpecies;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.ml.genetic.crossover.SpliceNoRepeat;
import org.encog.ml.genetic.genome.IntegerArrayGenome;
import org.encog.ml.genetic.genome.IntegerArrayGenomeFactory;
import org.encog.ml.genetic.mutate.MutateShuffle;

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
	public static final int CUT_LENGTH = CITIES/5;
	public static final int MAP_SIZE = 256;
	public static final int MAX_SAME_SOLUTION = 50;
	
	private TrainEA genetic;
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
	
	private IntegerArrayGenome randomGenome() {
		IntegerArrayGenome result = new IntegerArrayGenome(cities.length);
		final int organism[] = result.getData();
		final boolean taken[] = new boolean[cities.length];

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
		return result;
	}
	
	private Population initPopulation()
	{
		Population result = new BasicPopulation(POPULATION_SIZE, null);

		BasicSpecies defaultSpecies = new BasicSpecies();
		defaultSpecies.setPopulation(result);
		for (int i = 0; i < POPULATION_SIZE; i++) {
			final IntegerArrayGenome genome = randomGenome();
			defaultSpecies.getMembers().add(genome);
		}
		result.setGenomeFactory(new IntegerArrayGenomeFactory(cities.length));
		result.getSpecies().add(defaultSpecies);
		
		return result;
	}


	/**
	 * Display the cities in the final path.
	 */
	public void displaySolution(IntegerArrayGenome solution) {

		boolean first = true;
		int[] path = solution.getData();
		
		for(int i=0;i<path.length;i++) {
			if( !first )
				System.out.print(">");
			System.out.print( ""+ path[i]);
			first = false;
		}
		
		System.out.println();
	}

	/**
	 * Setup and solve the TSP.
	 */
	public void solve() {
		StringBuilder builder = new StringBuilder();

		initCities();
		
		Population pop = initPopulation();
		
		CalculateScore score =  new TSPScore(cities);

		genetic = new TrainEA(pop,score);
		
		genetic.addOperation(0.9,new SpliceNoRepeat(CITIES/3));
		genetic.addOperation(0.1,new MutateShuffle());

		int sameSolutionCount = 0;
		int iteration = 1;
		double lastSolution = Double.MAX_VALUE;

		while (sameSolutionCount < MAX_SAME_SOLUTION) {
			genetic.iteration();

			double thisSolution = genetic.getError();

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
		IntegerArrayGenome best = (IntegerArrayGenome)genetic.getBestGenome();
		displaySolution(best);
		genetic.finishTraining();

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
