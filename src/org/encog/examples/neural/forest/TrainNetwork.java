package org.encog.examples.neural.forest;

import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.data.csv.CSVNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.logic.FeedforwardLogic;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class TrainNetwork {
	
	public static BasicNetwork generateNetwork()
	{
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,10));
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,20));
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,7));
		network.setLogic(new FeedforwardLogic());
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	public void train()
	{
		BasicNetwork network = generateNetwork();
		BufferedNeuralDataSet trainingSet = new BufferedNeuralDataSet(Constant.BUFFER_FILE);

		final Train train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;
		long remaining;

		System.out.println("Beginning training...");
		long start = System.currentTimeMillis();
		do {
			train.iteration();
			
			long current = System.currentTimeMillis();
			long elapsed = (current-start)/1000;// seconds
			elapsed/=60;// minutes
			remaining = Constant.TRAINING_MINUTES - elapsed;
			
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError() + " remaining minutes = " +remaining);
			epoch++;
		} while(remaining>0);
		
		EncogPersistedCollection encog = new EncogPersistedCollection(Constant.TRAINED_NETWORK_FILE);
		encog.add(Constant.TRAINED_NETWORK_NAME, network);
	}
	
	public static void main(String[] args)
	{
		Logging.stopConsoleLogging();
		TrainNetwork program = new TrainNetwork();
		program.train();
	}
}
