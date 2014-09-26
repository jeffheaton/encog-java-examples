/*
 * Encog(tm) Java Examples v3.3
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2014 Heaton Research, Inc.
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

import org.encog.ml.MLError;
import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.util.Format;

public class ProBenEvaluate {
	
	private ProBenData data;
	private int iterations;
	private double trainError;
	private double testError;
	private double validationError;
	private String methodName;
	private String trainingName;
	private String methodArchitecture;
	private String trainingArgs;
	
	public ProBenEvaluate(ProBenData theData, String theMethodName, String theTrainingName, String theMethodArchitecture, String theTrainingArgs) {
		this.data = theData;
		this.methodName = theMethodName;
		this.trainingName = theTrainingName;
		this.methodArchitecture = theMethodArchitecture;
		this.trainingArgs = theTrainingArgs;
	}
	
	public void evaluate() {
		
			MLMethodFactory methodFactory = new MLMethodFactory();		
			MLMethod method = methodFactory.create(methodName, methodArchitecture, 
					data.getInputCount(), data.getIdealCount());
			
			MLTrainFactory trainFactory = new MLTrainFactory();	
			MLTrain train = trainFactory.create(method,data.getTrainingDataSet(),trainingName,trainingArgs);
			
			// reset if improve is less than 1% over 5 cycles
			if( method instanceof MLResettable && !(train instanceof ManhattanPropagation) ) {
				train.addStrategy(new RequiredImprovementStrategy(5000));
			}

			
		this.iterations = 0;
		do {
			//System.out.println(this.iterations + " - " + train.getError());
			train.iteration();
			this.iterations++;
		} while (train.getError() > 0.01);
		
		MLError calc = (MLError)train.getMethod();
		this.trainError = calc.calculateError(data.getTrainingDataSet());
		this.testError = calc.calculateError(data.getTestDataSet());
		this.validationError = calc.calculateError(data.getValidationDataSet());
		
		System.out.println(data.getName() + "; Iterations=" + iterations + "; Data Size=" + Format.formatInteger((int)data.getTrainingDataSet().getRecordCount()) 
				+ "; Training Error=" + Format.formatPercent(this.trainError) + "; Validation Error=" + Format.formatPercent(this.validationError));
	}
}
