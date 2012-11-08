package org.encog.examples.ml.prg;

import org.encog.ml.prg.EncogProgram;
import org.encog.parse.expression.common.RenderCommonExpression;
import org.encog.util.Stopwatch;

public class Benchmark {
	public static final int MILLION = 1000000;
	public static final int COUNT = 10 * MILLION;
	
	
	public static void main(String[] args) {
		EncogProgram expression = new EncogProgram("((a+25)^3/25)-((a*3)^4/250)");
		
		RenderCommonExpression render = new RenderCommonExpression();
		System.out.println(render.render(expression));
		
		
		Stopwatch sw = new Stopwatch();
		sw.start();
		for(double a = 0;a<COUNT;a++) {
			expression.getVariables().setVariable("a",a);
			expression.evaluate();
		}
		sw.stop();
		
		System.out.println("Time: " + sw.getElapsedMilliseconds());
		// 3264
	}
}
