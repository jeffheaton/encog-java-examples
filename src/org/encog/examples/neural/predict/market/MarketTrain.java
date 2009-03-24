package org.encog.examples.neural.predict.market;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.backpropagation.Backpropagation;
import org.encog.neural.persist.EncogPersistedCollection;

public class MarketTrain {
	
	public final static int TRAINING_MINUTES = 1440;
	
	public static void main(String args[])
	{
/*		EncogPersistedCollection encog = new EncogPersistedCollection();
		encog.load("marketdata.eg");
		NeuralDataSet trainingSet = (NeuralDataSet) encog.find("market");
				
		
		BasicNetwork network = (BasicNetwork) encog.find("market-network");
		
		// train the neural network
		final Train train = new Backpropagation(network, trainingSet, 0.00001, 0.1);

		int epoch = 1;
		long startTime = System.currentTimeMillis();
		int left = 0;

		do {
			int running = (int)((System.currentTimeMillis()-startTime)/60000);
			left = MarketTrain.TRAINING_MINUTES - running;
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + (train.getError()*100.0)+"%,"
							+" Time Left: " + left + " Minutes");
			epoch++;
		} while ((left>=0) && (train.getError() > 0.001));
		
		//encog.delete("market-network");
		
		network.setName("market-network");
		network.setDescription("Trained neural network");
		encog.add(network);
		encog.save("marketdata.eg");
		*/
	}
}
