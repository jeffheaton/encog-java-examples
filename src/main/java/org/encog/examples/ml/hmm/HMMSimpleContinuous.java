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
package org.encog.examples.ml.hmm;

import org.encog.ml.data.MLSequenceSet;
import org.encog.ml.hmm.HiddenMarkovModel;
import org.encog.ml.hmm.alog.KullbackLeiblerDistanceCalculator;
import org.encog.ml.hmm.alog.MarkovGenerator;
import org.encog.ml.hmm.distributions.ContinousDistribution;
import org.encog.ml.hmm.train.bw.TrainBaumWelch;

/**
 * This class is a very simple example of a HMM for a continuous input.
 * First, a known HMM is created and output is generated from it.  We then
 * create a second initial HMM, and use the data generated from the first
 * HMM to train it to match the first.
 */
public class HMMSimpleContinuous {
	
	static HiddenMarkovModel buildContHMM()
	{	
		double [] mean1 = {0.25, -0.25};
		double [][] covariance1 = { {1, 2}, {1, 4} };
		
		double [] mean2 = {0.5, 0.25};
		double [][] covariance2 = { {4, 2}, {3, 4} };
		
		HiddenMarkovModel hmm = new HiddenMarkovModel(2);
		
		hmm.setPi(0, 0.8);
		hmm.setPi(1, 0.2);
		
		hmm.setStateDistribution(0, new ContinousDistribution(mean1,covariance1));
		hmm.setStateDistribution(1, new ContinousDistribution(mean2,covariance2));
		
		hmm.setTransitionProbability(0, 1, 0.05);
		hmm.setTransitionProbability(0, 0, 0.95);
		hmm.setTransitionProbability(1, 0, 0.10);
		hmm.setTransitionProbability(1, 1, 0.90);
		
		return hmm;
	}
	
	static HiddenMarkovModel buildContInitHMM()
	{	
		double [] mean1 = {0.20, -0.20};
		double [][] covariance1 = { {1.3, 2.2}, {1.3, 4.3} };
		
		double [] mean2 = {0.5, 0.25};
		double [][] covariance2 = { {4.1, 2.1}, {3.2, 4.4} };
		
		HiddenMarkovModel hmm = new HiddenMarkovModel(2);
		
		hmm.setPi(0, 0.9);
		hmm.setPi(1, 0.1);
		
		hmm.setStateDistribution(0, new ContinousDistribution(mean1,covariance1));
		hmm.setStateDistribution(1, new ContinousDistribution(mean2,covariance2));
		
		hmm.setTransitionProbability(0, 1, 0.10);
		hmm.setTransitionProbability(0, 0, 0.90);
		hmm.setTransitionProbability(1, 0, 0.15);
		hmm.setTransitionProbability(1, 1, 0.85);
		
		return hmm;
	}
	
	public static void main(String[] args) {
		
		HiddenMarkovModel hmm = buildContHMM();
		HiddenMarkovModel learntHmm = buildContInitHMM();
		
		MarkovGenerator mg = new MarkovGenerator(hmm);
		MLSequenceSet training = mg.generateSequences(200,100);
		
		TrainBaumWelch bwl = new TrainBaumWelch(learntHmm,training);
		
		KullbackLeiblerDistanceCalculator klc = 
			new KullbackLeiblerDistanceCalculator();
		
		System.out.println("Training Continuous Hidden Markov Model with Baum Welch");
		
		for(int i=1;i<=10;i++) {
			double e = klc.distance(learntHmm, hmm);
			System.out.println("Iteration #"+i+": Difference: " + e);
			bwl.iteration();
			learntHmm = (HiddenMarkovModel)bwl.getMethod();
		}		
	}
}
