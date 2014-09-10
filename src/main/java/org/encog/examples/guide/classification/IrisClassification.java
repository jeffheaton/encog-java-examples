package org.encog.examples.guide.classification;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.encog.Encog;
import org.encog.bot.BotUtil;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.BasicNormalizationStrategy;
import org.encog.ml.data.versatile.CSVDataSource;
import org.encog.ml.data.versatile.ColumnDefinition;
import org.encog.ml.data.versatile.ColumnType;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileDataSource;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.neural.networks.BasicNetwork;
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
			File irisFile = downloadData(args);
			VersatileDataSource source = new CSVDataSource(irisFile, false,
					CSVFormat.DECIMAL_POINT);
			VersatileMLDataSet data = new VersatileMLDataSet(source,
					new BasicNormalizationStrategy(-1, 1, -1, 1));
			data.defineSourceColumn("sepal-length", ColumnType.continuous);
			data.defineSourceColumn("sepal-width", ColumnType.continuous);
			data.defineSourceColumn("petal-length", ColumnType.continuous);
			data.defineSourceColumn("petal-width", ColumnType.continuous);
			ColumnDefinition outputColumn = data.defineSourceColumn("species",
					ColumnType.nominal);
			data.analyze();
			data.normalizeSingleOutputOthersInput(outputColumn);

			BasicNetwork network = EncogUtility.simpleFeedForward(data.getInputSize(), 5, 0, data.getIdealSize(), true);
			EncogUtility.trainToError(network, data, 0.05);
			
			NormalizationHelper helper = data.getNormHelper();
			System.out.println(helper.toString());
			
			
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
				helper.normalizeInputVector(line,input);
				MLData output = network.compute(input);
				String irisChosen = helper.denormalizeOutputVectorToString(output)[0];
				
				result.append(Arrays.toString(line));
				result.append(" -> predicted: ");
				result.append(irisChosen);
				result.append("(correct: ");
				result.append(correct);
				result.append(")");
				
				System.out.println(result.toString());
			}
			
			
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
