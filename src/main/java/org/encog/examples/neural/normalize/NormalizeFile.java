package org.encog.examples.neural.normalize;

import java.io.File;

import org.encog.Encog;
import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.csv.normalize.AnalystNormalizeCSV;
import org.encog.app.analyst.script.normalize.AnalystField;
import org.encog.app.analyst.wizard.AnalystWizard;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.csv.CSVFormat;

/**
 * This is a simple example that will normalize a CSV file.  
 * The fields are detected from CSV headers.
 */
public class NormalizeFile {

	public static void dumpFieldInfo(EncogAnalyst analyst) {
		System.out.println("Fields found in file:");
		for (AnalystField field : analyst.getScript().getNormalize()
				.getNormalizedFields()) {

			StringBuilder line = new StringBuilder();
			line.append(field.getName());
			line.append(",action=");
			line.append(field.getAction());
			line.append(",min=");
			line.append(field.getActualLow());
			line.append(",max=");
			line.append(field.getActualHigh());
			System.out.println(line.toString());
		}
	}

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Note: This example assumes that headers are present in the CSV files.");
			System.out.println("NormalizeFile [input file] [target file]");
		} else {
			File sourceFile = new File(args[0]);
			File targetFile = new File(args[1]);

			EncogAnalyst analyst = new EncogAnalyst();
			AnalystWizard wizard = new AnalystWizard(analyst);
			wizard.wizard(sourceFile, true, AnalystFileFormat.DECPNT_COMMA);

			dumpFieldInfo(analyst);

			final AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
			norm.analyze(sourceFile, true, CSVFormat.ENGLISH, analyst);
			norm.setOutputFormat(CSVFormat.ENGLISH);
			norm.setProduceOutputHeaders(true);
			norm.normalize(targetFile);
			Encog.getInstance().shutdown();
		}
	}
}
