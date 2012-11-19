package org.encog.examples.ml.prg;

import org.encog.mathutil.EncogFunction;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.prg.EncogProgramContext;
import org.encog.ml.prg.extension.StandardExtensions;
import org.encog.ml.prg.train.PrgGenetic;
import org.encog.ml.prg.train.PrgPopulation;
import org.encog.ml.prg.train.rewrite.RewriteConstants;
import org.encog.parse.expression.common.RenderCommonExpression;
import org.encog.util.data.GenerationUtil;
import org.encog.util.simple.EncogUtility;

public class SimpleExpression {
	public static void main(String[] args) {
		
		MLDataSet trainingData = GenerationUtil.generateSingleDataRange(new EncogFunction() {

			@Override
			public double fn(double[] x) {
				return (x[0] + 10)/4;
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

		PrgGenetic genetic = new PrgGenetic(pop,trainingData);
		genetic.createRandomPopulation(5);
		System.out.println("Error: " + genetic.getError());
		genetic.iteration();
		System.out.println("Error: " + genetic.getError());
		
		RenderCommonExpression render = new RenderCommonExpression();
		System.out.println(render.render(genetic.getBestGenome()));
		
		EncogUtility.evaluate(genetic.getBestGenome(), trainingData);
		
		//System.out.println(train.getError());
		//pop.dumpMembers();
	}
}
