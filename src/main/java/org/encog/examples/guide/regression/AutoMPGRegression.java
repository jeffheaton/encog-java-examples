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
package org.encog.examples.guide.regression;

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
import org.encog.ml.data.versatile.missing.MeanMissingHandler;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;

public class AutoMPGRegression {
	public static String DATA_URL = "https://archive.ics.uci.edu/ml/machine-learning-databases/auto-mpg/auto-mpg.data";

	private String tempPath;

	public File downloadData(String[] args) throws MalformedURLException {
		if (args.length != 0) {
			tempPath = args[0];
		} else {
			tempPath = System.getProperty("java.io.tmpdir");
		}

		File mpgFile = new File(tempPath, "auto-mpg.data");
		BotUtil.downloadPage(new URL(AutoMPGRegression.DATA_URL), mpgFile);
		System.out.println("Downloading auto-mpg dataset to: " + mpgFile);
		return mpgFile;
	}

	public void run(String[] args) {
		try {
			// Download the data that we will attempt to model.
			File filename = downloadData(args);
			
			// Define the format of the data file.
			// This area will change, depending on the columns and 
			// format of the file that you are trying to model.
			CSVFormat format = new CSVFormat('.',' '); // decimal point and space separated
			VersatileDataSource source = new CSVDataSource(filename, false, format);
			
			VersatileMLDataSet data = new VersatileMLDataSet(source);
			data.getNormHelper().setFormat(format);
			
			ColumnDefinition columnMPG = data.defineSourceColumn("mpg", 0, ColumnType.continuous);
			ColumnDefinition columnCylinders = data.defineSourceColumn("cylinders", 1, ColumnType.ordinal);
			// It is very important to predefine ordinals, so that the order is known.
			columnCylinders.defineClass(new String[] {"3","4","5","6","8"});
			data.defineSourceColumn("displacement", 2,ColumnType.continuous);
			ColumnDefinition columnHorsePower = data.defineSourceColumn("horsepower", 3, ColumnType.continuous);
			data.defineSourceColumn("weight", 4, ColumnType.continuous);
			data.defineSourceColumn("acceleration", 5, ColumnType.continuous);
			ColumnDefinition columnModelYear = data.defineSourceColumn("model_year", 6, ColumnType.ordinal);
			columnModelYear.defineClass(new String[] {"70","71","72","73","74","75","76","77","78","79","80","81","82"});
			data.defineSourceColumn("origin", 7, ColumnType.nominal);
			
			// Define how missing values are represented.
			data.getNormHelper().defineUnknownValue("?");
			data.getNormHelper().defineMissingHandler(columnHorsePower, new MeanMissingHandler());
			
			// Analyze the data, determine the min/max/mean/sd of every column.
			data.analyze();
			
			// Map the prediction column to the output of the model, and all
			// other columns to the input.
			data.defineSingleOutputOthersInput(columnMPG);
			
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
			System.out.println( "Training error: " + model.calculateError(bestMethod, model.getTrainingDataset()));
			System.out.println( "Validation error: " + model.calculateError(bestMethod, model.getValidationDataset()));
			
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
			ReadCSV csv = new ReadCSV(filename, false, format);
			String[] line = new String[7];
			MLData input = helper.allocateInputVector();
			
			while(csv.next()) {
				StringBuilder result = new StringBuilder();
				
				line[0] = csv.get(1);
				line[1] = csv.get(2);
				line[2] = csv.get(3);
				line[3] = csv.get(4);
				line[4] = csv.get(5);
				line[5] = csv.get(6);
				line[6] = csv.get(7);
				
				String correct = csv.get(0);
				helper.normalizeInputVector(line,input.getData(),false);
				MLData output = bestMethod.compute(input);
				String predictedMPG = helper.denormalizeOutputVectorToString(output)[0];
				
				result.append(Arrays.toString(line));
				result.append(" -> predicted: ");
				result.append(predictedMPG);
				result.append("(correct: ");
				result.append(correct);
				result.append(")");
				
				System.out.println(result.toString());
			}
			
			// Delete data file and shut down.
			filename.delete();
			Encog.getInstance().shutdown();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AutoMPGRegression prg = new AutoMPGRegression();
		prg.run(args);
	}
}
