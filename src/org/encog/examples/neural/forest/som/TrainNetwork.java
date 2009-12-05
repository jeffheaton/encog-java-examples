package org.encog.examples.neural.forest.som;

import org.encog.examples.neural.gui.som.MapPanel;
import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.logic.FeedforwardLogic;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodGaussianMulti;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodSingle;
import org.encog.neural.pattern.SOMPattern;
import org.encog.normalize.DataNormalization;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.simple.EncogUtility;

public class TrainNetwork {
	public static BasicNetwork generateNetwork(NeuralDataSet trainingSet)
	{
		SOMPattern pattern = new SOMPattern();		 	
		pattern.setInputNeurons(trainingSet.getInputSize());
		pattern.setOutputNeurons(Constant.OUTPUT_COUNT);
		BasicNetwork result = pattern.generate();
		result.reset();
		return result;
	}
	
	public void train(boolean useGUI)
	{
		System.out.println("Converting training file to binary...");
		EncogPersistedCollection encog = new EncogPersistedCollection(Constant.TRAINED_NETWORK_FILE);
		DataNormalization norm = (DataNormalization) encog.find(Constant.NORMALIZATION_NAME);
		
		//EncogUtility.convertCSV2Binary(Constant.NORMALIZED_FILE, Constant.BINARY_FILE, norm.getNetworkInputLayerSize(),norm.getNetworkOutputLayerSize(), false);
		BufferedNeuralDataSet trainingSet = new BufferedNeuralDataSet(Constant.BINARY_FILE);
		
		System.out.println("Beginning training...");
		BasicNetwork network = generateNetwork(trainingSet);

		NeighborhoodGaussianMulti neighborhood = new NeighborhoodGaussianMulti(Constant.OUTPUT_COUNT,Constant.OUTPUT_COUNT,1,5,0);
		CompetitiveTraining train = new CompetitiveTraining(
				network,
				0.1,
				trainingSet,
				neighborhood);
		train.setForceWinner(false);
				
		int iteration = 0;

		train.setAutoDecay(10, 0.3, 0.1, 6, 1);
		for(iteration = 0;iteration<=10;iteration++)
		{
			train.iteration();
			train.autoDecay();
			System.out.println("Iteration: " + iteration + ", Error:" + train.getError());
		}

		System.out.println("Training complete, saving network...");
		encog.add(Constant.TRAINED_NETWORK_NAME, network);
	}

}
