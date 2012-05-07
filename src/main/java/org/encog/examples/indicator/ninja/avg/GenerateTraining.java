package org.encog.examples.indicator.ninja.avg;

import java.io.File;

import org.encog.util.arrayutil.WindowDouble;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;

public class GenerateTraining {

	private File path;	
	private WindowDouble window = new WindowDouble(10);
	
	public GenerateTraining(File thePath) {
		this.path = thePath;
	}
	
	public void processFile(File file) {
		ReadCSV csv = new ReadCSV(file.toString(),true,CSVFormat.ENGLISH);
		while(csv.next()) {
			double a[] = new double[2];
			double close = csv.getDouble(1);
			double fast = csv.getDouble(2);
			double slow = csv.getDouble(3);
			double diff = fast - slow;
			a[0] = close;
			a[1] = diff;
			window.add(a);
		}
	}
	
	public void generate() {
		File[] list = this.path.listFiles();

		for (File file : list) {
			String fn = file.getName();
			if (fn.startsWith("collected") && fn.endsWith(".csv")) {
				processFile(file);
			}
		}
	}
}
