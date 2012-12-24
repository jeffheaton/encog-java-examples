package org.encog.examples.ml.prg;

import org.encog.mathutil.EncogFunction;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.genetic.crossover.SubtreeCrossover;
import org.encog.ml.genetic.mutate.SubtreeMutation;
import org.encog.ml.prg.EncogProgram;
import org.encog.ml.prg.EncogProgramContext;
import org.encog.ml.prg.extension.StandardExtensions;
import org.encog.ml.prg.train.PrgGenetic;
import org.encog.ml.prg.train.PrgGenomeFactory;
import org.encog.ml.prg.train.PrgPopulation;
import org.encog.ml.prg.train.fitness.MultiObjectiveFitness;
import org.encog.ml.prg.train.rewrite.RewriteConstants;
import org.encog.ml.prg.train.rewrite.algebraic.RewriteAlgebraic;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.util.data.GenerationUtil;
import org.encog.util.simple.EncogUtility;

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

		PrgGenomeFactory genomeFactory = new PrgGenomeFactory(context);
		PrgPopulation pop = new PrgPopulation(context, genomeFactory);
		pop.addRewriteRule(new RewriteConstants());
		pop.addRewriteRule(new RewriteAlgebraic());

		MultiObjectiveFitness score = new MultiObjectiveFitness();
		score.addObjective(1.0, new TrainingSetScore(trainingData));
		//score.addObjective(400.0, new ComplexityBasedScore());

		PrgGenetic genetic = new PrgGenetic(pop, score);
		genetic.addOperation(0.95, new SubtreeCrossover());
		genetic.addOperation(0.05, new SubtreeMutation(context,4));
		genetic.createRandomPopulation(5);

		//context.getParams().setIgnoreExceptions(true);
		EncogProgram best = (EncogProgram)genetic.getPopulation().getGenomeFactory().factor();

		try {

			for (int i = 0; i < 1000; i++) {
				genetic.iteration();
				genetic.copyBestGenome(best);
				System.out.println(genetic.getIteration() + ", Error: "
						+ genetic.getError() + ",best: " + best.dumpAsCommonExpression());
			}
			
			genetic.copyBestGenome(best);
			EncogUtility.evaluate(best, trainingData);
			genetic.calculateEffectiveScore(best);
			System.out.println("Final score:" + best.getScore()
					+ ", effective score:" + best.getAdjustedScore());
			System.out.println(best.dumpAsCommonExpression());
			
			genetic.sort();
			pop.dumpMembers(10);

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			genetic.finishTraining();
		}

		
	}
}
