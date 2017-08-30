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
package org.encog.examples.neural.forest;

import java.io.File;

public class ForestConfig {
	
	/**
	 * The base directory that all of the data for this example is stored in.
	 */
	private File basePath = new File("d:\\data");
	
	/**
	 * The source data file from which all others are built.  This file can
	 * be downloaded from:
	 * 
	 * http://kdd.ics.uci.edu/databases/covertype/covertype.html
	 */
	private File coverTypeFile;
	
	/**
	 * 75% of the data will be moved into this file to be used as training data.  The 
	 * data is still in "raw form" in this file.
	 */
	private File trainingFile;
	
	/**
	 * 25% of the data will be moved into this file to be used as evaluation data.  The 
	 * data is still in "raw form" in this file.
	 */
	private File evaluateFile;
	
	/**
	 * We will limit the number of samples per "tree type" to 3000, this causes the data
	 * to be more balanced and will not allow one tree type to over-fit the network.
	 * The training file is narrowed and placed into this file in "raw form".
	 */
	private File balanceFile;
	
	/**
	 * The training file is normalized and placed into this file.
	 */
	private File normalizedDataFile;
	
	/**
	 * CSV files are slow to parse with because the text must be converted into numbers.
	 * The balanced file will be converted to a binary file to be used for training.
	 */
	private File binaryFile;
	
	/**
	 * The trained network saved into an Encog EG file.
	 */
	private File trainedNetworkFile;
	
	/**
	 * The normalizer will be saved into a ser file.
	 */
	private File normalizeFile;
	
	/**
	 * How many minutes to train for (console mode only)
	 */
	private int trainingMinutes = 10;
	
	/**
	 * How many hidden neurons to use.
	 */
	private int hiddenCount = 100;
	
	public ForestConfig(File theBasePath) {
		
		this.basePath = theBasePath;
		
		this.coverTypeFile = new File(basePath,"covtype.data");
		
		this.trainingFile = new File(basePath,"training.csv");
		
		this.evaluateFile = new File(basePath,"evaluate.csv");
		
		this.balanceFile = new File(basePath,"balance.csv");
		
		this.normalizedDataFile = new File(basePath, "normalized.csv");
		
		this.binaryFile = new File(basePath, "normalized.egb");
		
		this.trainedNetworkFile = new File(basePath,"forest.eg");
		
		this.normalizeFile = new File(basePath,"norm.ser");		
	}

	/**
	 * @return the basePath
	 */
	public File getBasePath() {
		return basePath;
	}

	/**
	 * @param basePath the basePath to set
	 */
	public void setBasePath(File basePath) {
		this.basePath = basePath;
	}

	/**
	 * @return the coverTypeFile
	 */
	public File getCoverTypeFile() {
		return coverTypeFile;
	}

	/**
	 * @param coverTypeFile the coverTypeFile to set
	 */
	public void setCoverTypeFile(File coverTypeFile) {
		this.coverTypeFile = coverTypeFile;
	}

	/**
	 * @return the trainingFile
	 */
	public File getTrainingFile() {
		return trainingFile;
	}

	/**
	 * @param trainingFile the trainingFile to set
	 */
	public void setTrainingFile(File trainingFile) {
		this.trainingFile = trainingFile;
	}

	/**
	 * @return the evaluateFile
	 */
	public File getEvaluateFile() {
		return evaluateFile;
	}

	/**
	 * @param evaluateFile the evaluateFile to set
	 */
	public void setEvaluateFile(File evaluateFile) {
		this.evaluateFile = evaluateFile;
	}

	/**
	 * @return the balanceFile
	 */
	public File getBalanceFile() {
		return balanceFile;
	}

	/**
	 * @param balanceFile the balanceFile to set
	 */
	public void setBalanceFile(File balanceFile) {
		this.balanceFile = balanceFile;
	}

	/**
	 * @return the normalizedDataFile
	 */
	public File getNormalizedDataFile() {
		return normalizedDataFile;
	}

	/**
	 * @param normalizedDataFile the normalizedDataFile to set
	 */
	public void setNormalizedDataFile(File normalizedDataFile) {
		this.normalizedDataFile = normalizedDataFile;
	}

	/**
	 * @return the binaryFile
	 */
	public File getBinaryFile() {
		return binaryFile;
	}

	/**
	 * @param binaryFile the binaryFile to set
	 */
	public void setBinaryFile(File binaryFile) {
		this.binaryFile = binaryFile;
	}

	/**
	 * @return the trainedNetworkFile
	 */
	public File getTrainedNetworkFile() {
		return trainedNetworkFile;
	}

	/**
	 * @param trainedNetworkFile the trainedNetworkFile to set
	 */
	public void setTrainedNetworkFile(File trainedNetworkFile) {
		this.trainedNetworkFile = trainedNetworkFile;
	}

	/**
	 * @return the normalizeFile
	 */
	public File getNormalizeFile() {
		return normalizeFile;
	}

	/**
	 * @param normalizeFile the normalizeFile to set
	 */
	public void setNormalizeFile(File normalizeFile) {
		this.normalizeFile = normalizeFile;
	}

	/**
	 * @return the trainingMinutes
	 */
	public int getTrainingMinutes() {
		return trainingMinutes;
	}

	/**
	 * @param trainingMinutes the trainingMinutes to set
	 */
	public void setTrainingMinutes(int trainingMinutes) {
		this.trainingMinutes = trainingMinutes;
	}

	/**
	 * @return the hiddenCount
	 */
	public int getHiddenCount() {
		return hiddenCount;
	}

	/**
	 * @param hiddenCount the hiddenCount to set
	 */
	public void setHiddenCount(int hiddenCount) {
		this.hiddenCount = hiddenCount;
	}
	
	
	
}
