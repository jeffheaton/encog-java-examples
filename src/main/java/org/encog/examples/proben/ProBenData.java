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
package org.encog.examples.proben;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.util.Format;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;

public class ProBenData {

	private File sourceFile;
	private int boolIn = 0;
	private int realIn = 0;
	private int boolOut = 0;
	private int realOut = 0;
	private int trainingExamples = 0;
	private int validationExamples = 0;
	private int testExamples = 0;
	private MLDataSet trainingDataSet;
	private MLDataSet validationDataSet;
	private MLDataSet testDataSet;
	private boolean mergeTest;
	
	public ProBenData(File file, boolean mergeTest) {
		this.sourceFile = file;
		this.mergeTest = mergeTest;
	}
	
	public static String obtainProbenPath(String[] args) {
		if (args.length > 0) {
			return args[0];
		} else {
			System.out
					.println("To run this program, it is necessary to download the Proben1\n"
							+ "datasets and pass their path as the first agrument to this\n"
							+ "program.  Proben1 can be downloaded\n"
							+ "from: https://github.com/jeffheaton/proben1");
			System.exit(1);
			return null; 
		}
	}
	
	public void processHeaderLine(String line) {
		// find the name/value
		int index = line.indexOf('=');
		String name = line.substring(0,index).trim().toLowerCase();
		String value = line.substring(index+1).trim();
		int valueInt = Integer.parseInt(value);
		
		// fill in the correct value
		if( name.equals("bool_in")) {
			this.boolIn = valueInt;
		} else if( name.equals("real_in")) {
			this.realIn = valueInt;
		}  else if( name.equals("bool_out")) {
			this.boolOut = valueInt;
		} else if( name.equals("real_out")) {
			this.realOut = valueInt;
		} else if( name.equals("training_examples")) {
			this.trainingExamples = valueInt;
		} else if( name.equals("validation_examples")) {
			this.validationExamples = valueInt;
		} else if( name.equals("test_examples")) {
			this.testExamples = valueInt;
		} else {
			throw new ProBenError("Unknown header element: " + name);
		}
		
	}
	
	public int getInputCount() {
		return boolIn+realIn;
	}
	
	public int getIdealCount() {
		return boolOut+realOut;
	}
	
	public void processDataLine(String line) {
		MLData inputData = new BasicMLData(getInputCount());
		MLData idealData = new BasicMLData(getIdealCount());
		
		StringTokenizer tok = new StringTokenizer(line, " ");
		for(int i=0;i<inputData.size();i++) {
			inputData.setData(i, Double.parseDouble(tok.nextToken()));
		}
		
		for(int i=0;i<idealData.size();i++) {
			idealData.setData(i, Double.parseDouble(tok.nextToken()));
		}
		
		if( this.trainingDataSet.getRecordCount()<this.trainingExamples) {
			this.trainingDataSet.add(inputData, idealData);
		} else if( this.validationDataSet.getRecordCount()<this.validationExamples) {
			this.validationDataSet.add(inputData, idealData);
		}  else if( this.testDataSet.getRecordCount()<this.testExamples) {
			if( this.mergeTest ) {
				this.trainingDataSet.add(inputData, idealData);
			} else {
				this.testDataSet.add(inputData, idealData);
			}
		}
	}
	
	public void load() {
		
		this.trainingDataSet = new BasicMLDataSet();
		this.validationDataSet = new BasicMLDataSet(); 
		this.testDataSet = new BasicMLDataSet();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					this.sourceFile));
			String str;
			while ((str = in.readLine()) != null) {
				if (str.indexOf('=') != -1) {
					processHeaderLine(str);
				} else {
					processDataLine(str);
				}
			}
			in.close();
		} catch (IOException ex) {
			throw new ProBenError(ex);
		}
	}
	
	
	
	public int getTrainingExamples() {
		return trainingExamples;
	}

	public MLDataSet getTrainingDataSet() {
		return trainingDataSet;
	}

	public MLDataSet getValidationDataSet() {
		return validationDataSet;
	}
	
	public MLDataSet getTestDataSet() {
		return testDataSet;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append( "bool_in = " + boolIn + "\n");
		result.append( "real_in = " + realIn + "\n");
		result.append( "bool_out = " + boolOut + "\n");
		result.append( "real_out = " + realOut + "\n");
		result.append( "training examples: " + Format.formatInteger((int)trainingDataSet.getRecordCount())+"\n");
		result.append( "validation examples: " + Format.formatInteger((int)validationDataSet.getRecordCount())+"\n");
		result.append( "test examples: " + Format.formatInteger((int)testDataSet.getRecordCount())+"\n");
		return result.toString();
	}

	public String getName() {
		return this.sourceFile.getName();
	}
	
	private void center(MLDataSet dataset, double inputCenter, double outputCenter) {
		if( dataset.size()==0 ) {
			return;
		}
		
		// Calculate MEAN
		double[] mean = new double[this.getInputCount()+this.getIdealCount()];
		int count = 0;
		for(MLDataPair pair: dataset) {
			count++;
			int meanIndex = 0;
			for(int i=0;i<getInputCount();i++) {
				mean[meanIndex++]+=pair.getInput().getData(i);
			}
			for(int i=0;i<getIdealCount();i++) {
				mean[meanIndex++]+=pair.getIdeal().getData(i);
			}
		}
		
		for(int i=0;i<mean.length;i++) {
			mean[i]/=count;
		}
		
		// Calculate the variance (on the way to standard deviation)
		double[] sdev = new double[this.getInputCount()+this.getIdealCount()];
		
		for(MLDataPair pair: dataset) {
			int varIndex = 0;
			for(int i=0;i<getInputCount();i++) {
				sdev[varIndex]+=Math.pow(mean[varIndex]-pair.getInput().getData(i),2);
				varIndex++;
			}
			for(int i=0;i<getIdealCount();i++) {
				sdev[varIndex]+=Math.pow(mean[varIndex]-pair.getIdeal().getData(i),2);
				varIndex++;
			}
		}
		
		// Take square root of variance, and get standard deviation
		for(int i=0;i<sdev.length;i++) {
			sdev[i]=Math.sqrt(sdev[i]);
		}
		
		// Now use the zscore, centered at requested value
		for(MLDataPair pair: dataset) {
			int index = 0;
			for(int i=0;i<getInputCount();i++) {
				double zscore = 0;
				
				// If zscore is undefined (zero variance) then just use zero so that this
				// value is at the center.
				if(sdev[i]>Encog.DEFAULT_DOUBLE_EQUAL) {
					zscore = (pair.getInput().getData(i) - mean[index])/sdev[i];	
				}
				
				pair.getInput().setData(i, inputCenter + zscore);
				index++;
			}
			for(int i=0;i<getIdealCount();i++) {
				double zscore = 0;
				
				// If zscore is undefined (zero variance) then just use zero so that this
				// value is at the center.
				if(sdev[i]>Encog.DEFAULT_DOUBLE_EQUAL) {
					zscore = (pair.getIdeal().getData(i) - mean[index])/sdev[i];	
				}

				pair.getIdeal().setData(i, outputCenter + zscore);
				index++;
			}
		}
	}
	
	public void center(double inputCenter, double outputCenter) {
		center(this.getTrainingDataSet(),inputCenter,outputCenter);
		center(this.getValidationDataSet(),inputCenter,outputCenter);
		center(this.getTestDataSet(),inputCenter,outputCenter);
	}

}
