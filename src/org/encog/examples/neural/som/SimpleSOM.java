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

public class SimpleSOM {
	
	public static double SOM_INPUT[][] = { 
		{ 0.0, 0.0, 1.0, 1.0 }, 
		{ 1.0, 1.0, 0.0, 0.0 } };
	
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
		network.reset();
		
		CompetitiveTraining train = new CompetitiveTraining(
				network,
				0.4,
				training,
				new NeighborhoodSingle());
				
		int iteration = 0;
		
		for(iteration = 0;iteration<=100;iteration++)
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
