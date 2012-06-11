package org.encog.examples.indicator.avg;

import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;

public class Config {
	
	/**
	 * The maximum range (either positive or negative) that the difference between the fast and slow will be normalized to.
	 */
	public static final int DIFF_RANGE = 50;
	/**
	 * The maximum range (either positive or negative) that the pip profit(or loss) will be in.
	 */
	public static final int PIP_RANGE = 35;
	/**
	 * The size of a single PIP (i.e. 0.0001 for EURUSD)
	 */
	public static final double PIP_SIZE = 0.0001;
	
	/**
	 * The size of the input window.  This is the number of previous bars to consider.
	 */
	public static final int INPUT_WINDOW = 3;
	
	/**
	 * The number of bars to look forward to determine a max profit, or loss.
	 */
	public static final int PREDICT_WINDOW = 10;
	
	/**
	 * The targeted error.  Once the training error reaches this value, training will stop.
	 */
	public static final float TARGET_ERROR = 0.03f;
	
	/**
	 * The type of method.  This is an Encog factory code.
	 */
	public static final String METHOD_TYPE = MLMethodFactory.TYPE_FEEDFORWARD;
	
	/**
	 * The architecture of the method.  This is an Encog factory code.
	 */
	public static final String METHOD_ARCHITECTURE = "?:B->TANH->20:B->TANH->?";
	
	/**
	 * The type of training.  This is an Encog factory code.
	 */
	public static final String TRAIN_TYPE = MLTrainFactory.TYPE_RPROP;
	
	/**
	 * The training parameters.  This is an Encog factory code.
	 */
	public static final String TRAIN_PARAMS = "";
	
	/**
	 * The filename for the training data.
	 */
	public static final String FILENAME_TRAIN = "training.egb";
	
	/**
	 * The filename to store the method to.
	 */
	public static final String METHOD_NAME = "method.eg";
	
	
}
