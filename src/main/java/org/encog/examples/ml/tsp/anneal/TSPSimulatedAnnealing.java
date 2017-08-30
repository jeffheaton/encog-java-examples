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
package org.encog.examples.ml.tsp.anneal;

import org.encog.examples.ml.tsp.City;
import org.encog.ml.anneal.SimulatedAnnealing;
import org.encog.neural.NeuralNetworkError;



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
	public double calculateScore() throws NeuralNetworkError {
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
