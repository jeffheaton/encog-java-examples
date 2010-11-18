/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
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
package org.encog.examples.neural.cloud;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.encog.cloud.EncogCloud;
import org.encog.neural.data.market.MarketDataDescription;
import org.encog.neural.data.market.MarketDataType;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.data.market.TickerSymbol;
import org.encog.neural.data.market.loader.MarketLoader;
import org.encog.neural.data.market.loader.YahooFinanceLoader;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.simple.EncogUtility;

/**
 * This example uses the Encog Cloud Server(http://cloud.encog.com) to track the
 * status of a training process. Some financial data is loaded and used to train
 * the network. The results from the train will be reported to the Encog cloud
 * and you can login to the Heaton Research website and track the progress of
 * the train.
 * 
 * To use this example you must have an Encog Cloud login. This can be obtained
 * from the following URL.
 * 
 * http://www.heatonresearch.com/user/register
 * 
 * Once you have a login, use the ID and password in this program. You can then
 * login and click the "Encog Cloud" tab, under your user profile.
 * 
 */
public class CloudStatusReport {
	public static final String ENCOG_CLOUD_USER = "[Enter your user id]";
	public static final String ENCOG_CLOUD_PASSWORD = "[Enter your password]";
	public static final int INPUT_WINDOW_SIZE = 7;
	public static final int OUTPUT_WINDOW_SIZE = 1;
	public static final TickerSymbol TICKER = new TickerSymbol("AAPL");
	public static final Calendar TRAIN_BEGIN = new GregorianCalendar(2000, 0, 1);
	public static final Calendar TRAIN_END = new GregorianCalendar(2008, 12, 31);

	public static void main(String args[]) {
		// connect to the cloud
		EncogCloud cloud = new EncogCloud();
		cloud.connect(ENCOG_CLOUD_USER, ENCOG_CLOUD_PASSWORD);

		// obtain some data to train with
		final MarketLoader loader = new YahooFinanceLoader();
		final MarketNeuralDataSet market = new MarketNeuralDataSet(loader,
				INPUT_WINDOW_SIZE, OUTPUT_WINDOW_SIZE);
		final MarketDataDescription desc = new MarketDataDescription(TICKER,
				MarketDataType.ADJUSTED_CLOSE, true, true);
		market.addDescription(desc);

		market.load(TRAIN_BEGIN.getTime(), TRAIN_END.getTime());
		market.generate();

		// create a neural network
		BasicNetwork network = EncogUtility.simpleFeedForward(
				INPUT_WINDOW_SIZE, 40, 0, OUTPUT_WINDOW_SIZE, true);

		// train the neural network
		ResilientPropagation rprop = new ResilientPropagation(network, market);
		rprop.setCloud(cloud);

		EncogUtility.trainToError(rprop, network, market, 0.01);

	}
}
