package org.encog.examples.neural.forest;

import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.data.csv.CSVNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.logic.FeedforwardLogic;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.multi.MultiPropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class TrainNetwork {
	
	public static BasicNetwork generateNetwork(NeuralDataSet trainingSet)
	{
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,trainingSet.getInputSize()));
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,Constant.HIDDEN_COUNT));
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,trainingSet.getIdealSize()));
		network.setLogic(new FeedforwardLogic());
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	public void train()
	{		
		BufferedNeuralDataSet trainingSet = new BufferedNeuralDataSet(Constant.TRAINING_FILE);
		BasicNetwork network = generateNetwork(trainingSet);

		//final Train train = new ResilientPropagation(network, trainingSet);
		final Train train = new MultiPropagation(network, trainingSet );

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

}
