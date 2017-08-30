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
import org.encog.ml.hmm.distributions.DiscreteDistribution;
import org.encog.ml.hmm.train.kmeans.TrainKMeans;

/**
 * This is a simple example of KMeans training for a Hidden Markov Model (HMM).
 * First, a known HMM is created and output generated for it.  Using this output
 * and KMeans we can generate a 2nd hiddem markov model that is very similar to the
 * first. 
 */
public class HMMSimpleKMeans {
	
	static HiddenMarkovModel buildDiscHMM()
	{	
		HiddenMarkovModel hmm = 
			new HiddenMarkovModel(2, 2);
		
		hmm.setPi(0, 0.95);
		hmm.setPi(1, 0.05);
		
		hmm.setStateDistribution(0, new DiscreteDistribution(new double[][] { { 0.95, 0.05 } }));
		hmm.setStateDistribution(1, new DiscreteDistribution(new double[][] { { 0.20, 0.80 } }));
		
		hmm.setTransitionProbability(0, 1, 0.05);
		hmm.setTransitionProbability(0, 0, 0.95);
		hmm.setTransitionProbability(1, 0, 0.10);
		hmm.setTransitionProbability(1, 1, 0.90);
		
		return hmm;
	}
	
	
	public static void main(String[] args) {
		HiddenMarkovModel hmm = buildDiscHMM();
		HiddenMarkovModel learntHmm = null;
		
		MarkovGenerator mg = new MarkovGenerator(hmm);
		MLSequenceSet training = mg.generateSequences(200,100);
		
		TrainKMeans trainer = new TrainKMeans(hmm,training);
		
		KullbackLeiblerDistanceCalculator klc = 
			new KullbackLeiblerDistanceCalculator();
		
		System.out.println("Training Hidden Markov Model with KMeans");
				
		for(int i=1;i<=10;i++) {
			trainer.iteration();
			learntHmm = (HiddenMarkovModel)trainer.getMethod();
			double e = klc.distance(learntHmm, hmm);
			System.out.println("Iteration #"+i+": Difference: " + e);			
		}
	}
}
