package org.encog.examples.ml.bayesian;

import java.util.ArrayList;
import java.util.List;

import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.query.sample.SamplingQuery;
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
	
	public static final int K = 0;
	
	private final BagOfWords spamBag = new BagOfWords();
	private final BagOfWords hamBag = new BagOfWords();
	private final BagOfWords totalBag = new BagOfWords();
	
	private BayesianNetwork network = new BayesianNetwork();
	private BayesianEvent spamEvent;
	
	public void init() {
		for(String line: SPAM_DATA) {
			spamBag.process(line);
			totalBag.process(line);
		}
		
		for(String line: HAM_DATA) {
			hamBag.process(line);
			totalBag.process(line);
		}	
		
		System.out.println("Word count spam: " + this.spamBag.getTotalWords());
		System.out.println("Word count ham: " + this.hamBag.getTotalWords());
		System.out.println("Total count: " + this.totalBag.getTotalWords());
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
	
	public double probabilityWordSpam(String word) {
		if( !spamBag.contains(word) )
			return 0;
		return (double)spamBag.getWords().get(word)/(double)spamBag.getTotalWords();
	}
	
	public double probabilityWordHam(String word) {
		if( !hamBag.contains(word) )
			return 0;
		return (double)hamBag.getWords().get(word)/(double)hamBag.getTotalWords();
	}
	
	public double probabilitySpam(String m) {
		List<String> words = separateSpaces(m);
		
		this.spamEvent = network.createEvent("spam");
		
		int index = 0;
		for( String word: words) {
			BayesianEvent event = network.createEvent(word+index);
			network.createDependancy(spamEvent, event);
			index++;
		}
		
		network.finalizeStructure();
		
		System.out.println(network.toString());
		
		SamplingQuery query = new SamplingQuery(network);
		
		double probSpam = (double)(SPAM_DATA.length+K)/(double)(SPAM_DATA.length+HAM_DATA.length+(K*2));
		System.out.println(probSpam);

		this.spamEvent.getTable().addLine(probSpam, true);
		query.defineEventType(this.spamEvent, EventType.Outcome);
		query.setEventValue(this.spamEvent, true);
		
		index = 0;
		for( String word: words) {
			String word2 = word+index;
			BayesianEvent event = network.getEvent(word2);
			event.getTable().addLine(probabilityWordSpam(word), true, true);
			event.getTable().addLine(probabilityWordHam(word), true, false);
			query.defineEventType(event, EventType.Evidence);
			query.setEventValue(event, true);
			index++;
		}

		//query.setSampleSize(100000000);
		query.execute();
		System.out.println(query.toString());
		return 0;
		
	}
	
	public void run() {
		init();
		//System.out.println(probabilityWordSpam("sports"));
		//System.out.println(probabilityWordHam("sports"));
		//probabilitySpam("today");
		probabilitySpam("sports");
		//probabilitySpam("today is secret");
		//probabilitySpam("secret is secret");
	}
	
	public static final void main(String[] args) {
		BayesianSpam program = new BayesianSpam();
		program.run();
	}
	
}
