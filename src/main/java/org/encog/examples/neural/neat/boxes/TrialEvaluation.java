package org.encog.examples.neural.neat.boxes;

import org.encog.ml.MLMethod;

public class TrialEvaluation {

	private final MLMethod phenotype;
	private final BoxTrial test;
	private double accDistance;
	private double accRange;
	
	public TrialEvaluation(MLMethod thePhenotype, BoxTrial theTest) {
		this.phenotype = thePhenotype;
		this.test = theTest;
	}

	/**
	 * @return the phenotype
	 */
	public MLMethod getPhenotype() {
		return phenotype;
	}

	/**
	 * @return the test
	 */
	public BoxTrial getTest() {
		return test;
	}

	public void accumulate(double distance, double range) {
		this.accDistance+=distance;
		this.accRange+=range;
	}

	/**
	 * @return the accDistance
	 */
	public double getAccDistance() {
		return accDistance;
	}

	/**
	 * @param accDistance the accDistance to set
	 */
	public void setAccDistance(double accDistance) {
		this.accDistance = accDistance;
	}

	/**
	 * @return the accRange
	 */
	public double getAccRange() {
		return accRange;
	}

	/**
	 * @param accRange the accRange to set
	 */
	public void setAccRange(double accRange) {
		this.accRange = accRange;
	}

	public double calculateFitness() {
        final double threshold = BoxesScore.EDGE_LEN * BoxesScore.SQR_LEN;
        double rmsd = Math.sqrt(this.accDistance / 75.0);
        double fitness;
        if(rmsd > threshold) {
            fitness = 0.0;
        } else {
            fitness = (((threshold-rmsd) * 100.0) / threshold) + (this.accRange / 7.5);
        }

        return fitness;
	}
	
	
	
	
}
