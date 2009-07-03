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

package org.encog.examples.neural.som;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.encog.neural.activation.ActivationBiPolar;
import org.encog.neural.activation.ActivationLinear;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodSingle;
import org.encog.util.logging.Logging;
import org.encog.util.randomize.RangeRandomizer;

/**
 * Implement a simple SOM using Encog.  It learns to recognize two patterns.
 * @author jeff
 *
 */
public class SimpleSOM {
	
	public static double SOM_INPUT[][] = { 
		{ -1.0, -1.0, 1.0, 1.0 }, 
		{ 1.0, 1.0, -1.0, -1.0 } };
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		
		// create the training set
		NeuralDataSet training = new BasicNeuralDataSet(SOM_INPUT,null);
		
		// Create the neural network.
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationLinear(),false,4));
		network.addLayer(new BasicLayer(new ActivationLinear(),false,2));
		network.getStructure().finalizeStructure();
		(new RangeRandomizer(-0.5, 0.5)).randomize(network);
		
		CompetitiveTraining train = new CompetitiveTraining(
				network,
				0.7,
				training,
				new NeighborhoodSingle());
				
		int iteration = 0;
		
		for(iteration = 0;iteration<=10;iteration++)
		{
			train.iteration();
			System.out.println("Iteration: " + iteration + ", Error:" + train.getError());
		}
		
		NeuralData data1 = new BasicNeuralData(SOM_INPUT[0]);
		NeuralData data2 = new BasicNeuralData(SOM_INPUT[1]);
		System.out.println("Pattern 1 winner: " + network.winner(data1));
		System.out.println("Pattern 2 winner: " + network.winner(data2));
	}
}
