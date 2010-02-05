/*
 * Encog(tm) Artificial Intelligence Framework v2.3
 * Java Examples
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

package org.encog.examples.neural.benchmark;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.logging.Logging;

public class MultiBench {
	
	public static final int INPUT_COUNT = 40;
	public static final int HIDDEN_COUNT = 60;
	public static final int OUTPUT_COUNT = 20;
	
	public static BasicNetwork generateNetwork()
	{
		final BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(MultiBench.INPUT_COUNT));
		network.addLayer(new BasicLayer(MultiBench.HIDDEN_COUNT));
		network.addLayer(new BasicLayer(MultiBench.OUTPUT_COUNT));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	public static NeuralDataSet generateTraining()
	{
		final NeuralDataSet training = RandomTrainingFactory.generate(50000,
				INPUT_COUNT, OUTPUT_COUNT, -1, 1);
		return training;
	}
	
	public static double evaluateRPROP(BasicNetwork network,NeuralDataSet data)
	{

		ResilientPropagation train = new ResilientPropagation(network,data);
		long start = System.currentTimeMillis();
		System.out.println("Training 20 Iterations with RPROP");
		for(int i=1;i<=20;i++)
		{
			train.iteration();
			System.out.println("Iteration #" + i + " Error:" + train.getError());
		}
		train.finishTraining();
		long stop = System.currentTimeMillis();
		double diff = ((double)(stop - start))/1000.0;
		System.out.println("RPROP Result:" + diff + " seconds." );
		System.out.println("Final RPROP error: " + network.calculateError(data));
		return diff;
	}
	
	public static double evaluateMPROP(BasicNetwork network,NeuralDataSet data)
	{

		ResilientPropagation train = new ResilientPropagation(network,data);
		train.setNumThreads(0);
		long start = System.currentTimeMillis();
		System.out.println("Training 20 Iterations with MPROP");
		for(int i=1;i<=20;i++)
		{
			train.iteration();
			System.out.println("Iteration #" + i + " Error:" + train.getError());
		}
		train.finishTraining();
		long stop = System.currentTimeMillis();
		double diff = ((double)(stop - start))/1000.0;
		System.out.println("MPROP Result:" + diff + " seconds." );
		System.out.println("Final MPROP error: " + network.calculateError(data));
		return diff;
	}
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		BasicNetwork network = generateNetwork();
		NeuralDataSet data = generateTraining();
		
		double rprop = evaluateRPROP(network,data);
		double mprop = evaluateMPROP(network,data);
		double factor = rprop/mprop;
		System.out.println("Factor improvement:" + factor);
	}
}
