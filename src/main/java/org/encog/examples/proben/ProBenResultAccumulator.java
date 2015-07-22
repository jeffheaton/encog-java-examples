package org.encog.examples.proben;

import org.encog.util.Format;

public class ProBenResultAccumulator {

	private int iterations;
	private double trainingError;
	private double validationError;
	private int count;
		
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
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	public void accumulate(ProBenResult result) {
		this.iterations+=result.getIterations();
		this.trainingError+=result.getTrainingError();
		this.validationError+=result.getValidationError();
		this.count++;
	}
	
	public String toString() {
		return("Mean Iterations=" + iterations/this.count  
				+ "; Mean Training Error=" + Format.formatDouble(this.trainingError/this.count, 4) 
				+ "; Mean Validation Error=" + Format.formatDouble(this.validationError/this.count, 4));
	}
}
