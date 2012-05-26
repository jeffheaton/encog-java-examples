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

/**
 * This class is used to generate training for the moving averages example.
 *
 */
public class GenerateTraining {

	/**
	 * The path that the data files will be stored at.
	 */
	private final File path;	
	
	/**
	 * The path to the training file.
	 */
	private final File trainingFile;
	
	/**
	 * A moving window used to track future gains.
	 */
	private final WindowDouble window = new WindowDouble(Config.PREDICT_WINDOW);

	/**
	 * Used to normalize the difference between the two fields.
	 */
	private final NormalizedField fieldDifference;
	
	/**
	 * Used to normalize the outcome (gain or loss).
	 */
	private final NormalizedField fieldOutcome;
	
	/** 
	 * The maximum difference.
	 */
	private double maxDifference;
	
	/**
	 * The minimum difference.
	 */
	private double minDifference;
	
	/**
	 * The max pip gain/loss.
	 */
	private int maxPIPs;
	
	/**
	 * The min pip gain/loss.
	 */
	private int minPIPs;
	
	/**
	 * Construct the training generator.
	 * @param thePath The path to use.
	 */
	public GenerateTraining(File thePath) {
		this.path = thePath;
		this.trainingFile = new File(this.path,Config.FILENAME_TRAIN);		
		
		this.fieldDifference = new NormalizedField(NormalizationAction.Normalize,"diff",Config.DIFF_RANGE,-Config.DIFF_RANGE,1,-1);
		this.fieldOutcome = new NormalizedField(NormalizationAction.Normalize,"out",Config.PIP_RANGE,-Config.PIP_RANGE,1,-1);
	}
	
	/**
	 * Process the individual training file.
	 * @param file The training file to process.
	 * @param output The data set to output to.
	 */
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
				double diff = this.fieldDifference.normalize( (fast - slow)/Config.PIP_SIZE);		
				a[i+1] = diff;
			}
			window.add(a);
			
			
			if( window.isFull() ) {
				double max = (this.window.calculateMax(0,Config.INPUT_WINDOW)-close)/Config.PIP_SIZE;
				double min = (this.window.calculateMin(0,Config.INPUT_WINDOW)-close)/Config.PIP_SIZE;
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
				
				o = this.fieldOutcome.normalize(o);
				idealData.setData(0, o);
				
				output.add(inputData, idealData);
			}			
		}
	}
	
	/**
	 * Used to calibrate the training file. 
	 * @param file The file to consider.
	 */
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
					double diff = (fast - slow)/Config.PIP_SIZE;
					this.minDifference = Math.min(this.minDifference, diff);
					this.maxDifference = Math.max(this.maxDifference, diff);	
				}
			}
			window.add(a);
			
			if( window.isFull() ) {
				double max = (this.window.calculateMax(0,Config.INPUT_WINDOW)-close)/Config.PIP_SIZE;
				double min = (this.window.calculateMin(0,Config.INPUT_WINDOW)-close)/Config.PIP_SIZE;
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

	/**
	 * Called to generate the training file.
	 */
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

	/**
	 * Called to calibrate the data.  Does not actually do anything, other
	 * than display a range report.
	 */
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
