package org.encog.examples.indicator.ninja.avg;

import java.io.File;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import org.encog.util.arrayutil.WindowDouble;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;

public class GenerateTraining {

	private final File path;	
	private final File trainingFile;
	private final WindowDouble window = new WindowDouble(10);
	private final double pipSize;
	private final NormalizedField fieldDifference;
	private final NormalizedField fieldOutcome;
	
	public GenerateTraining(File thePath,double thePipSize) {
		this.path = thePath;
		this.pipSize = thePipSize;
		this.trainingFile = new File(this.path,"training.egb");		
		
		this.fieldDifference = new NormalizedField(NormalizationAction.Normalize,"diff",50,-50,1,-1);
		this.fieldOutcome = new NormalizedField(NormalizationAction.Normalize,"out",100,-100,1,-1);
	}
	
	public GenerateTraining(File thePath) {
		this(thePath,0.0001);
	}
		
	public void processFile(File file, BufferedMLDataSet output) {
		
		MLData inputData = new BasicMLData(output.getInputSize());
		MLData idealData = new BasicMLData(output.getIdealSize());
				
		ReadCSV csv = new ReadCSV(file.toString(),true,CSVFormat.ENGLISH);
		while(csv.next()) {
			double a[] = new double[4];
			double close = csv.getDouble(1);
			
			a[0] = close;
			for(int i=0;i<3;i++) {
				double fast = csv.getDouble(2+i);
				double slow = csv.getDouble(5+i);
				double diff = this.fieldDifference.normalize( (fast - slow)/pipSize);		
				a[i+1] = diff;
			}
			window.add(a);
			
			if( window.isFull() ) {
				double max = (this.window.calculateMax(0,3)-close)/pipSize;
				double min = (this.window.calculateMin(0,3)-close)/pipSize;
				double o;
				
				if( Math.abs(max)>Math.abs(min) ) {
					o = max;
				} else {
					o = min;
				}
				
				a = window.getLast();
				for(int i=0;i<3;i++) {							
					inputData.setData(i, a[i+1]);
				}
				
				idealData.setData(0, this.fieldOutcome.normalize(o));
				
				output.add(inputData, idealData);
			}			
		}
	}
	
	public void generate() {
		File[] list = this.path.listFiles();
		
		this.trainingFile.delete();
		BufferedMLDataSet output = new BufferedMLDataSet(this.trainingFile);
		output.beginLoad(3, 1);

		for (File file : list) {
			String fn = file.getName();
			if (fn.startsWith("collected") && fn.endsWith(".csv")) {
				processFile(file, output);
			}
		}
		
		output.endLoad();
		output.close();
	}
}
