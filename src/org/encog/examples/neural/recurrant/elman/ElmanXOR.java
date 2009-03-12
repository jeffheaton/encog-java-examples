package org.encog.examples.neural.recurrant.elman;

import org.encog.examples.neural.util.TemporalXOR;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.ContextLayer;
import org.encog.neural.networks.layers.Layer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.backpropagation.Backpropagation;

public class ElmanXOR {
	
	
	
	public static void main(String args[])
	{
		TemporalXOR temp = new TemporalXOR();
		NeuralDataSet trainingSet = temp.generate(3000);
		
		// construct an Elman type network
		Layer hidden;
		Layer context = new ContextLayer(2);
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(1));
		network.addLayer(hidden = new BasicLayer(2));
		hidden.addNext(context);
		context.addNext(hidden);
		network.addLayer(new BasicLayer(1));
		network.reset();
		
		// train the neural network
		final Train train = new Backpropagation(network, trainingSet,
				0.0001, 0.1);

		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while ((epoch < 5000) && (train.getError() > 0.001));
		
	}
}
