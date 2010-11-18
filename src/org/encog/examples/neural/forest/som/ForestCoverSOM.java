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
package org.encog.examples.neural.forest.som;

import org.encog.normalize.DataNormalization;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class ForestCoverSOM {
	public static void generate() {
		GenerateData generate = new GenerateData();
		generate.step1();
		DataNormalization norm = generate.step2();

		EncogPersistedCollection encog = new EncogPersistedCollection(
				Constant.TRAINED_NETWORK_FILE);
		encog.add(Constant.NORMALIZATION_NAME, norm);
	}

	public static void train(boolean useGUI) {
		TrainNetwork program = new TrainNetwork();
		program.train(useGUI);
	}

	public static void evaluate() {
		Evaluate evaluate = new Evaluate();
		evaluate.evaluate();
	}

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out
					.println("Usage: ForestCover [generate [e/o]/train/traingui/evaluate] ");
		} else {
			Logging.stopConsoleLogging();
			if (args[0].equalsIgnoreCase("generate")) {
				generate();
			} else if (args[0].equalsIgnoreCase("train"))
				train(false);
			else if (args[0].equalsIgnoreCase("traingui"))
				train(true);
			else if (args[0].equalsIgnoreCase("evaluate"))
				evaluate();
		}
	}
}
