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
package org.encog.examples.ml.bayesian;

import java.util.ArrayList;
import java.util.List;

import org.encog.mathutil.probability.CalcProbability;
import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;
import org.encog.ml.bayesian.query.sample.SamplingQuery;
import org.encog.util.Format;
import org.encog.util.text.BagOfWords;

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
	
	private int k;
	
	private BagOfWords spamBag;
	private BagOfWords hamBag;
	private BagOfWords totalBag;
		
	public void init(int theK) {
		
		this.k = theK;
		
		this.spamBag = new BagOfWords(this.k);
		this.hamBag = new BagOfWords(this.k);
		this.totalBag = new BagOfWords(this.k);

		
		for(String line: SPAM_DATA) {
			spamBag.process(line);
			totalBag.process(line);
		}
		
		for(String line: HAM_DATA) {
			hamBag.process(line);
			totalBag.process(line);
		}
		
		this.hamBag.setLaplaceClasses(totalBag.getUniqueWords());
		this.spamBag.setLaplaceClasses(totalBag.getUniqueWords());		
	}
	
	public List<String> separateSpaces(String str) {
		List<String> result = new ArrayList<String>();
		StringBuilder word = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch != '\'' && !Character.isLetterOrDigit(ch)) {
				if (word.length() > 0) {
					result.add(word.toString());
					word.setLength(0);
				}
			} else {
				word.append(ch);
			}
		}

		if (word.length() > 0) {
			result.add(word.toString());
		}
		
		return result;
	}
	
	public double probabilitySpam(String m) {
		List<String> words = separateSpaces(m);
		
		BayesianNetwork network = new BayesianNetwork();
		BayesianEvent spamEvent = network.createEvent("spam");
		
		int index = 0;
		for( String word: words) {
			BayesianEvent event = network.createEvent(word+index);
			network.createDependency(spamEvent, event);
			index++;
		}
		
		network.finalizeStructure();
		
		//SamplingQuery query = new SamplingQuery(network);
		EnumerationQuery query = new EnumerationQuery(network);
		
		CalcProbability messageProbability = new CalcProbability(this.k);
		messageProbability.addClass(SPAM_DATA.length);
		messageProbability.addClass(HAM_DATA.length);
		double probSpam = messageProbability.calculate(0);

		spamEvent.getTable().addLine(probSpam, true);
		query.defineEventType(spamEvent, EventType.Outcome);
		query.setEventValue(spamEvent, true);
				
		index = 0;
		for( String word: words) {
			String word2 = word+index;
			BayesianEvent event = network.getEvent(word2);
			event.getTable().addLine(this.spamBag.probability(word), true, true); // spam
			event.getTable().addLine(this.hamBag.probability(word), true, false); // ham
			query.defineEventType(event, EventType.Evidence);
			query.setEventValue(event, true);
			index++;
		}

		//query.setSampleSize(100000000);
		query.execute();
		return query.getProbability();		
	}
	
	public void test(String message) {
		double d = probabilitySpam(message);
		System.out.println("Probability of \"" + message + "\" being spam is " + Format.formatPercent(d));
	}
	
	public static final void main(String[] args) {
		BayesianSpam program = new BayesianSpam();
		
		System.out.println("Using Laplace of 0");
		program.init(0);
		program.test("today"); // 0.0
		program.test("sports"); // 16.67
		program.test("today is secret"); // 0.0
		program.test("secret is secret"); // 96.15
		
		System.out.println("Using Laplace of 1");
		program.init(1);
		program.test("today");
		program.test("sports");
		program.test("today is secret"); // 48.58
		program.test("secret is secret");
	}
	
}
