/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
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
 */

package org.encog.examples.neural.xorradial;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.RadialBasisFunctionLayer;
import org.encog.neural.pattern.RadialBasisPattern;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming. This example shows how to construct an Encog neural network to
 * predict the output from the XOR operator using a RBF neural network. This
 * example uses RPROP to train.
 * 
 * @author $Author$
 * @version $Revision$
 */
public class XorRadial {

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
		RadialBasisFunctionLayer rbfLayer = (RadialBasisFunctionLayer)network.getLayer(RadialBasisPattern.RBF_LAYER);

		// rbfLayer.setRadialBasisFunction(0, new GaussianFunction(0.0,1,0.5));
		// rbfLayer.setRadialBasisFunction(1, new GaussianFunction(0.25,1,0.5));
		// rbfLayer.setRadialBasisFunction(2, new GaussianFunction(0.5,1,0.5));
		// rbfLayer.setRadialBasisFunction(3, new GaussianFunction(1.0,1,0.5));
		rbfLayer.randomizeGaussianCentersAndWidths(0, 1);

		final NeuralDataSet trainingSet = new BasicNeuralDataSet(
				XorRadial.XOR_INPUT, XorRadial.XOR_IDEAL);

		// train the neural network
		EncogUtility.trainToError(network, trainingSet, 0.01);

		// test the neural network
		System.out.println("Neural Network Results:");
		EncogUtility.evaluate(network, trainingSet);
	}
}
