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
package org.encog.examples.ml.sampling;

public class SimpleSampling {

	/**
	 * How many times to run the experiment? Higher is more accurate, but takes
	 * longer.
	 */
	public static final long COUNT = 50000;

	/**
	 * P(H|F)
	 * Probability of heads, given a fair coin.
	 */
	public static final double PROB_HEADS_G_FAIR = 0.5;
	
	/**
	 * P(~H|F)
	 * Probability of tails, given a fair coin.
	 */
	public static final double PROB_TAILS_G_FAIR = (1.0 - PROB_HEADS_G_FAIR);
	
	/**
	 * P(H|~F)
	 * Probability of heads, given an unfair coin.
	 */
	public static final double PROB_HEADS_G_UNFAIR = 0.9;
	
	/**
	 * P(~H|~F)
	 * Probability of tails, given a unfair coin.
	 */
	public static final double PROB_TAILS_G_UNFAIR = (1.0 - PROB_HEADS_G_UNFAIR);
	
	/**
	 * P(F)
	 * Probability of a fair coin.
	 */
	public static final double PROB_FAIR = 0.8;
	
	/**
	 * P(~F)
	 * Probability of a fair coin.
	 */
	public static final double PROB_UNFAIR = (1.0 - PROB_FAIR);

	/**
	 * How many good trials were there, that had the desired output?
	 */
	int goodCount = 0;

	/**
	 * How many total tries were there, that were not discarded?
	 */
	int totalCount = 0;

	/**
	 * Flip a coin, with the specified probability.
	 * 
	 * @param prob
	 *            The probability that this method returns true.
	 * @return True or false.
	 */
	public boolean flip(double prob) {
		return Math.random() < prob;
	}

	/**
	 * This is just a simple coin flip. Random number generator should make this
	 * about 0.5. P(H) = 0.5 P(~H) = 0.5
	 */
	public void simpleFlip() {
		boolean c = flip(PROB_HEADS_G_FAIR);

		totalCount++;
		if (c) {
			goodCount++;
		}
	}

	/**
	 * What is the total probability of flipping heads.
	 */
	public void probHeadsTotal() {

		boolean f = flip(PROB_FAIR);
		boolean h;

		// flip using a fair, or unfair, coin
		if (f) {
			h = flip(PROB_HEADS_G_FAIR);
		} else {
			h = flip(PROB_HEADS_G_UNFAIR);
		}

		totalCount++;
		if (h) {
			goodCount++;
		}

	}

	/**
	 * What is the probability of using a fair coin when you got heads. This
	 * would normally use Bayes rule, however, we are sampling.
	 */
	public void fairWithHeads() {

		boolean f = flip(PROB_FAIR);
		boolean h;

		if (f) {
			h = flip(PROB_HEADS_G_FAIR);
		} else {
			h = flip(PROB_HEADS_G_UNFAIR);
		}

		// did we get heads, if not we are not 
		// interested in this case
		if (h) {
			totalCount++;
			// if we used a fair coin, increase the good count
			if (f) {
				goodCount++;
			}
		}
	}

	/**
	 * Reset the counters.
	 */
	public void reset() {
		this.goodCount = 0;
		this.totalCount = 0;
	}

	/**
	 * Print the probability.
	 */
	public void printProbability() {
		System.out.println("Probability: " + (double) goodCount
				/ (double) totalCount);
	}

	public static void main(String[] args) {
		SimpleSampling flipper = new SimpleSampling();

		// Simple heads
		System.out.println("Simple heads");
		flipper.reset();
		for (int i = 0; i < COUNT; i++) {
			flipper.simpleFlip();
		}

		flipper.printProbability();

		// total probability
		flipper.reset();
		System.out.println();
		System.out.println("Heads total probability");

		for (int i = 0; i < COUNT; i++) {
			flipper.probHeadsTotal();
		}
		flipper.printProbability();

		// bayes
		flipper.reset();
		System.out.println();
		System.out.println("Probability of fair coin, given heads flip");

		for (int i = 0; i < COUNT; i++) {
			flipper.fairWithHeads();
		}

		flipper.printProbability();
	}
}
