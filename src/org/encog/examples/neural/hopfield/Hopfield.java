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

package org.encog.examples.neural.hopfield;

import org.encog.neural.activation.ActivationBiPolar;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.hopfield.TrainHopfield;
import org.encog.util.logging.Logging;

/**
 * ConsoleHopfield: Simple console application that shows how to use a Hopfield
 * Neural Network.
 */
public class Hopfield {

	/**
	 * Convert a boolean array to the form [T,T,F,F]
	 * 
	 * @param b
	 *            A boolen array.
	 * @return The boolen array in string form.
	 */
	public static String formatBoolean(final NeuralData b) {
		final StringBuilder result = new StringBuilder();
		result.append('[');
		for (int i = 0; i < b.size(); i++) {
			if (b.getData(i) > 0) {
				result.append("T");
			} else {
				result.append("F");
			}
			if (i != b.size() - 1) {
				result.append(",");
			}
		}
		result.append(']');
		return (result.toString());
	}

	/**
	 * A simple main method to test the Hopfield neural network.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(final String args[]) {

		Logging.stopConsoleLogging();

		// Create the neural network.
		BasicLayer hopfield;
		final BasicNetwork network = new BasicNetwork();
		network.addLayer(hopfield = new BasicLayer(new ActivationBiPolar(),
				false, 4));
		hopfield.addNext(hopfield);
		// This pattern will be trained
		final boolean[] pattern1 = { true, true, false, false };
		// This pattern will be presented
		final boolean[] pattern2 = { true, false, false, false };
		NeuralData result;

		final BiPolarNeuralData data1 = new BiPolarNeuralData(pattern1);
		final BiPolarNeuralData data2 = new BiPolarNeuralData(pattern2);
		final BasicNeuralDataSet set = new BasicNeuralDataSet();
		set.add(data1);
		network.getStructure().finalizeStructure();

		// train the neural network with pattern1
		System.out.println("Training Hopfield network with: "
				+ Hopfield.formatBoolean(data1));

		final TrainHopfield train = new TrainHopfield(set, network);
		train.iteration();
		// present pattern1 and see it recognized
		result = network.compute(data1);
		System.out.println("Presenting pattern:"
				+ Hopfield.formatBoolean(data1) + ", and got "
				+ Hopfield.formatBoolean(result));
		// Present pattern2, which is similar to pattern 1. Pattern 1
		// should be recalled.
		result = network.compute(data2);
		System.out.println("Presenting pattern:"
				+ Hopfield.formatBoolean(data2) + ", and got "
				+ Hopfield.formatBoolean(result));

	}

}
