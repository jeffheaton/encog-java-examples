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
import org.encog.neural.NeuralNetworkError;
import org.encog.solve.anneal.SimulatedAnnealing;



/**
 * TSPSimulatedAnnealing: Implementation of the simulated annealing
 * algorithm that trys to solve the traveling salesman problem.
 */
public class TSPSimulatedAnnealing extends SimulatedAnnealing<Integer> {

	protected City cities[];
	protected Integer path[];

	/**
	 * The constructor.
	 * 
	 * @param network
	 *            The neural network that is to be trained.
	 */
	public TSPSimulatedAnnealing(final City cities[], final double startTemp,
			final double stopTemp, final int cycles) {

		this.setTemperature(startTemp);
		setStartTemperature(startTemp);
		setStopTemperature(stopTemp);
		setCycles(cycles);

		this.cities = cities;
		this.path = new Integer[this.cities.length];
	}

	
	@Override
	public double determineError() throws NeuralNetworkError {
		double cost = 0.0;
		for (int i = 0; i < this.cities.length - 1; i++) {
			final double dist = this.cities[this.path[i]]
					.proximity(this.cities[this.path[i + 1]]);
			cost += dist;
		}
		return cost;
	}

	/**
	 * Called to get the distance between two cities.
	 * 
	 * @param i
	 *            The first city
	 * @param j
	 *            The second city
	 * @return The distance between the two cities.
	 */
	public double distance(final int i, final int j) {
		final int c1 = this.path[i % this.path.length];
		final int c2 = this.path[j % this.path.length];
		return this.cities[c1].proximity(this.cities[c2]);
	}

	@Override
	public Integer[] getArray() {
		return this.path;
	}

	@Override
	public void putArray(final Integer[] array) {
		this.path = array;
	}

	@Override
	public void randomize() {

		final int length = this.path.length;

		// make adjustments to city order(annealing)
		for (int i = 0; i < this.getTemperature(); i++) {
			int index1 = (int) Math.floor(length * Math.random());
			int index2 = (int) Math.floor(length * Math.random());
			final double d = distance(index1, index1 + 1) + distance(index2, index2 + 1)
					- distance(index1, index2) - distance(index1 + 1, index2 + 1);
			if (d>0) {
				
				// sort index1 and index2 if needed
				if (index2 < index1) {
					final int temp = index1;
					index1 = index2;
					index2 = temp;
				}
				for (; index2 > index1; index2--) {
					final int temp = this.path[index1 + 1];
					this.path[index1 + 1] = this.path[index2];
					this.path[index2] = temp;
					index1++;
				}
			}
		}

	}

	@Override
	public Integer[] getArrayCopy() {
		Integer result[] = new Integer[this.path.length];
		System.arraycopy(path, 0, result, 0, path.length);
		return result;
	}

}
