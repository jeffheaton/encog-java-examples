package org.encog.examples.ml.prg;

import java.util.Random;

import org.encog.mathutil.EncogFunction;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.ea.score.adjust.ComplexityAdjustedScore;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.ml.prg.EncogProgram;
import org.encog.ml.prg.EncogProgramContext;
import org.encog.ml.prg.PrgCODEC;
import org.encog.ml.prg.extension.StandardExtensions;
import org.encog.ml.prg.generator.PrgGrowGenerator;
import org.encog.ml.prg.opp.SubtreeCrossover;
import org.encog.ml.prg.opp.SubtreeMutation;
import org.encog.ml.prg.train.PrgPopulation;
import org.encog.ml.prg.train.fitness.MultiObjectiveFitness;
import org.encog.ml.prg.train.rewrite.RewriteConstants;
import org.encog.ml.prg.train.rewrite.algebraic.RewriteAlgebraic;
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

		StandardExtensions.createNumericOperators(context.getFunctions());

		PrgPopulation pop = new PrgPopulation(context,1000);
		pop.addRewriteRule(new RewriteConstants());
		pop.addRewriteRule(new RewriteAlgebraic());
		
		

		MultiObjectiveFitness score = new MultiObjectiveFitness();
		score.addObjective(1.0, new TrainingSetScore(trainingData));
		//score.addObjective(400.0, new ComplexityBasedScore());

		TrainEA genetic = new TrainEA(pop, score);
		genetic.setValidationMode(true);
		genetic.setCODEC(new PrgCODEC());
		genetic.addOperation(0.95, new SubtreeCrossover());
		genetic.addOperation(0.05, new SubtreeMutation(context,4));
		genetic.addScoreAdjuster(new ComplexityAdjustedScore());

		(new PrgGrowGenerator(context,genetic.getScoreFunction(),5)).generate(new Random(), pop);
		
		genetic.setShouldIgnoreExceptions(false);
		
		EncogProgram best = null;
		
		try {

			for (int i = 0; i < 1000; i++) {
				genetic.iteration();
				best = (EncogProgram) genetic.getBestGenome();
				System.out.println(genetic.getIteration() + ", Error: "
						+ best.getScore() + ",best: " + best.dumpAsCommonExpression());
			}
			
			//EncogUtility.evaluate(best, trainingData);

			System.out.println("Final score:" + best.getScore()
					+ ", effective score:" + best.getAdjustedScore());
			System.out.println(best.dumpAsCommonExpression());
			System.out.println( "Index:" + pop.getSpecies().get(0).getMembers().indexOf(best));
			pop.dumpMembers(Integer.MAX_VALUE);

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			genetic.finishTraining();
		}

		
	}
}
