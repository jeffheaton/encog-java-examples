package org.encog.examples.ml.bayesian;

import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.training.k2.TrainK2;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;

public class SimpleK2 {
	
	public static final double DATA[][] = {
		{ 1, 0, 0 }, // case 1
		{ 1, 1, 1 }, // case 2
		{ 0, 0, 1 }, // case 3
		{ 1, 1, 1 }, // case 4
		{ 0, 0, 0 }, // case 5
		{ 0, 1, 1 }, // case 6
		{ 1, 1, 1 }, // case 7
		{ 0, 0, 0 }, // case 8
		{ 1, 1, 1 }, // case 9
		{ 0, 0, 0 }, // case 10		
	};

	public static void main(String[] args) {
		String[] labels = { "available", "not" };
		
		MLDataSet data = new BasicMLDataSet(DATA,null);
		BayesianNetwork network = new BayesianNetwork();
		network.createEvent("x1", labels);
		network.createEvent("x2", labels);
		network.createEvent("x3", labels);
		network.finalizeStructure();
		TrainK2 train = new TrainK2(network,data,10);
		train.iteration();
		System.out.println(network.toString());
	}
}
