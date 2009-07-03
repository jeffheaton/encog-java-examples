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

import org.encog.neural.data.market.MarketDataDescription;
import org.encog.neural.data.market.MarketDataType;
import org.encog.neural.data.market.MarketNeuralDataSet;
import org.encog.neural.data.market.loader.MarketLoader;
import org.encog.neural.data.market.loader.YahooFinanceLoader;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

/**
 * Build the training data for the prediction and store it in an Encog file for
 * later training.
 * 
 * @author jeff
 * 
 */
public class MarketBuildTraining {

	public static void main(final String args[]) {
		Logging.stopConsoleLogging();
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
