package org.encog.examples.neural.analyst;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.encog.ConsoleStatusReportable;
import org.encog.app.analyst.ConsoleAnalystListener;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.report.AnalystReport;
import org.encog.app.analyst.wizard.AnalystWizard;
import org.encog.util.csv.CSVFormat;
import org.encog.util.logging.Logging;


public class AnalystExample {

	public static final String IRIS_SOURCE = "http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data";
	public static final String FOREST_SOURCE = "http://archive.ics.uci.edu/ml/machine-learning-databases/covtype/covtype.data.gz";

	public void irisExample(File dir) throws MalformedURLException {
		System.out.println("Starting Iris dataset example.");
		URL url = new URL(IRIS_SOURCE);
		File analystFile = new File(dir,"iris.ega");
		File rawFile = new File(dir,"iris_raw.csv");
		
		EncogAnalyst encog = new EncogAnalyst();
		encog.addAnalystListener(new ConsoleAnalystListener());
		AnalystWizard wiz = new AnalystWizard(encog);
		
		wiz.wizard(url, analystFile, rawFile, false, CSVFormat.ENGLISH);

		encog.executeTask("task-full");
		
		encog.save(analystFile);
		
		AnalystReport report = new AnalystReport(encog);
		report.produceReport(new File(dir,"report.html"));
	}

	public void forestExample(File dir) throws MalformedURLException {
		System.out.println("Starting forest cover dataset example.");
		URL url = new URL(FOREST_SOURCE);
		File analystFile = new File(dir,"forest.ega");
		File rawFile = new File(dir,"forest_raw.csv");
		
		EncogAnalyst encog = new EncogAnalyst();
		encog.addAnalystListener(new ConsoleAnalystListener());
		AnalystWizard wiz = new AnalystWizard(encog);
		
		wiz.wizard(url, analystFile, rawFile, false, CSVFormat.ENGLISH);
		
		encog.executeTask("task-full");
		
		encog.save(analystFile);
		
		//AnalystReport report = new AnalystReport(encog);
		//report.produceReport(new File(dir,"report.html"));
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out
					.println("Usage: AnalystExample [iris/forest] [data directory]");
			System.out
					.println("Data directory can be any empty directory.  Raw files will be downloaded to here.");
			System.exit(1);
		}
		
		Logging.stopConsoleLogging();

		String command = args[0].trim().toLowerCase();
		File dir = new File(args[1].trim());

		AnalystExample example = new AnalystExample();

		try {
			if (command.equals("forest")) {
				example.forestExample(dir);
			} else if (command.equals("iris")) {
				example.irisExample(dir);
			} else {
				System.out.println("Unknown command: " + command);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
