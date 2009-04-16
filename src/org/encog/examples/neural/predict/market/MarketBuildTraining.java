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
	
	public static final int INPUT_WINDOW = 10;
	public static final int PREDICT_WINDOW = 1;
	public static final TickerSymbol TICKER = new TickerSymbol("AAPL");
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		MarketLoader loader = new YahooFinanceLoader();
		MarketNeuralDataSet market = new MarketNeuralDataSet(
				loader, 
				MarketBuildTraining.INPUT_WINDOW,
				MarketBuildTraining.PREDICT_WINDOW);
		MarketDataDescription desc = new MarketDataDescription(
				MarketBuildTraining.TICKER, 
				MarketDataType.ADJUSTED_CLOSE, 
				true,
				true);
		market.addDescription(desc);
		
		Calendar end = new GregorianCalendar();
		end.add(Calendar.DATE, -30);
		
		Calendar begin = (Calendar)end.clone();
		begin.add(Calendar.YEAR, -5);
				
		market.load(begin.getTime(), end.getTime());
		market.generate();
		market.setDescription("Market data for: " + MarketBuildTraining.TICKER.getSymbol());
		
		// create a network
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(market.getInputSize()));
		network.addLayer(new BasicLayer(10));
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
