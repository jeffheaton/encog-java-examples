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
	private final WindowDouble window = new WindowDouble(Config.PREDICT_WINDOW);
	private final double pipSize;
	private final NormalizedField fieldDifference;
	private final NormalizedField fieldOutcome;
	private double maxDifference;
	private double minDifference;
	private int maxPIPs;
	private int minPIPs;
	
	public GenerateTraining(File thePath,double thePipSize) {
		this.path = thePath;
		this.pipSize = thePipSize;
		this.trainingFile = new File(this.path,Config.FILENAME_TRAIN);		
		
		this.fieldDifference = new NormalizedField(NormalizationAction.Normalize,"diff",Config.DIFF_RANGE,-Config.DIFF_RANGE,1,-1);
		this.fieldOutcome = new NormalizedField(NormalizationAction.Normalize,"out",Config.PIP_RANGE,-Config.PIP_RANGE,1,-1);
	}
	
	public GenerateTraining(File thePath) {
		this(thePath,Config.PIP_SIZE);
	}
		
	protected void processFile(File file, BufferedMLDataSet output) {
		
		MLData inputData = new BasicMLData(output.getInputSize());
		MLData idealData = new BasicMLData(output.getIdealSize());
				
		ReadCSV csv = new ReadCSV(file.toString(),true,CSVFormat.ENGLISH);
		while(csv.next()) {
			double a[] = new double[Config.INPUT_WINDOW+1];
			double close = csv.getDouble(1);
			
			int fastIndex = 2;
			int slowIndex = fastIndex + Config.INPUT_WINDOW;
			
			a[0] = close;
			for(int i=0;i<3;i++) {
				double fast = csv.getDouble(fastIndex+i);
				double slow = csv.getDouble(slowIndex+i);
				double diff = this.fieldDifference.normalize( (fast - slow)/pipSize);		
				a[i+1] = diff;
			}
			window.add(a);
			
			
			if( window.isFull() ) {
				double max = (this.window.calculateMax(0,Config.INPUT_WINDOW)-close)/pipSize;
				double min = (this.window.calculateMin(0,Config.INPUT_WINDOW)-close)/pipSize;
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
	
	protected void calibrateFile(File file) {
						
		ReadCSV csv = new ReadCSV(file.toString(),true,CSVFormat.ENGLISH);
		while(csv.next()) {
			double a[] = new double[1];
			double close = csv.getDouble(1);
			
			int fastIndex = 2;
			int slowIndex = fastIndex + Config.INPUT_WINDOW;
			a[0] = close;
			for(int i=0;i<Config.INPUT_WINDOW;i++) {
				double fast = csv.getDouble(fastIndex+i);
				double slow = csv.getDouble(slowIndex+i);
				
				if( !Double.isNaN(fast) && !Double.isNaN(slow) ) {
					double diff = (fast - slow)/pipSize;
					this.minDifference = Math.min(this.minDifference, diff);
					this.maxDifference = Math.max(this.maxDifference, diff);	
				}
			}
			window.add(a);
			
			if( window.isFull() ) {
				double max = (this.window.calculateMax(0,Config.INPUT_WINDOW)-close)/pipSize;
				double min = (this.window.calculateMin(0,Config.INPUT_WINDOW)-close)/pipSize;
				double o;
				
				if( Math.abs(max)>Math.abs(min) ) {
					o = max;
				} else {
					o = min;
				}
				
				this.maxPIPs = Math.max(this.maxPIPs, (int)o);
				this.minPIPs = Math.min(this.minPIPs, (int)o);
			}			
		}
	}

	public void generate() {
		File[] list = this.path.listFiles();
		
		this.trainingFile.delete();
		BufferedMLDataSet output = new BufferedMLDataSet(this.trainingFile);
		output.beginLoad(Config.INPUT_WINDOW, 1);

		for (File file : list) {
			String fn = file.getName();
			if (fn.startsWith("collected") && fn.endsWith(".csv")) {
				processFile(file, output);
			}
		}
		
		output.endLoad();
		output.close();
	}

	public void calibrate() {
		File[] list = this.path.listFiles();
		
		this.maxDifference = Double.NEGATIVE_INFINITY;
		this.minDifference = Double.POSITIVE_INFINITY;
		this.maxPIPs = Integer.MIN_VALUE;
		this.minPIPs = Integer.MAX_VALUE;
		
		for (File file : list) {
			String fn = file.getName();
			if (fn.startsWith("collected") && fn.endsWith(".csv")) {
				calibrateFile(file);
			}
		}		
		
		System.out.println("Max difference: " + this.maxDifference);
		System.out.println("Min difference: " + this.minDifference);
		System.out.println("Max PIPs: " + this.maxPIPs);
		System.out.println("Min PIPs: " + this.minPIPs);
		System.out.println("\nSuggested calibration: ");
		System.out.println("DIFF_RANGE = " + (int)(Math.max(this.maxDifference,Math.abs(this.minDifference)) * 1.2) );
		System.out.println("PIP_RANGE = " + (int)(Math.max(this.maxPIPs,Math.abs(this.minPIPs)) * 1.2) );

	}
}
