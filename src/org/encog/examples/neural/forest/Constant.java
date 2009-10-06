package org.encog.examples.neural.forest;

import java.io.File;

public class Constant {
	public static final File COVER_TYPE_FILE = new File("/Users/jeff/data/covtype.data");
	public static final File TRAINING_FILE = new File("/Users/jeff/data/output.csv");
	public static final File BUFFER_FILE = new File("/Users/jeff/data/buffered.ser");
	public static final File TRAINED_NETWORK_FILE = new File("/Users/jeff/data/forst.eg");
	
	public static final String TRAINED_NETWORK_NAME = "forest-network";
	
	public static final int INPUT_NEURON_COUNT = 10;
	public static final int OUTPUT_NEURON_COUNT = 7;
	
	public static final int TRAINING_MINUTES = 3;
}
