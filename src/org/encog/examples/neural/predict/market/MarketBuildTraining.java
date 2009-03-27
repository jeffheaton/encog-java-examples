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
import org.encog.neural.persist.EncogPersistedCollection;
import org.encog.util.Logging;
import org.encog.util.time.DateUtil;

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
		EncogPersistedCollection encog = new EncogPersistedCollection(Config.FILENAME);
		encog.create();
		encog.add("market",market);
				
	}
}
