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

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.logic.HopfieldLogic;
import org.encog.neural.pattern.HopfieldPattern;
import org.encog.util.logging.Logging;

/**
 * ConsoleHopfield: Simple console application that shows how to
 * use a Hopfield Neural Network.
 */
public class Hopfield {

	/**
	 * A simple main method to test the Hopfield neural network.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(final String args[]) {

		Logging.stopConsoleLogging();

		// Create the neural network.
		HopfieldPattern pattern = new HopfieldPattern();
		pattern.setInputNeurons(4);
		BasicNetwork hopfield = pattern.generate();
		HopfieldLogic hopfieldLogic = (HopfieldLogic)hopfield.getLogic();

		
		// This pattern will be trained
		final boolean[] pattern1 = { true, true, false, false };
		// This pattern will be presented
		final boolean[] pattern2 = { true, false, false, false };
		NeuralData result;
		
		BiPolarNeuralData data1 = new BiPolarNeuralData(pattern1);
		BiPolarNeuralData data2 = new BiPolarNeuralData(pattern2);
		
		hopfieldLogic.addPattern(data1);

		// train the neural network with pattern1
		System.out.println("Training Hopfield network with: "
				+ data1.toString());

		System.out.println("Network energy: " + hopfieldLogic.calculateEnergy());
		
		// present pattern1 and see it recognized
		hopfieldLogic.setCurrentState(data1);
		hopfieldLogic.run();
		result = hopfieldLogic.getCurrentState();
		System.out.println("Presenting pattern:" + data1.toString()
				+ ", and got " + result.toString());
		System.out.println("Network energy: " + hopfieldLogic.calculateEnergy());
		
		// Present pattern2, which is similar to pattern 1. Pattern 1
		// should be recalled.
		hopfieldLogic.setCurrentState(data2);
		hopfieldLogic.run();
		result = hopfieldLogic.getCurrentState();
		System.out.println("Presenting pattern:" + data2.toString()
				+ ", and got " + result.toString());
		System.out.println("Network energy: " + hopfieldLogic.calculateEnergy());

	}

}
