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

import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;
import org.encog.ml.bayesian.query.sample.SamplingQuery;

public class SimpleBayesian {
	public static void main(String[] args) {
		
		// build the bayesian network structure
		BayesianNetwork network = new BayesianNetwork();
		BayesianEvent rained = network.createEvent("rained");
		BayesianEvent evenTemperatures = network.createEvent("temperature");
		BayesianEvent gardenGrew = network.createEvent("gardenGrew");
		BayesianEvent plentyOfCarrots = network.createEvent("carrots");
		BayesianEvent plentyOfTomatoes = network.createEvent("Tomatoes");
		network.createDependency(rained, gardenGrew);
		network.createDependency(evenTemperatures, gardenGrew);
		network.createDependency(gardenGrew, plentyOfCarrots);
		network.createDependency(gardenGrew, plentyOfTomatoes);
		network.finalizeStructure();
		
		// build the truth tales
		rained.getTable().addLine(0.2, true);
		evenTemperatures.getTable().addLine(0.5, true);
		gardenGrew.getTable().addLine(0.9, true, true, true);
		gardenGrew.getTable().addLine(0.7, true, false, true);
		gardenGrew.getTable().addLine(0.5, true, true, false);
		gardenGrew.getTable().addLine(0.1, true, false, false);
		plentyOfCarrots.getTable().addLine(0.8, true, true);
		plentyOfCarrots.getTable().addLine(0.2, true, false);
		plentyOfTomatoes.getTable().addLine(0.6, true, true);
		plentyOfTomatoes.getTable().addLine(0.1, true, false);
		
		// validate the network
		network.validate();
		
		// display basic stats
		System.out.println(network.toString());
		System.out.println("Parameter count: " + network.calculateParameterCount());
		
		EnumerationQuery query = new EnumerationQuery(network);
		//SamplingQuery query = new SamplingQuery(network);
		query.defineEventType(rained, EventType.Evidence);
		query.defineEventType(evenTemperatures, EventType.Evidence);
		query.defineEventType(plentyOfCarrots, EventType.Outcome);
		query.setEventValue(rained, true);
		query.setEventValue(evenTemperatures, true);
		query.setEventValue(plentyOfCarrots, true);
		query.execute();
		System.out.println(query.toString());
	}
}
