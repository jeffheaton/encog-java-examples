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
package org.encog.examples.ml.bayesian.words;

import org.encog.util.Format;

public class BayesianSpam {

	public final static String[] SPAM_DATA = {
		"offer is secret",
		"click secret link",
		"secret sports link"
	};
	
	public final static String[] HAM_DATA = {
		"play sports today",
		"went play sports",
		"secret sports event",
		"sports is today",
		"sports costs money"
	};
	
	public static void test(BayesianWordAnalyzer a, String message) {
		double d = a.probability(message);
		System.out.println("Probability of \"" + message + "\" being " + a.getClassName() + " is " + Format.formatPercent(d) + " ; " + a.getLastProblem());
	}
	
	public static void testWordClass(BayesianWordAnalyzer a, String word) {
		double d = a.probabilityWordClass(word);
		System.out.println(a.getLastProblem() + " = " + Format.formatPercent(d));
	}
	
	public static void testWordNotClass(BayesianWordAnalyzer a, String word) {
		double d = a.probabilityWordNotClass(word);
		System.out.println(a.getLastProblem() + " = " + Format.formatPercent(d));

	}
	
	public static final void main(String[] args) {
		BayesianWordAnalyzer a1 = new BayesianWordAnalyzer(0,"spam",SPAM_DATA,"ham",HAM_DATA);
		System.out.println("Using Laplace of 0");
		System.out.println("P(" + a1.getClassName() + "): " + Format.formatPercent(a1.getClassProbability()));
		System.out.println("P(" + a1.getNotClassName() + "): " + Format.formatPercent(a1.getNotClassProbability()));
		testWordClass(a1,"secret");
		testWordNotClass(a1,"secret");
		test(a1,"secret"); // 0.0
		test(a1,"today"); // 0.0
		test(a1,"sports"); // 16.67
		test(a1,"today is secret"); // 0.0
		test(a1,"secret is secret"); // 96.15
		
		BayesianWordAnalyzer a2 = new BayesianWordAnalyzer(1,"spam",SPAM_DATA,"ham",HAM_DATA);
		System.out.println("Using Laplace of 1");
		System.out.println("P(" + a2.getClassName() + "): " + Format.formatPercent(a2.getClassProbability()));
		System.out.println("P(" + a2.getNotClassName() + "): " + Format.formatPercent(a2.getNotClassProbability()));
		testWordClass(a2,"today");
		testWordNotClass(a2,"today");
		test(a2,"secret"); // 0.0
		test(a2,"today");
		test(a2,"sports");
		test(a2,"today is secret"); // 48.58
		test(a2,"secret is secret");
	}
	
	
	
}
