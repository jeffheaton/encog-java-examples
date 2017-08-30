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
package org.encog.examples.neural.freeform;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.examples.neural.util.TemporalXOR;
import org.encog.ml.CalculateScore;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.neural.freeform.FreeformNetwork;
import org.encog.neural.freeform.training.FreeformBackPropagation;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.simple.EncogUtility;

public class ElmanFreeform {

    public static void main(final String args[]) {

        final TemporalXOR temp = new TemporalXOR();
        final MLDataSet trainingSet = temp.generate(120);

        final FreeformNetwork elmanNetwork = FreeformNetwork.createElman(1, 6, 1, new ActivationSigmoid());
        final FreeformNetwork feedforwardNetwork = FreeformNetwork.createFeedforward(1, 6, 0, 1, new ActivationSigmoid());

        double feedforwardError = trainNetwork("feedforward",feedforwardNetwork,trainingSet);
        double elmanError = trainNetwork("elman",elmanNetwork,trainingSet);

        System.out.println("Best error rate with Elman Network: " + elmanError);
        System.out.println("Best error rate with Feedforward Network: "
                + feedforwardError);
        System.out
                .println("Elman should be able to get into the 10% range,\nfeedforward should not go below 25%.\nThe recurrent Elment net can learn better in this case.");
        System.out
                .println("If your results are not as good, try rerunning, or perhaps training longer.");

        Encog.getInstance().shutdown();
    }

    public static double trainNetwork(final String what,
            final FreeformNetwork network, final MLDataSet trainingSet) {
    	CalculateScore score = new TrainingSetScore(trainingSet);
    	
		final MLTrain trainAlt = new NeuralSimulatedAnnealing(
				network, score, 10, 2, 100);

		final MLTrain trainMain = new FreeformBackPropagation(network, trainingSet,0.00001, 0.0);

		final StopTrainingStrategy stop = new StopTrainingStrategy();
		trainMain.addStrategy(new Greedy());
		trainMain.addStrategy(new HybridStrategy(trainAlt));
		trainMain.addStrategy(stop);
		
		EncogUtility.trainToError(trainMain, 0.01);
    	
        return trainMain.getError();
    }
}
