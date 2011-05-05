/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
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
package org.encog.examples.neural.recurrent;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.JordanPattern;
import org.encog.util.Format;
import org.encog.util.simple.EncogUtility;

/**
 * This is a very simple example that shows where a temporal neural network, in
 * this case an Jordan neural network, can be useful. A binary message is
 * constantly coming into the neural network. The neural network has a single
 * input neuron and a single output neuron. The network should detect a
 * "distress signal" in the stream of bits. A distress signal is defined to be
 * three 1's, or "111".
 * 
 * Training data is provided with some sample signals, and several distress
 * signals. The "!" character is placed after each distress signal, for
 * training. The neural network should produce a "1" on the same cycle as the
 * final "1" in the distress signal.
 * 
 * An Jordan neural network becomes quite good at detecting the signal.
 * Feedforward is never really able to do it. A feedforward is only looking at
 * one bit at a time, and because the distress signal spans 3 bits, the neural
 * network must have some sort of short term memory, to remember the past few
 * patterns. A short-term memory is exactly what an Jordan neural network has.
 */
public class TemporalString {

	public final static String DATA = "000000111!00000111!000000";
	private double[][] input;
	private double[][] ideal;

	/**
	 * Generate training from the input sequence. The input sequence is a string
	 * of 0's and 1's. Three 1's("111") is a "distress signal", and is indicated
	 * by a "!".
	 */
	public void generateTraining()
	{
		// first figure out the length
		int length = DATA.length();
		
		for(int i=0;i<DATA.length();i++) {
			char ch = DATA.charAt(i);
			
			switch(ch) {
				case '1':
				case '0':
					// don't care
					break;
				case '!':
					length--;
					break;
				default:
					System.out.println("Bad format!");
					System.exit(0);
			}
		}
		
		// allocate input and ideal
		this.input = new double[length][1];
		this.ideal = new double[length][1];
		
		int index = 0;
		for(int i=0;i<DATA.length();i++)
		{
			char ch = DATA.charAt(i);
			switch(ch)
			{
				case '1':
					this.input[index][0] = 1.0;
					this.ideal[index][0] = 0.0;
					index++;
					break;
				case '0':
					this.input[index][0] = 0.0;
					this.ideal[index][0] = 0.0;
					index++;
					break;
				case '!':
					this.ideal[index-1][0] = 1.0;
					break;
					
			}
		}
	}
	
	

	public BasicNetwork createFeedForward()
	{
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(this.input[0].length);
		pattern.setOutputNeurons(this.ideal[0].length);
		pattern.addHiddenLayer(8);
		pattern.setActivationFunction(new ActivationSigmoid());
		return (BasicNetwork)pattern.generate();
	}
		
	public BasicNetwork createJordan()
	{
		JordanPattern pattern = new JordanPattern();
		pattern.setInputNeurons(this.input[0].length);
		pattern.setOutputNeurons(this.ideal[0].length);
		pattern.addHiddenLayer(16);
		pattern.setActivationFunction(new ActivationSigmoid());
		return (BasicNetwork)pattern.generate();
	}
	
	public void run()
	{

		generateTraining();
		
		BasicNetwork ffNetwork = createFeedForward();	
		BasicNetwork jordanNetwork = createJordan();
		
		MLDataSet trainingSet = new BasicMLDataSet(this.input, this.ideal);
		
		// train the neural network
		EncogUtility.trainConsole( jordanNetwork, trainingSet, 1);
		EncogUtility.trainConsole( ffNetwork, trainingSet, 1);
		
		System.out.println("Final Jordan Error: " + Format.formatPercent(jordanNetwork.calculateError(trainingSet)));
		System.out.println("Final Feedforwd Error: " + Format.formatPercent(jordanNetwork.calculateError(trainingSet)));
		
		System.out.println("However, the error rate can be misleading.  Consider the evaluations of each network.");
		
		System.out.println("Feedforward Evaluation:");
		EncogUtility.evaluate(ffNetwork, trainingSet);
		
		System.out.println("Jordan Evaluation:");
		EncogUtility.evaluate(jordanNetwork, trainingSet);
		
		
	}
	
	public static void main(final String args[]) {
		TemporalString test = new TemporalString();
		test.run();
	}
}
