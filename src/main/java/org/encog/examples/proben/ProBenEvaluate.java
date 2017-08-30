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
package org.encog.examples.proben;

import java.util.Set;
import java.util.TreeSet;

import org.encog.ml.MLError;
import org.encog.ml.MLMethod;
import org.encog.ml.train.MLTrain;

public class ProBenEvaluate {
	
	private ProBenData data;
	private int iterations;
	private BenchmarkDefinition def;
	
	public ProBenEvaluate(ProBenData theData, BenchmarkDefinition theDefinition) {
		this.data = theData;
		this.def = theDefinition;
	}
	
	public ProBenResult evaluate() {
		Set<ProBenResult> results = new TreeSet<ProBenResult>();
		for(int i=0;i<5;i++) {
			ProBenResult result = evaluateSingle();
			results.add(result);
			System.out.println(i+":"+result);
		}
		return results.iterator().next();
	}
	
	private ProBenResult evaluateSingle() {
		MLMethod method = this.def.createMethod(this.data);
		MLTrain train = this.def.createTrainer(method, this.data);

		this.iterations = 0;
		do {
			//System.out.println(this.iterations + " " + train.getError());
			train.iteration();
			this.iterations++;
		} while (!train.isTrainingDone());
		
		MLError calc = (MLError)train.getMethod();
		
		return new ProBenResult(data.getName(),
				iterations,
				calc.calculateError(data.getTrainingDataSet()),
				calc.calculateError(data.getValidationDataSet()));		
	}
}
