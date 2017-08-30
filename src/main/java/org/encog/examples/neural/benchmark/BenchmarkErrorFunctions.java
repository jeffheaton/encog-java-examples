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
package org.encog.examples.neural.benchmark;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationReLU;
import org.encog.examples.proben.BenchmarkDefinition;
import org.encog.examples.proben.ProBenData;
import org.encog.examples.proben.ProBenResultAccumulator;
import org.encog.examples.proben.ProBenRunner;
import org.encog.mathutil.randomize.XaiverRandomizer;
import org.encog.ml.MLMethod;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
import org.encog.ml.train.strategy.end.EarlyStoppingStrategy;
import org.encog.ml.train.strategy.end.EndIterationsStrategy;
import org.encog.neural.error.ATanErrorFunction;
import org.encog.neural.error.CrossEntropyErrorFunction;
import org.encog.neural.error.ErrorFunction;
import org.encog.neural.error.LinearErrorFunction;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.ContainsFlat;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class BenchmarkErrorFunctions implements BenchmarkDefinition {
	
	private String probenFolder;
	private ErrorFunction errorFn;
	
	BenchmarkErrorFunctions(String theProbenFolder, ErrorFunction theErrorFn) {
		this.probenFolder = theProbenFolder;
		this.errorFn = theErrorFn;
	}
	
	public MLMethod createMethod(ProBenData data) {
		int hiddenCount = (int)((data.getInputCount()+data.getIdealCount())*0.5);
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,data.getInputCount()));
		network.addLayer(new BasicLayer(new ActivationReLU(),true,hiddenCount));
		network.addLayer(new BasicLayer(new ActivationLinear(),false,data.getIdealCount()));
		network.getStructure().finalizeStructure();
		network.reset();
		(new XaiverRandomizer()).randomize(network);
		return network;
	}
	
	public MLTrain createTrainer(MLMethod method, ProBenData data) {
		final ResilientPropagation train = new ResilientPropagation(
				(ContainsFlat)method, data.getTrainingDataSet());
		train.setErrorFunction(this.errorFn);
		train.addStrategy(new EarlyStoppingStrategy(data.getValidationDataSet()));
		train.addStrategy(new RequiredImprovementStrategy(100));
		train.addStrategy(new EndIterationsStrategy(2000));
		return train;
	}
	
	public String getProBenFolder() {
		return this.probenFolder;
	}
	
	public static ProBenResultAccumulator benchmarkLinear(String probenPath) {
		System.out.println("Starting Linear...");
		BenchmarkErrorFunctions def = new BenchmarkErrorFunctions(probenPath, new LinearErrorFunction());
		ProBenRunner runner = new ProBenRunner(def);
		return runner.run();
	}
	
	public static ProBenResultAccumulator benchmarkArctan(String probenPath) {
		System.out.println("Starting Arctan...");
		BenchmarkErrorFunctions def = new BenchmarkErrorFunctions(probenPath, new ATanErrorFunction());
		ProBenRunner runner = new ProBenRunner(def);
		return runner.run();
	}
	
	public static ProBenResultAccumulator benchmarkCrossEntropy(String probenPath) {
		System.out.println("Starting CrossEntropy...");
		BenchmarkErrorFunctions def = new BenchmarkErrorFunctions(probenPath, new CrossEntropyErrorFunction());
		ProBenRunner runner = new ProBenRunner(def);
		return runner.run();
	}
	
	public static void main(String[] args) {		
		String probenPath = ProBenData.obtainProbenPath(args);
		
		System.out.println("Starting...");
		ProBenResultAccumulator linear = benchmarkLinear(probenPath);
		ProBenResultAccumulator arctan = benchmarkArctan(probenPath);
		ProBenResultAccumulator crossEntropy = benchmarkCrossEntropy(probenPath);

		System.out.println("Linear: " + linear.toString());
		System.out.println("Arctan: " + arctan.toString());
		System.out.println("Cross Entropy: " + crossEntropy.toString());

		Encog.getInstance().shutdown();

		
	}

	@Override
	public boolean shouldCenter() {
		return true;
	}

	@Override
	public double getInputCenter() {
		return 0;
	}

	@Override
	public double getOutputCenter() {
		return 2;
	}
}
