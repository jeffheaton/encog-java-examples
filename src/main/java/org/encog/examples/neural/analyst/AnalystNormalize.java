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
package org.encog.examples.neural.analyst;

import java.util.ArrayList;
import java.util.List;

import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.script.prop.ScriptProperties;
import org.encog.app.analyst.util.FieldDirection;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.util.arrayutil.ClassItem;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;

public class AnalystNormalize {
	public static void main(String[] args) {
		try {
			EncogAnalyst analyst = new EncogAnalyst();
			
			List<ClassItem> speciesClasses = new ArrayList<ClassItem>();
			speciesClasses.add(new ClassItem("Iris-setosa",0));
			speciesClasses.add(new ClassItem("Iris-versicolor",1));
			speciesClasses.add(new ClassItem("Iris-virginica",2));
			
			analyst.getScript().getProperties().setProperty(ScriptProperties.SETUP_CONFIG_INPUT_HEADERS, true);
			analyst.getScript().getProperties().setProperty(ScriptProperties.SETUP_CONFIG_CSV_FORMAT, AnalystFileFormat.DECPNT_COMMA);
			
			analyst.getScript().setDefaultNormalizedRange(0,1);
			analyst.getScript().defineField("sepal_l", FieldDirection.Input, NormalizationAction.Normalize, 0, 100);
			analyst.getScript().defineField("sepal_w", FieldDirection.Input, NormalizationAction.Normalize, 0, 100);
			analyst.getScript().defineField("petal_l", FieldDirection.Input, NormalizationAction.Normalize, 0, 100);
			analyst.getScript().defineField("petal_w", FieldDirection.Input, NormalizationAction.Normalize, 0, 100);
			analyst.getScript().defineClass("species", FieldDirection.Output, NormalizationAction.Equilateral, speciesClasses);
			
			MLDataSet training = analyst.getUtility().loadCSV("/Users/jheaton/iris.csv");
			/*for(MLDataPair pair: training) {
				System.out.println(pair.toString());
			}*/
			System.out.println(training.size());
			
			analyst.save("/Users/jheaton/test.ega");
			
			ReadCSV csv = new ReadCSV("/Users/jheaton/iris.csv",true,CSVFormat.ENGLISH);
			MLData input = new BasicMLData(analyst.determineInputCount());
			double[] rawInput = new double[analyst.determineInputFieldCount()];
			
			while(csv.next()) {
				rawInput[0] = csv.getDouble(0);
				rawInput[1] = csv.getDouble(1);
				rawInput[2] = csv.getDouble(2);
				rawInput[3] = csv.getDouble(3);
				//analyst.getUtility().prepareInput(rawInput, input);
				System.out.println(input.toString());
			}
			

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
