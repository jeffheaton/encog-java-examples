/*
 * Encog(tm) Java Examples v3.3
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2014 Heaton Research, Inc.
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

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.util.Format;
import org.encog.util.file.FileUtil;

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
	
	public ProBenData(File file) {
		this.sourceFile = file;
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
			this.testDataSet.add(inputData, idealData);
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

}
