package org.encog.examples.neural.forest;

import java.io.File;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.normalize.Normalization;
import org.encog.persist.EncogPersistedCollection;

public class Evaluate {
	
	public BasicNetwork loadNetwork()
	{
		File file = Constant.TRAINED_NETWORK_FILE;
		
		if( !file.exists() )
		{
			System.out.println("Can't read file: " + file.getAbsolutePath() );
			return null;
		}
		
		EncogPersistedCollection encog = new EncogPersistedCollection(file);					
		BasicNetwork network = (BasicNetwork) encog.find(Constant.TRAINED_NETWORK_NAME);
		Normalization norm = (Normalization) encog.find(Constant.NORMALIZATION_NAME);
		System.out.println(norm);
		if( network==null )
		{
			System.out.println("Can't find network resource: " + Constant.TRAINED_NETWORK_NAME );
			return null;
		}
				
		return network;
	}
	
	public NeuralDataSet loadData()
	{
		BufferedNeuralDataSet trainingSet = new BufferedNeuralDataSet(Constant.EVAL_FILE);
		return trainingSet;
	}
	
	public void evaluate()
	{
		BasicNetwork network = loadNetwork();
		NeuralDataSet data = loadData();
		System.out.println(network.calculateError(data));
	}
}
