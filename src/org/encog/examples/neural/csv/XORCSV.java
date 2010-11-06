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

package org.encog.examples.neural.csv;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.csv.CSVFormat;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;
import org.encog.util.simple.TrainingSetUtil;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming.  This example shows how to construct an Encog neural
 * network to predict the output from the XOR operator.  This example
 * uses resilient propagation (RPROP) to train the neural network.
 * RPROP is the best general purpose supervised training method provided by
 * Encog.
 * 
 * For the XOR example with RPROP I use 4 hidden neurons.  XOR can get by on just
 * 2, but often the random numbers generated for the weights are not enough for
 * RPROP to actually find a solution.  RPROP can have issues on really small
 * neural networks, but 4 neurons seems to work just fine.
 * 
 * This example reads the XOR data from a CSV file.  This file should be something like:
 * 
 * 0,0,0
 * 1,0,1
 * 0,1,1
 * 1,1,0
 */
public class XORCSV {

	public static void main(final String args[]) {
		
		Logging.stopConsoleLogging();
		NeuralDataSet trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, "d:\\xor.csv", false, 2, 1);
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 4, 0, 1, true);
		
		System.out.println();
		System.out.println("Training Network");
		EncogUtility.trainToError(network, trainingSet, 0.01);
		
		System.out.println();
		System.out.println("Evaluating Network");
		EncogUtility.evaluate(network, trainingSet);
	}
}
