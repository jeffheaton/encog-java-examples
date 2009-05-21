package org.encog.examples.neural.predict.market;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.encog.neural.data.market.TickerSymbol;

public class Config {
	public static final String FILENAME = "marketdata.eg";
	public static final String MARKET_NETWORK = "market-network";
	public static final String MARKET_TRAIN = "market-train";
	public static final int TRAINING_MINUTES = 15;
	public static final int HIDDEN1_COUNT = 20;
	public static final int HIDDEN2_COUNT = 0;
	public static final Calendar TRAIN_BEGIN = new GregorianCalendar(2000,0,1);
	public static final Calendar TRAIN_END = new GregorianCalendar(2008,12,31);
	public static final int INPUT_WINDOW = 10;
	public static final int PREDICT_WINDOW = 1;
	public static final TickerSymbol TICKER = new TickerSymbol("AAPL");
}
