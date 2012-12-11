package org.encog.examples.ml.prg;

import org.encog.mathutil.EncogFunction;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.prg.EncogProgram;
import org.encog.ml.prg.EncogProgramContext;
import org.encog.ml.prg.extension.StandardExtensions;
import org.encog.ml.prg.train.PrgGenetic;
import org.encog.ml.prg.train.PrgPopulation;
import org.encog.ml.prg.train.fitness.MultiObjectiveFitness;
import org.encog.ml.prg.train.rewrite.RewriteAlgebraic;
import org.encog.ml.prg.train.rewrite.RewriteConstants;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.parse.expression.common.RenderCommonExpression;
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
						return 3*Math.pow(x[0], 2) +  (12*x[0]) + 4;
					}

					@Override
					public int size() {
						return 1;
					}

				}, 0, 100, 1);

		EncogProgramContext context = new EncogProgramContext();
		context.defineVariable("x");

		StandardExtensions.createNumericOperators(context.getFunctions());

		PrgPopulation pop = new PrgPopulation(context);
		//pop.addRewriteRule(new RewriteConstants());
		//pop.addRewriteRule(new RewriteAlgebraic());
		
		MultiObjectiveFitness score = new MultiObjectiveFitness();
		score.addObjective(1.0, new TrainingSetScore(trainingData));
		//score.addObjective(400.0, new ComplexityBasedScore());
		
	
		//CalculateScore score = new ComplexityBasedScore(new TrainingSetScore(trainingData),1);
		PrgGenetic genetic = new PrgGenetic(pop, score);
		genetic.setThreadCount(1);
		//PrgGenetic genetic = new PrgGenetic(pop, new TrainingSetScore(trainingData));
		genetic.createRandomPopulation(5);
		//genetic.getContext().getParams().setMutationProbability(0);
		//genetic.getContext().getParams().setCrossoverProbability(0);

		// genetic.sort();
		// pop.dumpMembers();
		EncogProgram best;

		RenderCommonExpression render = new RenderCommonExpression();

		for(int i=0;i<100;i++) {
			genetic.iteration();
			best = genetic.getBestGenome();
			System.out.println(genetic.getIteration() + ", Error: " + genetic.getError() + ",best: " + best.toString());
			/*System.out.println(genetic.getIteration() + ", Error: " + genetic.getError()
					+ ", size: " + best.size() + ", best: " + render.render(best));*/
		}
		
		genetic.finishTraining();

		// System.out.println(render.render(best));

		best = genetic.getBestGenome();
		EncogUtility.evaluate(genetic.getBestGenome(), trainingData);
		genetic.calculateEffectiveScore(best);
		System.out.println("Final score:" + best.getScore() + ", effective score:" + best.getEffectiveScore());
		
		// System.out.println(best.calculateError(trainingData));

		// System.out.println(train.getError());
		// pop.dumpMembers();
	}
}
