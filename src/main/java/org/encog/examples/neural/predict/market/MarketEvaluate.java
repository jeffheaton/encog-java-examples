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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.market.MarketDataDescription;
import org.encog.ml.data.market.MarketDataType;
import org.encog.ml.data.market.MarketMLDataSet;
import org.encog.ml.data.market.loader.MarketLoader;
import org.encog.ml.data.market.loader.YahooFinanceLoader;
import org.encog.neural.data.NeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

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

	public static MarketMLDataSet grabData() {
		MarketLoader loader = new YahooFinanceLoader();
		MarketMLDataSet result = new MarketMLDataSet(loader,
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

	public static void evaluate(File dataDir) {

		File file = new File(dataDir,Config.NETWORK_FILE);

		if (!file.exists()) {
			System.out.println("Can't read file: " + file.getAbsolutePath());
			return;
		}

		BasicNetwork network = (BasicNetwork)EncogDirectoryPersistence.loadObject(file);	

		MarketMLDataSet data = grabData();

		DecimalFormat format = new DecimalFormat("#0.0000");

		int count = 0;
		int correct = 0;
		for (MLDataPair pair : data) {
			MLData input = pair.getInput();
			MLData actualData = pair.getIdeal();
			MLData predictData = network.compute(input);

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
