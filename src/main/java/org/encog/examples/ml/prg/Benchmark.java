package org.encog.examples.ml.prg;

import org.encog.ml.prg.EncogProgram;
import org.encog.parse.expression.common.RenderCommonExpression;
import org.encog.util.Stopwatch;

public class Benchmark {
	public static final int MILLION = 1000000;
	public static final int COUNT = 10 * MILLION;
	
	public static void testDirect() {
		Stopwatch sw = new Stopwatch();
		sw.start();
		double d = 0;
		
		for(double a = 0;a<COUNT;a++) {
			d+=(Math.pow(a+25,3)/25)-(Math.pow((a*3),4)/250);
		}
		sw.stop();
		
		System.out.println("Time Direct: " + sw.getElapsedMilliseconds());
	}
	
	public static void testEPL() {
		EncogProgram expression = new EncogProgram("((a+25)^3/25)-((a*3)^4/250)");

		Stopwatch sw = new Stopwatch();
		sw.start();
		for(double a = 0;a<COUNT;a++) {
			expression.getVariables().setVariable("a",a);
			expression.evaluate();
		}
		sw.stop();
		
		System.out.println("Time EPL: " + sw.getElapsedMilliseconds());
	}
	
	public static void main(String[] args) {
		testDirect();
		testEPL();
	}
}
