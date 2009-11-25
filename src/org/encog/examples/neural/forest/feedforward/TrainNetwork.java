package org.encog.examples.neural.forest.feedforward;

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
import org.encog.normalize.DataNormalization;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

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
	
	public void train(boolean useGUI)
	{
		System.out.println("Converting training file to binary");
		EncogPersistedCollection encog = new EncogPersistedCollection(Constant.TRAINED_NETWORK_FILE);
		DataNormalization norm = (DataNormalization) encog.find(Constant.NORMALIZATION_NAME);
		
		EncogUtility.convertCSV2Binary(Constant.NORMALIZED_FILE, Constant.BINARY_FILE, norm.getNetworkInputLayerSize(),norm.getNetworkOutputLayerSize(), false);
		BufferedNeuralDataSet trainingSet = new BufferedNeuralDataSet(Constant.BINARY_FILE);
		BasicNetwork network = EncogUtility.simpleFeedForward(norm.getNetworkInputLayerSize(), Constant.HIDDEN_COUNT, 0, norm.getNetworkOutputLayerSize(), false);

		if( useGUI)
		{
			EncogUtility.trainDialog(network, trainingSet);
		}
		else
		{
			EncogUtility.trainConsole(network, trainingSet, Constant.TRAINING_MINUTES);
		}

		System.out.println("Training complete, saving network...");
		encog.add(Constant.TRAINED_NETWORK_NAME, network);
	}

}
