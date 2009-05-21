package org.encog.examples.neural.predict.market;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class MarketTrain {
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		
		EncogPersistedCollection encog = new EncogPersistedCollection(Config.FILENAME);
		NeuralDataSet trainingSet = (NeuralDataSet) encog.find(Config.MARKET_TRAIN);
					
		BasicNetwork network = (BasicNetwork) encog.find(Config.MARKET_NETWORK);
		
		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);
		//final Train train = new Backpropagation(network, trainingSet, 0.0001, 0.0);

	
		
		int epoch = 1;
		long startTime = System.currentTimeMillis();
		int left = 0;

		do {
			int running = (int)((System.currentTimeMillis()-startTime)/60000);
			left = Config.TRAINING_MINUTES - running;
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + (train.getError()*100.0)+"%,"
							+" Time Left: " + left + " Minutes");
			epoch++;
		} while ((left>=0) && (train.getError() > 0.001));
				
		network.setDescription("Trained neural network");
		encog.add(Config.MARKET_NETWORK,network);
		
	}
}
