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
package org.encog.examples.guide.classification;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.bot.BotUtil;
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
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.simple.EncogUtility;

public class IrisClassification {
	public static String DATA_URL = "https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data";

	private String tempPath;

	public File downloadData(String[] args) throws MalformedURLException {
		if (args.length != 0) {
			tempPath = args[0];
		} else {
			tempPath = System.getProperty("java.io.tmpdir");
		}

		File irisFile = new File(tempPath, "iris.csv");
		BotUtil.downloadPage(new URL(IrisClassification.DATA_URL), irisFile);
		System.out.println("Downloading Iris dataset to: " + irisFile);
		return irisFile;
	}

	public void run(String[] args) {
		try {
			// Download the data that we will attempt to model.
			File irisFile = downloadData(args);
			
			// Define the format of the data file.
			// This area will change, depending on the columns and 
			// format of the file that you are trying to model.
			VersatileDataSource source = new CSVDataSource(irisFile, false,
					CSVFormat.DECIMAL_POINT);
			VersatileMLDataSet data = new VersatileMLDataSet(source);
			data.defineSourceColumn("sepal-length", 0, ColumnType.continuous);
			data.defineSourceColumn("sepal-width", 1, ColumnType.continuous);
			data.defineSourceColumn("petal-length", 2, ColumnType.continuous);
			data.defineSourceColumn("petal-width", 3, ColumnType.continuous);
			
			// Define the column that we are trying to predict.
			ColumnDefinition outputColumn = data.defineSourceColumn("species", 4,
					ColumnType.nominal);
			
			// Analyze the data, determine the min/max/mean/sd of every column.
			data.analyze();
			
			// Map the prediction column to the output of the model, and all
			// other columns to the input.
			data.defineSingleOutputOthersInput(outputColumn);
			
			// Create feedforward neural network as the model type. MLMethodFactory.TYPE_FEEDFORWARD.
			// You could also other model types, such as:
			// MLMethodFactory.SVM:  Support Vector Machine (SVM)
			// MLMethodFactory.TYPE_RBFNETWORK: RBF Neural Network
			// MLMethodFactor.TYPE_NEAT: NEAT Neural Network
			// MLMethodFactor.TYPE_PNN: Probabilistic Neural Network
			EncogModel model = new EncogModel(data);
			model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);
			
			// Send any output to the console.
			model.setReport(new ConsoleStatusReportable());
			
			// Now normalize the data.  Encog will automatically determine the correct normalization
			// type based on the model you chose in the last step.
			data.normalize();
			
			// Hold back some data for a final validation.
			// Shuffle the data into a random ordering.
			// Use a seed of 1001 so that we always use the same holdback and will get more consistent results.
			model.holdBackValidation(0.3, true, 1001);
			
			// Choose whatever is the default training type for this model.
			model.selectTrainingType(data);
			
			// Use a 5-fold cross-validated train.  Return the best method found.
			MLRegression bestMethod = (MLRegression)model.crossvalidate(5, true);

			// Display the training and validation errors.
			System.out.println( "Training error: " + EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));
			System.out.println( "Validation error: " + EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()));
			
			// Display our normalization parameters.
			NormalizationHelper helper = data.getNormHelper();
			System.out.println(helper.toString());
			
			// Display the final model.
			System.out.println("Final model: " + bestMethod);
			
			// Loop over the entire, original, dataset and feed it through the model.
			// This also shows how you would process new data, that was not part of your
			// training set.  You do not need to retrain, simply use the NormalizationHelper
			// class.  After you train, you can save the NormalizationHelper to later
			// normalize and denormalize your data.
			ReadCSV csv = new ReadCSV(irisFile, false, CSVFormat.DECIMAL_POINT);
			String[] line = new String[4];
			MLData input = helper.allocateInputVector();
			
			while(csv.next()) {
				StringBuilder result = new StringBuilder();
				line[0] = csv.get(0);
				line[1] = csv.get(1);
				line[2] = csv.get(2);
				line[3] = csv.get(3);
				String correct = csv.get(4);
				helper.normalizeInputVector(line,input.getData(),false);
				MLData output = bestMethod.compute(input);
				String irisChosen = helper.denormalizeOutputVectorToString(output)[0];
				
				result.append(Arrays.toString(line));
				result.append(" -> predicted: ");
				result.append(irisChosen);
				result.append("(correct: ");
				result.append(correct);
				result.append(")");
				
				System.out.println(result.toString());
			}
			
			// Delete data file ande shut down.
			irisFile.delete();
			Encog.getInstance().shutdown();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		IrisClassification prg = new IrisClassification();
		prg.run(args);
	}
}
