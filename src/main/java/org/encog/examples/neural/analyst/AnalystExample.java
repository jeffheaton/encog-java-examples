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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.encog.Encog;
import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.ConsoleAnalystListener;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.report.AnalystReport;
import org.encog.app.analyst.wizard.AnalystWizard;

/**
 * The Encog Analyst is typically used through the Encog Workbench.  However, 
 * this does not need to be the case.  This example shows how to use the 
 * Encog Analyst from code.  This example will download the needed data from 
 * UCI.  This example can use either the classic Iris dataset of the forest 
 * cover data set.  You must provide a working directory for it to use. 
 * 
 * To use this example with Iris data use:
 * 
 * AnalystExample iris d:\data\iris
 * 
 * To use this example with Forest Cover use:
 * 
 * AnalystExample forest d:\data\iris
 *
 */
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
		
		wiz.wizard(url, analystFile, rawFile, false, AnalystFileFormat.DECPNT_COMMA);

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
		wiz.setTaskBalance(true);
		
		wiz.wizard(url, analystFile, rawFile, false, AnalystFileFormat.DECPNT_COMMA);
		encog.setMaxIteration(600);
		encog.executeTask("task-full");
		
		encog.save(analystFile);
		
		AnalystReport report = new AnalystReport(encog);
		report.produceReport(new File(dir,"report.html"));
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out
					.println("Usage: AnalystExample [iris/forest] [data directory]");
			System.out
					.println("Data directory can be any empty directory.  Raw files will be downloaded to here.");
			System.exit(1);
		}
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
		} finally {
			Encog.getInstance().shutdown();
		}
	}
}
