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
package org.encog.examples.ml.prg;

import java.util.Random;

import org.encog.Encog;
import org.encog.mathutil.EncogFunction;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.ea.score.adjust.ComplexityAdjustedScore;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.ml.fitness.MultiObjectiveFitness;
import org.encog.ml.prg.EncogProgram;
import org.encog.ml.prg.EncogProgramContext;
import org.encog.ml.prg.PrgCODEC;
import org.encog.ml.prg.extension.StandardExtensions;
import org.encog.ml.prg.generator.RampedHalfAndHalf;
import org.encog.ml.prg.opp.ConstMutation;
import org.encog.ml.prg.opp.SubtreeCrossover;
import org.encog.ml.prg.opp.SubtreeMutation;
import org.encog.ml.prg.species.PrgSpeciation;
import org.encog.ml.prg.train.PrgPopulation;
import org.encog.ml.prg.train.rewrite.RewriteAlgebraic;
import org.encog.ml.prg.train.rewrite.RewriteConstants;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.util.data.GenerationUtil;

public class SimpleExpression {
	public static void main(String[] args) {

		MLDataSet trainingData = GenerationUtil.generateSingleDataRange(
				new EncogFunction() {

					@Override
					public double fn(double[] x) {
						// return (x[0] + 10) / 4;
						// return Math.sin(x[0]);
						return 3 * Math.pow(x[0], 2) + (12 * x[0]) + 4;
					}

					@Override
					public int size() {
						return 1;
					}

				}, 0, 100, 1);

		EncogProgramContext context = new EncogProgramContext();
		context.defineVariable("x");

		StandardExtensions.createNumericOperators(context);

		PrgPopulation pop = new PrgPopulation(context,1000);
		
		MultiObjectiveFitness score = new MultiObjectiveFitness();
		score.addObjective(1.0, new TrainingSetScore(trainingData));

		TrainEA genetic = new TrainEA(pop, score);
		//genetic.setValidationMode(true);
		genetic.setCODEC(new PrgCODEC());
		genetic.addOperation(0.5, new SubtreeCrossover());
		genetic.addOperation(0.25, new ConstMutation(context,0.5,1.0));
		genetic.addOperation(0.25, new SubtreeMutation(context,4));
		genetic.addScoreAdjuster(new ComplexityAdjustedScore(10,20,10,20.0));
		pop.getRules().addRewriteRule(new RewriteConstants());
		pop.getRules().addRewriteRule(new RewriteAlgebraic());
		genetic.setSpeciation(new PrgSpeciation());

		(new RampedHalfAndHalf(context,1, 6)).generate(new Random(), pop);
		
		genetic.setShouldIgnoreExceptions(false);
		
		EncogProgram best = null;
		
		try {

			for (int i = 0; i < 1000; i++) {
				genetic.iteration();
				best = (EncogProgram) genetic.getBestGenome();
				System.out.println(genetic.getIteration() + ", Error: "
						+ best.getScore() + ",Best Genome Size:" +best.size()
						+ ",Species Count:" + pop.getSpecies().size() + ",best: " + best.dumpAsCommonExpression());
			}
			
			//EncogUtility.evaluate(best, trainingData);

			System.out.println("Final score:" + best.getScore()
					+ ", effective score:" + best.getAdjustedScore());
			System.out.println(best.dumpAsCommonExpression());
			//pop.dumpMembers(Integer.MAX_VALUE);
			//pop.dumpMembers(10);

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			genetic.finishTraining();
			Encog.getInstance().shutdown();
		}
	}
}
