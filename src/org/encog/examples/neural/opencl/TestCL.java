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

package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.network.train.prop.OpenCLTrainingProfile;
import org.encog.engine.opencl.EncogCLDevice;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.strategy.RequiredImprovementStrategy;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;


public class TestCL {

	public static final int INPUT_SIZE = 100;
	public static final int HIDDEN1 = 200;
	public static final int HIDDEN2 = 40;
	public static final int IDEAL_SIZE = 5;

	public static final int TRAINING_SIZE = 10000;
	
	public static void main(final String args[]) {
		Logging.stopConsoleLogging();
		
		NeuralDataSet trainingSet = RandomTrainingFactory.generate(1000,
				TRAINING_SIZE, INPUT_SIZE, IDEAL_SIZE, -1, 1);
		
		BasicNetwork network = EncogUtility.simpleFeedForward(INPUT_SIZE, HIDDEN1, HIDDEN2, IDEAL_SIZE, true);
		network.reset();
		
		Encog.getInstance().initCL();

		
		// train the neural network
		EncogCLDevice device = Encog.getInstance().getCL().chooseDevice();
		OpenCLTrainingProfile profile = new OpenCLTrainingProfile(device);

		System.out.println("OpenCL device used: " + profile.getDevice().toString());

		final Propagation train = new ResilientPropagation(network, trainingSet, profile);
		
		EncogUtility.trainToError(train, network, trainingSet, 0.01);

	}
}
