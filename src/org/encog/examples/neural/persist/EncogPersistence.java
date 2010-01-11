/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010, Heaton Research Inc., and individual contributors.
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
package org.encog.examples.neural.persist;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class EncogPersistence {
	
	public static final String FILENAME = "encogexample.eg";
	
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
		{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	
	public void trainAndSave()
	{
		System.out.println("Training XOR network to under 1% error rate.");
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(1));
		network.getStructure().finalizeStructure();
		network.reset();

		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		
		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);

		do {
			train.iteration();
		} while(train.getError() > 0.009);
		
		double e = network.calculateError(trainingSet);
		System.out.println("Network traiined to error: " + e);
		
		System.out.println("Saving network");
		final EncogPersistedCollection encog = new EncogPersistedCollection(
				FILENAME);
		encog.create();
		encog.add("network", network);
	}
	
	public void loadAndEvaluate()
	{
		System.out.println("Loading network");
		
		final EncogPersistedCollection encog = new EncogPersistedCollection(
				FILENAME);
		BasicNetwork network = (BasicNetwork)encog.find("network");
		
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		double e = network.calculateError(trainingSet);
		System.out.println("Loaded network's error is(should be same as above): " + e);
	}
	
	public static void main(String[] args)
	{
		Logging.stopConsoleLogging();
		
		try
		{
			EncogPersistence program = new EncogPersistence();
			program.trainAndSave();
			program.loadAndEvaluate();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		
	}
}
