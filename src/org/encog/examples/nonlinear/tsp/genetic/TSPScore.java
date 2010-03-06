package org.encog.examples.nonlinear.tsp.genetic;

import org.encog.examples.nonlinear.tsp.City;
import org.encog.solve.genetic.genes.IntegerGene;
import org.encog.solve.genetic.genome.CalculateGenomeScore;
import org.encog.solve.genetic.genome.Genome;

public class TSPScore implements CalculateGenomeScore {

	private City[] cities;
	
	public TSPScore(City[] cities)
	{
		this.cities = cities;
	}
	
	@Override
	public double calculateScore(Genome genome) {
		double result = 0.0;
		
		int[] path = (int[])genome.getOrganism();
		
		for (int i = 0; i < cities.length - 1; i++) {
			City city1 = cities[path[i]];
			City city2 = cities[path[i+1]];
			
			final double dist = city1.proximity(city2);
			result += dist;
		}
		
		return result;
	}

	public boolean shouldMinimize() {
		return true;
	}

}
