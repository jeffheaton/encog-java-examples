/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.neural.recurrent;

import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.logic.FeedforwardLogic;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.pattern.ElmanPattern;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.JordanPattern;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming.  This example shows how to construct an Encog neural
 * network to predict the output from the XOR operator.  This example
 * uses backpropagation to train the neural network.
 * 
 * @author $Author$
 * @version $Revision$
 */
public class TemporalString {

	public final static String DATA = "0010111!00101010111!00110000111!0010010111!01001";
	private double[][] input;
	private double[][] ideal;
	
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
		return pattern.generate();
	}
	
	public BasicNetwork createElmann()
	{
		ElmanPattern pattern = new ElmanPattern();
		pattern.setInputNeurons(this.input[0].length);
		pattern.setOutputNeurons(this.ideal[0].length);
		pattern.addHiddenLayer(16);
		pattern.setActivationFunction(new ActivationSigmoid());
		return pattern.generate();
	}
	
	public BasicNetwork createJordan()
	{
		JordanPattern pattern = new JordanPattern();
		pattern.setInputNeurons(this.input[0].length);
		pattern.setOutputNeurons(this.ideal[0].length);
		pattern.addHiddenLayer(16);
		pattern.setActivationFunction(new ActivationSigmoid());
		return pattern.generate();
	}
	
	public void run()
	{
		Logging.stopConsoleLogging();
		
		generateTraining();
		
		//BasicNetwork network = createFeedForward();
		//BasicNetwork network = createElmann();
		BasicNetwork network = createJordan();
		
		NeuralDataSet trainingSet = new BasicNeuralDataSet(this.input, this.ideal);
		
		// train the neural network
		final Train train = new Backpropagation(network, trainingSet, 0.000001, 0.0);

		EncogUtility.trainDialog(network, trainingSet);
		
		EncogUtility.evaluate(network, trainingSet);
		
	}
	
	public static void main(final String args[]) {
		TemporalString test = new TemporalString();
		test.run();
	}
}
