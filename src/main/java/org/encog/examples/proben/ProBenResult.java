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

import org.encog.util.Format;

public class ProBenResult implements Comparable<ProBenResult> {
	private final String name;
	private final int iterations;
	private final double trainingError;
	private final double validationError;
	
	
	public ProBenResult(String name, int iterations, double trainingError,
			double validationError) {
		super();
		this.name = name;
		this.iterations = iterations;
		this.trainingError = trainingError;
		this.validationError = validationError;
	}


	@Override
	public int compareTo(ProBenResult other) {
		return Double.compare(this.validationError, other.validationError);
	}
	
	public String toString() {
		return(this.name + "; Iterations=" + iterations  
				+ "; Training Error=" + Format.formatDouble(this.trainingError, 4) 
				+ "; Validation Error=" + Format.formatDouble(this.validationError, 4));
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}


	/**
	 * @return the trainingError
	 */
	public double getTrainingError() {
		return trainingError;
	}


	/**
	 * @return the validationError
	 */
	public double getValidationError() {
		return validationError;
	}	
	
	
}
