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

import java.io.File;
import java.text.DecimalFormat;
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

public class MarketEvaluate {

	enum Direction {
		up, down
	};

	public static Direction determineDirection(double d) {
		if (d < 0)
			return Direction.down;
		else
			return Direction.up;
	}

	public static MarketNeuralDataSet grabData() {
		MarketLoader loader = new YahooFinanceLoader();
		MarketNeuralDataSet result = new MarketNeuralDataSet(loader,
				Config.INPUT_WINDOW, Config.PREDICT_WINDOW);
		MarketDataDescription desc = new MarketDataDescription(Config.TICKER,
				MarketDataType.ADJUSTED_CLOSE, true, true);
		result.addDescription(desc);

		Calendar end = new GregorianCalendar();// end today
		Calendar begin = (Calendar) end.clone();// begin 30 days ago
		begin.add(Calendar.DATE, -60);

		result.load(begin.getTime(), end.getTime());
		result.generate();

		return result;

	}

	public static void evaluate() {

		File file = new File(Config.FILENAME);

		if (!file.exists()) {
			System.out.println("Can't read file: " + file.getAbsolutePath());
			return;
		}

		EncogPersistedCollection encog = new EncogPersistedCollection(file);
		BasicNetwork network = (BasicNetwork) encog.find(Config.MARKET_NETWORK);

		if (network == null) {
			System.out.println("Can't find network resource: "
					+ Config.MARKET_NETWORK);
			return;
		}

		MarketNeuralDataSet data = grabData();

		DecimalFormat format = new DecimalFormat("#0.00");

		int count = 0;
		int correct = 0;
		for (NeuralDataPair pair : data) {
			NeuralData input = pair.getInput();
			NeuralData actualData = pair.getIdeal();
			NeuralData predictData = network.compute(input);

			double actual = actualData.getData(0);
			double predict = predictData.getData(0);
			double diff = Math.abs(predict - actual);

			Direction actualDirection = determineDirection(actual);
			Direction predictDirection = determineDirection(predict);

			if (actualDirection == predictDirection)
				correct++;

			count++;

			System.out.println("Day " + count + ":actual="
					+ format.format(actual) + "(" + actualDirection + ")"
					+ ",predict=" + format.format(predict) + "("
					+ predictDirection + ")" + ",diff=" + diff);

		}
		double percent = (double) correct / (double) count;
		System.out.println("Direction correct:" + correct + "/" + count);
		System.out.println("Directional Accuracy:"
				+ format.format(percent * 100) + "%");

	}
}
