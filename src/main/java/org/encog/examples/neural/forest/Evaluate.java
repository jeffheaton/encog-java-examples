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
package org.encog.examples.neural.forest;

import java.io.File;
import java.io.IOException;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.neural.data.NeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.Format;
import org.encog.util.csv.ReadCSV;
import org.encog.util.normalize.DataNormalization;
import org.encog.util.normalize.output.nominal.OutputEquilateral;
import org.encog.util.obj.SerializeObject;

public class Evaluate {

	private int[] treeCount = new int[10];
	private int[] treeCorrect = new int[10];
	private ForestConfig config;

	public Evaluate(ForestConfig config) {
		this.config = config;
	}

	public void keepScore(int actual, int ideal) {
		treeCount[ideal]++;
		if (actual == ideal)
			treeCorrect[ideal]++;
	}

	public BasicNetwork loadNetwork() {
		File file = config.getTrainedNetworkFile();

		if (!file.exists()) {
			System.out.println("Can't read file: " + file.getAbsolutePath());
			return null;
		}

		BasicNetwork network = (BasicNetwork)EncogDirectoryPersistence.loadObject(file);
		
		return network;
	}

	public DataNormalization loadNormalization() throws IOException, ClassNotFoundException {
		
		DataNormalization norm = null;
		
		if( config.getNormalizeFile().exists() ) {
			norm = (DataNormalization) SerializeObject.load(config.getNormalizeFile());
		}

		if (norm == null) {
			System.out.println("Can't find normalization resource: "
					+ config.getNormalizeFile());
			return null;
		}

		return norm;
	}

	public int determineTreeType(OutputEquilateral eqField, MLData output) {
		int result = 0;

		if (eqField != null) {
			result = eqField.getEquilateral().decode(output.getData());
		} else {
			double maxOutput = Double.NEGATIVE_INFINITY;
			result = -1;

			for (int i = 0; i < output.size(); i++) {
				if (output.getData(i) > maxOutput) {
					maxOutput = output.getData(i);
					result = i;
				}
			}
		}

		return result;
	}

	public void evaluate() throws IOException, ClassNotFoundException {
		BasicNetwork network = loadNetwork();
		DataNormalization norm = loadNormalization();

		ReadCSV csv = new ReadCSV(config.getEvaluateFile().toString(), false, ',');
		double[] input = new double[norm.getInputFields().size()];
		OutputEquilateral eqField = (OutputEquilateral) norm.findOutputField(
				OutputEquilateral.class, 0);

		int correct = 0;
		int total = 0;
		while (csv.next()) {
			total++;
			for (int i = 0; i < input.length; i++) {
				input[i] = csv.getDouble(i);
			}
			MLData inputData = norm.buildForNetworkInput(input);
			MLData output = network.compute(inputData);
			int coverTypeActual = determineTreeType(eqField, output);
			int coverTypeIdeal = (int) csv.getDouble(54) - 1;

			keepScore(coverTypeActual, coverTypeIdeal);

			if (coverTypeActual == coverTypeIdeal) {
				correct++;
			}
		}

		System.out.println("Total cases:" + total);
		System.out.println("Correct cases:" + correct);
		double percent = (double) correct / (double) total;
		System.out.println("Correct percent:"
				+ Format.formatPercentWhole(percent));
		for (int i = 0; i < 7; i++) {
			double p = ((double) this.treeCorrect[i] / (double) this.treeCount[i]);
			System.out.println("Tree Type #" + i + " - Correct/total: "
					+ this.treeCorrect[i] + "/" + treeCount[i] + "("
					+ Format.formatPercentWhole(p) + ")");
		}
		
		Encog.getInstance().shutdown();
	}
}
