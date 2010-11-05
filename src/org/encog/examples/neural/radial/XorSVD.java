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

package org.encog.examples.neural.radial;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.svd.SVDTraining;
import org.encog.neural.pattern.RadialBasisPattern;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming. This example shows how to construct an Encog neural network to
 * predict the output from the XOR operator using a RBF neural network. This
 * example uses RPROP to train.
 * 
 * This example uses an RBF neural network with an SVD trainer.
 */
public class XorSVD {

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) {

		Logging.stopConsoleLogging();

		RadialBasisPattern pattern = new RadialBasisPattern();
		pattern.setInputNeurons(2);
		pattern.addHiddenLayer(4);
		pattern.setOutputNeurons(1);
		BasicNetwork network = pattern.generate();

		network.getStructure().finalizeStructure();

		final NeuralDataSet trainingSet = new BasicNeuralDataSet(
				XorSVD.XOR_INPUT, XorSVD.XOR_IDEAL);

		SVDTraining train = new SVDTraining(network,trainingSet);
		// train the neural network
		EncogUtility.trainToError(train, network, trainingSet, 0.01);

		// test the neural network
		System.out.println("Neural Network Results:");
		EncogUtility.evaluate(network, trainingSet);
	}
}
