/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
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
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.neural.predict.market;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.encog.ml.data.market.MarketDataDescription;
import org.encog.ml.data.market.MarketDataType;
import org.encog.ml.data.market.MarketMLDataSet;
import org.encog.ml.data.market.loader.MarketLoader;
import org.encog.ml.data.market.loader.YahooFinanceLoader;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.simple.EncogUtility;

/**
 * Build the training data for the prediction and store it in an Encog file for
 * later training.
 * 
 * @author jeff
 * 
 */
public class MarketBuildTraining {

	public static void generate(File dataDir) {
		
		final MarketLoader loader = new YahooFinanceLoader();
		final MarketMLDataSet market = new MarketMLDataSet(loader,
				Config.INPUT_WINDOW, Config.PREDICT_WINDOW);
		final MarketDataDescription desc = new MarketDataDescription(
				Config.TICKER, MarketDataType.ADJUSTED_CLOSE, true, true);
		market.addDescription(desc);

		Calendar end = new GregorianCalendar();// end today
		Calendar begin = (Calendar) end.clone();// begin 30 days ago
		
		// Gather training data for the last 2 years, stopping 60 days short of today.
		// The 60 days will be used to evaluate prediction.
		begin.add(Calendar.DATE, -60);
		end.add(Calendar.DATE, -60);
		begin.add(Calendar.YEAR, -2);
		
		market.load(begin.getTime(), end.getTime());
		market.generate();
		EncogUtility.saveEGB(new File(dataDir,Config.TRAINING_FILE), market);

		// create a network
		final BasicNetwork network = EncogUtility.simpleFeedForward(
				market.getInputSize(), 
				Config.HIDDEN1_COUNT, 
				Config.HIDDEN2_COUNT, 
				market.getIdealSize(), 
				true);	

		// save the network and the training
		EncogDirectoryPersistence.saveObject(new File(dataDir,Config.NETWORK_FILE), network);
	}
}
