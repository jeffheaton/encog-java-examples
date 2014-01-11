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
