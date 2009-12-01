package org.encog.examples.neural.forest.som;

import java.io.File;

public class Constant {
	/**
	 * The base directory that all of the data for this example is stored in.
	 */
	public static final File BASE_DIRECTORY = new File("c:\\data");
	
	/**
	 * The source data file from which all others are built.  This file can
	 * be downloaded from:
	 * 
	 * http://kdd.ics.uci.edu/databases/covertype/covertype.html
	 */
	public static final File COVER_TYPE_FILE = new File(BASE_DIRECTORY,"covtype.data");
	
	/**
	 * 75% of the data will be moved into this file to be used as training data.  The 
	 * data is still in "raw form" in this file.
	 */
	public static final File TRAINING_FILE = new File(BASE_DIRECTORY,"trainingSOM.csv");
	
	/**
	 * 25% of the data will be moved into this file to be used as evaluation data.  The 
	 * data is still in "raw form" in this file.
	 */
	public static final File EVALUATE_FILE = new File(BASE_DIRECTORY,"evaluateSOM.csv");
	
	/**
	 * The training file is normalized and placed into this file.
	 */
	public static final File NORMALIZED_FILE = new File(BASE_DIRECTORY, "normalizedSOM.csv");
	
	/**
	 * CSV files are slow to parse with because the text must be converted into numbers.
	 * The balanced file will be converted to a binary file to be used for training.
	 */
	public static final File BINARY_FILE = new File(BASE_DIRECTORY, "normalizedSOM.bin");
	
	/**
	 * The trained network and normalizer will be saved into an Encog EG file.
	 */
	public static final File TRAINED_NETWORK_FILE = new File(BASE_DIRECTORY,"forestSOM.eg");
	
	/**
	 * The name of the network inside of the EG file.
	 */
	public static final String TRAINED_NETWORK_NAME = "forest-network";
	
	/**
	 * The name of the normalization object inside of the EG file.
	 */
	public static final String NORMALIZATION_NAME = "forest-norm";
	
	/**
	 * How many minutes to train for (console mode only)
	 */
	public static final int TRAINING_MINUTES = 10;
	
	/**
	 * How many hidden neurons to use.
	 */
	public static final int HIDDEN_COUNT = 100;
}
