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
package org.encog.examples.neural.benchmark;

import java.util.Arrays;

import org.encog.mathutil.error.ErrorCalculation;
import org.encog.mathutil.error.ErrorCalculationMode;
import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.util.Format;

public class ErrorMethod {
	
	public static double[] generate(int size, double low, double high) {
		double[] result = new double[size];
		for(int i=0;i<result.length;i++) {
			result[i] = RangeRandomizer.randomize(low, high);
		}
		return result;
	}
	
	public static double[] distort(double[] d, double distort) {
		double[] result = new double[d.length];
		for(int i=0;i<d.length;i++) {
			double pct = RangeRandomizer.randomize(0,distort) - (distort/2);
			pct+=1.0;
			result[i] = d[i] * pct;
		}
		return result;
	}
	
	public static double evaluate(ErrorCalculationMode m, double[] actual, double[] ideal) {
		ErrorCalculation.setMode(m);
		ErrorCalculation error = new ErrorCalculation();
		for(int i=0;i<actual.length;i++) {
			error.updateError(actual[i], ideal[i]);
		}
		return error.calculate();
	}
	
	public static void tryRange(double low, double high) {
		System.out.println("Trying from " + Format.formatDouble(low, 2) + " to " +  Format.formatDouble(high,2));		
		double[] ideal = generate(10,low,high);
		double[] actual = distort(ideal,.2);
		System.out.println("Actual:" + Arrays.toString(actual));
		System.out.println("Ideal:" + Arrays.toString(ideal));
		System.out.println("Error (ESS): " + Format.formatDouble(evaluate(ErrorCalculationMode.ESS,actual,ideal),8));
		System.out.println("Error (MSE): " + Format.formatPercent(evaluate(ErrorCalculationMode.MSE,actual,ideal)));
		System.out.println("Error (RMS): " + Format.formatPercent(evaluate(ErrorCalculationMode.RMS,actual,ideal)));
	}
	
	public static void main(String[] args) {
		tryRange(-1,1);
		tryRange(0,1);
	}
}
