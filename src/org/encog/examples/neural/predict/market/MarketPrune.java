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

import org.encog.ConsoleStatusReportable;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.prune.PruneIncremental;
import org.encog.persist.EncogPersistedCollection;

public class MarketPrune {

	public static void incremental() {
		File file = new File(Config.FILENAME);

		if (!file.exists()) {
			System.out.println("Can't read file: " + file.getAbsolutePath());
			return;
		}

		EncogPersistedCollection encog = new EncogPersistedCollection(file);
		// BasicNetwork network = (BasicNetwork)
		// encog.find(Config.MARKET_NETWORK);
		NeuralDataSet training = (NeuralDataSet) encog
				.find(Config.MARKET_TRAIN);
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(training.getInputSize());
		pattern.setOutputNeurons(training.getIdealSize());
		pattern.setActivationFunction(new ActivationTANH());

		PruneIncremental prune = new PruneIncremental(training, pattern, 100, 1, 10,
				new ConsoleStatusReportable());

		prune.addHiddenLayer(5, 50);
		prune.addHiddenLayer(0, 50);

		prune.process();

		encog.add(Config.MARKET_NETWORK, prune.getBestNetwork());

	}
}
