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
package org.encog.examples.guide.timeseries;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.bot.BotUtil;
import org.encog.mathutil.error.ErrorCalculation;
import org.encog.mathutil.error.ErrorCalculationMode;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.arrayutil.VectorWindow;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;

public class SunSpotTimeseries {
	public static String DATA_URL = "http://solarscience.msfc.nasa.gov/greenwch/spot_num.txt";

	private String tempPath;
	public static final int WINDOW_SIZE = 3;

	public File downloadData(String[] args) throws MalformedURLException {
		if (args.length != 0) {
			tempPath = args[0];
		} else {
			tempPath = System.getProperty("java.io.tmpdir");
		}

		File filename = new File(tempPath, "auto-mpg.data");
		BotUtil.downloadPage(new URL(SunSpotTimeseries.DATA_URL), filename);
		System.out.println("Downloading sunspot dataset to: " + filename);
		return filename;
	}

	public void run(String[] args) {
		try {
			ErrorCalculation.setMode(ErrorCalculationMode.RMS);
			// Download the data that we will attempt to model.
			File filename = downloadData(args);

			// Define the format of the data file.
			// This area will change, depending on the columns and
			// format of the file that you are trying to model.
			CSVFormat format = new CSVFormat('.', ' '); // decimal point and
														// space separated
			VersatileDataSource source = new CSVDataSource(filename, true,
					format);

			VersatileMLDataSet data = new VersatileMLDataSet(source);
			data.getNormHelper().setFormat(format);

			ColumnDefinition columnSSN = data.defineSourceColumn("SSN",
					ColumnType.continuous);
			ColumnDefinition columnDEV = data.defineSourceColumn("DEV",
					ColumnType.continuous);

			// Analyze the data, determine the min/max/mean/sd of every column.
			data.analyze();

			// Use SSN & DEV to predict SSN. For time-series it is okay to have
			// SSN both as
			// an input and an output.
			data.defineInput(columnSSN);
			data.defineInput(columnDEV);
			data.defineOutput(columnSSN);

			// Create feedforward neural network as the model type.
			// MLMethodFactory.TYPE_FEEDFORWARD.
			// You could also other model types, such as:
			// MLMethodFactory.SVM: Support Vector Machine (SVM)
			// MLMethodFactory.TYPE_RBFNETWORK: RBF Neural Network
			// MLMethodFactor.TYPE_NEAT: NEAT Neural Network
			// MLMethodFactor.TYPE_PNN: Probabilistic Neural Network
			EncogModel model = new EncogModel(data);
			model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);

			// Send any output to the console.
			model.setReport(new ConsoleStatusReportable());

			// Now normalize the data. Encog will automatically determine the
			// correct normalization
			// type based on the model you chose in the last step.
			data.normalize();

			// Set time series.
			data.setLeadWindowSize(1);
			data.setLagWindowSize(WINDOW_SIZE);

			// Hold back some data for a final validation.
			// Do not shuffle the data into a random ordering. (never shuffle
			// time series)
			// Use a seed of 1001 so that we always use the same holdback and
			// will get more consistent results.
			model.holdBackValidation(0.3, false, 1001);

			// Choose whatever is the default training type for this model.
			model.selectTrainingType(data);

			// Use a 5-fold cross-validated train. Return the best method found.
			// (never shuffle time series)
			MLRegression bestMethod = (MLRegression) model.crossvalidate(5,
					false);

			// Display the training and validation errors.
			System.out.println("Training error: "
					+ model.calculateError(bestMethod,
							model.getTrainingDataset()));
			System.out.println("Validation error: "
					+ model.calculateError(bestMethod,
							model.getValidationDataset()));

			// Display our normalization parameters.
			NormalizationHelper helper = data.getNormHelper();
			System.out.println(helper.toString());

			// Display the final model.
			System.out.println("Final model: " + bestMethod);

			// Loop over the entire, original, dataset and feed it through the
			// model. This also shows how you would process new data, that was
			// not part of your training set. You do not need to retrain, simply
			// use the NormalizationHelper class. After you train, you can save
			// the NormalizationHelper to later normalize and denormalize your
			// data.
			ReadCSV csv = new ReadCSV(filename, true, format);
			String[] line = new String[2];

			// Create a vector to hold each time-slice, as we build them.
			// These will be grouped together into windows.
			double[] slice = new double[2];
			VectorWindow window = new VectorWindow(WINDOW_SIZE + 1);
			MLData input = helper.allocateInputVector(WINDOW_SIZE + 1);

			// Only display the first 100
			int stopAfter = 100;

			while (csv.next() && stopAfter > 0) {
				StringBuilder result = new StringBuilder();

				line[0] = csv.get(2);// ssn
				line[1] = csv.get(3);// dev
				helper.normalizeInputVector(line, slice, false);

				// enough data to build a full window?
				if (window.isReady()) {
					window.copyWindow(input.getData(), 0);
					String correct = csv.get(2); // trying to predict SSN.
					MLData output = bestMethod.compute(input);
					String predicted = helper
							.denormalizeOutputVectorToString(output)[0];

					result.append(Arrays.toString(line));
					result.append(" -> predicted: ");
					result.append(predicted);
					result.append("(correct: ");
					result.append(correct);
					result.append(")");

					System.out.println(result.toString());
				}

				// Add the normalized slice to the window. We do this just after
				// the after checking to see if the window is ready so that the
				// window is always one behind the current row. This is because
				// we are trying to predict next row.
				window.add(slice);

				stopAfter--;
			}

			// Delete data file and shut down.
			filename.delete();
			Encog.getInstance().shutdown();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SunSpotTimeseries prg = new SunSpotTimeseries();
		prg.run(args);
	}
}
