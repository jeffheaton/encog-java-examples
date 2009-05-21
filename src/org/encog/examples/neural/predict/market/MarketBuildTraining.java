package org.encog.examples.neural.predict.market;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.encog.neural.data.market.MarketDataDescription;
import org.encog.neural.data.market.MarketDataType;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.data.market.TickerSymbol;
import org.encog.neural.data.market.loader.MarketLoader;
import org.encog.neural.data.market.loader.YahooFinanceLoader;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class MarketBuildTraining {
		
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		MarketLoader loader = new YahooFinanceLoader();
		MarketNeuralDataSet market = new MarketNeuralDataSet(
				loader, 
				Config.INPUT_WINDOW,
				Config.PREDICT_WINDOW);
		MarketDataDescription desc = new MarketDataDescription(
				Config.TICKER, 
				MarketDataType.ADJUSTED_CLOSE, 
				true,
				true);
		market.addDescription(desc);
				
		market.load(Config.TRAIN_BEGIN.getTime(), Config.TRAIN_END.getTime());
		market.generate();
		market.setDescription("Market data for: " + Config.TICKER.getSymbol());
		
		// create a network
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(market.getInputSize()));
		network.addLayer(new BasicLayer(Config.HIDDEN1_COUNT));
		if( Config.HIDDEN2_COUNT!=0)
			network.addLayer(new BasicLayer(Config.HIDDEN2_COUNT));
		network.addLayer(new BasicLayer(market.getIdealSize()));
		network.getStructure().finalizeStructure();
		network.reset();		
		
		// save the network and the training
		EncogPersistedCollection encog = new EncogPersistedCollection(Config.FILENAME);
		encog.create();
		encog.add(Config.MARKET_TRAIN,market);
		encog.add(Config.MARKET_NETWORK, network);

				
	}
}
