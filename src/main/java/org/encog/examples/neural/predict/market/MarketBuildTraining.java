/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
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
