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
package org.encog.examples.ml.bayesian;

import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.training.BayesianInit;
import org.encog.ml.bayesian.training.TrainBayesian;
import org.encog.ml.bayesian.training.search.k2.SearchK2;
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
		//String[] labels = { "available", "not" };
		
		MLDataSet data = new BasicMLDataSet(DATA,null);
		BayesianNetwork network = new BayesianNetwork();
		network.createEvent("x1");
		network.createEvent("x2");
		network.createEvent("x3");
		network.finalizeStructure();
		
		TrainBayesian train = new TrainBayesian(network,data,10);
		train.setInitNetwork(BayesianInit.InitEmpty);
		train.iteration();
		
		double p = network.performQuery("P(+x2|+x1)");// 0.71
		System.out.println("x2 probability : " + network.getEvent("x2").getTable().findLine(1, new int[] {1}));
		System.out.println("Calculated P(+x2|+x1): " + p);
		System.out.println("Final network structure: " + network.toString());
		
		//EncogDirectoryPersistence.saveObject(new File("d:\\test.eg"), network);
	}
}
