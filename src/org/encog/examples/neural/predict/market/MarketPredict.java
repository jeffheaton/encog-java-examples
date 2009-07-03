/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.encog.examples.neural.predict.market;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.text.NumberFormatter;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.market.MarketDataDescription;
import org.encog.neural.data.market.MarketDataType;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.data.market.loader.MarketLoader;
import org.encog.neural.data.market.loader.YahooFinanceLoader;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;


/**
 * Use the saved market neural network, and now attempt to predict for today, and the
 * last 60 days and see what the results are.
 */
public class MarketPredict {
		
	
	enum Direction {
		up,
		down		
	};
	
	public static Direction determineDirection(double d)
	{
		if( d<0 )
			return Direction.down;
		else
			return Direction.up;
	}
	
	
	public static MarketNeuralDataSet grabData()
	{
		MarketLoader loader = new YahooFinanceLoader();
		MarketNeuralDataSet result = new MarketNeuralDataSet(
				loader, 
				Config.INPUT_WINDOW,
				Config.PREDICT_WINDOW);
		MarketDataDescription desc = new MarketDataDescription(
				Config.TICKER, 
				MarketDataType.ADJUSTED_CLOSE, 
				true,
				true);
		result.addDescription(desc);
		
		Calendar end = new GregorianCalendar();// end today		
		Calendar begin = (Calendar)end.clone();// begin 30 days ago
		begin.add(Calendar.DATE, -60);
		
		result.load(begin.getTime(), end.getTime());
		result.generate();
		
		return result;

	}
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		
		File file = new File(Config.FILENAME);
		
		if( !file.exists() )
		{
			System.out.println("Can't read file: " + file.getAbsolutePath() );
			return;
		}
		
		EncogPersistedCollection encog = new EncogPersistedCollection(file);					
		BasicNetwork network = (BasicNetwork) encog.find(Config.MARKET_NETWORK);
		
		if( network==null )
		{
			System.out.println("Can't find network resource: " + Config.MARKET_NETWORK );
			return;
		}
				
		MarketNeuralDataSet data = grabData();
		
		DecimalFormat format = new DecimalFormat("#0.00");
		
		int count = 0;
		int correct = 0;
		for(NeuralDataPair pair: data)
		{
			NeuralData input = pair.getInput();
			NeuralData actualData = pair.getIdeal();			
			NeuralData predictData = network.compute(input);
			
			double actual = actualData.getData(0);
			double predict = predictData.getData(0);			
			double diff = Math.abs(predict-actual);
			
			Direction actualDirection = determineDirection(actual);
			Direction predictDirection = determineDirection(predict);
			
			if( actualDirection==predictDirection )
				correct++;
			
			count++;
						
			System.out.println("Day " + count+":actual="
					+format.format(actual)+"("+actualDirection+")"
					+",predict=" 
					+format.format(predict)+"("+actualDirection+")"
					+",diff="+diff);			
			
		}
		double percent = (double)correct/(double)count;
		System.out.println("Direction correct:" + correct + "/" + count);
		System.out.println("Directional Accuracy:"+format.format(percent*100)+"%");
		
	}
	
}
