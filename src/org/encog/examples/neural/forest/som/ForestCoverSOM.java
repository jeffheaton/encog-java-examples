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
