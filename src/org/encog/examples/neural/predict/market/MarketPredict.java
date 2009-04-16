package org.encog.examples.neural.predict.market;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.market.MarketDataDescription;
import org.encog.neural.data.market.MarketDataType;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.data.market.loader.MarketLoader;
import org.encog.neural.data.market.loader.YahooFinanceLoader;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogPersistedCollection;


public class MarketPredict {
	
	
	public static MarketNeuralDataSet grabData()
	{
		MarketLoader loader = new YahooFinanceLoader();
		MarketNeuralDataSet result = new MarketNeuralDataSet(
				loader, 
				MarketBuildTraining.INPUT_WINDOW,
				MarketBuildTraining.PREDICT_WINDOW);
		MarketDataDescription desc = new MarketDataDescription(
				MarketBuildTraining.TICKER, 
				MarketDataType.ADJUSTED_CLOSE, 
				true,
				true);
		result.addDescription(desc);
		
		Calendar end = new GregorianCalendar();// end today		
		Calendar begin = (Calendar)end.clone();// begin 30 days ago
		begin.add(Calendar.DATE, -30);
		
		result.load(begin.getTime(), end.getTime());
		result.generate();
		
		return result;

	}
	
	public static void main(String args[])
	{
		//EncogPersistedCollection encog = new EncogPersistedCollection();
		//encog.load("marketdata.eg");
		//BasicNetwork network = (BasicNetwork) encog.find("market-network");
/*		MarketNeuralDataSet data = grabData();
		
		for(NeuralDataPair pair: data)
		{
			NeuralData input = pair.getInput();
			NeuralData actual = pair.getIdeal();
			
			NeuralData output = network.compute(input);			
			
			System.out.println(actual.getData(0)+"," + output.getData(0));
			
		}*/
		
	}
	
}
