package org.encog.examples.ml.prg;

import org.encog.mathutil.EncogFunction;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.prg.EncogProgramContext;
import org.encog.ml.prg.extension.StandardExtensions;
import org.encog.ml.prg.train.PrgPopulation;
import org.encog.ml.prg.train.rewrite.RewriteConstants;
import org.encog.util.data.GenerationUtil;

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
		
		PrgPopulation pop = new PrgPopulation(context,trainingData,1000);
		pop.addRewriteRule(new RewriteConstants());
		pop.createRandomPopulation(5);
		pop.sort();
		
		//System.out.println(train.getError());
		pop.dumpMembers();
	}
}
