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
