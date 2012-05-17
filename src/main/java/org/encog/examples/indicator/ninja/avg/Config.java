package org.encog.examples.indicator.ninja.avg;

import java.io.File;

import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;

public class Config {
	public static final int DIFF_RANGE = 50;
	public static final int PIP_RANGE = 35;
	public static final double PIP_SIZE = 0.0001;
	public static final int INPUT_WINDOW = 3;
	public static final int PREDICT_WINDOW = 10;
	public static final float targetError = 0.03f;
	public static final String METHOD_TYPE = MLMethodFactory.TYPE_FEEDFORWARD;
	public static final String METHOD_ARCHITECTURE = "?:B->TANH->20:B->TANH->?";
	public static final String TRAIN_TYPE = MLTrainFactory.TYPE_RPROP;
	public static final String TRAIN_PARAMS = "";
	public static final String FILENAME_TRAIN = "training.egb";
	public static final String METHOD_NAME = "method.eg";
	
	
}
