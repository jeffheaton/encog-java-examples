package org.encog.examples.neural.predict.market;

import java.io.File;

import org.encog.ConsoleStatusReportable;
import org.encog.neural.activation.ActivationTANH;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.prune.PruneIncremental;
import org.encog.neural.prune.PruneSelective;
import org.encog.persist.EncogPersistedCollection;

public class MarketPrune {
		
	public static void incremental()
	{
		File file = new File(Config.FILENAME);
		
		if( !file.exists() )
		{
			System.out.println("Can't read file: " + file.getAbsolutePath() );
			return;
		}
		
		EncogPersistedCollection encog = new EncogPersistedCollection(file);					
		//BasicNetwork network = (BasicNetwork) encog.find(Config.MARKET_NETWORK);
		NeuralDataSet training = (NeuralDataSet)encog.find(Config.MARKET_TRAIN);
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(training.getInputSize());
		pattern.setOutputNeurons(training.getIdealSize());
		pattern.setActivationFunction(new ActivationTANH());
		
		PruneIncremental prune = new PruneIncremental(
				training,
				pattern,
				100,
				new ConsoleStatusReportable());
		
		prune.addHiddenLayer(5, 50);
		prune.addHiddenLayer(0, 50);
		
		prune.process();
		
		encog.add(Config.MARKET_NETWORK, prune.getBestNetwork());

	}
}
