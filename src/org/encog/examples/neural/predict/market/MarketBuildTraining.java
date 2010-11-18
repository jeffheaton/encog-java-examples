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
package org.encog.examples.neural.predict.market;

import org.encog.neural.data.market.MarketDataDescription;
import org.encog.neural.data.market.MarketDataType;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.data.market.loader.MarketLoader;
import org.encog.neural.data.market.loader.YahooFinanceLoader;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogPersistedCollection;

/**
 * Build the training data for the prediction and store it in an Encog file for
 * later training.
 * 
 * @author jeff
 * 
 */
public class MarketBuildTraining {

	public static void generate() {
		
		final MarketLoader loader = new YahooFinanceLoader();
		final MarketNeuralDataSet market = new MarketNeuralDataSet(loader,
				Config.INPUT_WINDOW, Config.PREDICT_WINDOW);
		final MarketDataDescription desc = new MarketDataDescription(
				Config.TICKER, MarketDataType.ADJUSTED_CLOSE, true, true);
		market.addDescription(desc);

		market.load(Config.TRAIN_BEGIN.getTime(), Config.TRAIN_END.getTime());
		market.generate();
		market.setDescription("Market data for: " + Config.TICKER.getSymbol());

		// create a network
		final BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(market.getInputSize()));
		network.addLayer(new BasicLayer(Config.HIDDEN1_COUNT));
		if (Config.HIDDEN2_COUNT != 0) {
			network.addLayer(new BasicLayer(Config.HIDDEN2_COUNT));
		}
		network.addLayer(new BasicLayer(market.getIdealSize()));
		network.getStructure().finalizeStructure();
		network.reset();

		// save the network and the training
		final EncogPersistedCollection encog = new EncogPersistedCollection(
				Config.FILENAME);
		encog.create();
		encog.add(Config.MARKET_TRAIN, market);
		encog.add(Config.MARKET_NETWORK, network);

	}
}
