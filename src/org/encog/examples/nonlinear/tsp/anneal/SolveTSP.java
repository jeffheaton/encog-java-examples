/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
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
 */

package org.encog.examples.nonlinear.tsp.anneal;

import org.encog.examples.nonlinear.tsp.City;

/**
 * SolveTSP with Simulated Annealing.  The Encog API includes a generic
 * simulated annealing problem solver.  This example shows how to use it
 * to find a solution to the Traveling Salesman Problem (TSP).  This 
 * example does not use any sort of neural network.
 * @author 
 *
 */
public class SolveTSP {

	public static final double START_TEMP = 10.0;
	public static final double STOP_TEMP = 2.0;
	public static final int CYCLES = 10;
	public static final int CITIES = 50;
	public static final int MAP_SIZE = 256;
	public static final int MAX_SAME_SOLUTION = 25;

	private TSPSimulatedAnnealing anneal;
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

	/**
	 * Create an initial path of cities.
	 */
	private void initPath() {
		final boolean taken[] = new boolean[this.cities.length];
		final Integer path[] = new Integer[this.cities.length];

		for (int i = 0; i < path.length; i++) {
			taken[i] = false;
		}
		for (int i = 0; i < path.length - 1; i++) {
			int icandidate;
			do {
				icandidate = (int) (Math.random() * path.length);
			} while (taken[icandidate]);
			path[i] = icandidate;
			taken[icandidate] = true;
			if (i == path.length - 2) {
				icandidate = 0;
				while (taken[icandidate]) {
					icandidate++;
				}
				path[i + 1] = icandidate;
			}
		}

		this.anneal.putArray(path);
	}

	/**
	 * Display the cities in the final path.
	 */
	public void displaySolution() {
		Integer path[] = anneal.getArray();
		for (int i = 0; i < path.length; i++) {
			if (i != 0) {
				System.out.print(">");
			}
			System.out.print("" + path[i]);
		}
		System.out.println("");
	}

	/**
	 * Setup and solve the TSP.
	 */
	public void solve() {
		StringBuilder builder = new StringBuilder();

		initCities();

		anneal = new TSPSimulatedAnnealing(cities, START_TEMP, STOP_TEMP,
				CYCLES);

		initPath();

		int sameSolutionCount = 0;
		int iteration = 1;
		double lastSolution = Double.MAX_VALUE;

		while (sameSolutionCount < MAX_SAME_SOLUTION) {
			anneal.iteration();

			double thisSolution = anneal.getError();

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
