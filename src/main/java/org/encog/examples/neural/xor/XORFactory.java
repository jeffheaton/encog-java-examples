package org.encog.examples.neural.xor;

import org.encog.ml.MLMethod;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
import org.encog.util.simple.EncogUtility;

/**
 * This example shows how to use the Encog machine learning factory to 
 * generate a number of machine learning methods and training techniques 
 * to learn the XOR operator.
 */
public class XORFactory {

	/**
	 * The input necessary for XOR.
	 */
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for XOR.
	 */
	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };
	
	public static final String METHOD_FEEDFORWARD_A = "?:B->SIGMOID->4:B->SIGMOID->?";
	
	/**
	 * Common means of creating a machine learning method.
	 * @param methodType The type of method to create.
	 * @param architecture The architecture string to use.
	 * @return The newly created machine learning method.
	 */
	public MLMethod createMethod(String methodType, String architecture) {
		MLMethodFactory factory = new MLMethodFactory();
		System.out.println("Creating machine learning method: " + methodType);
		System.out.println("with architecture: " + architecture);
		return factory.create(methodType, architecture, 2, 1);
	}
	
	/**
	 * Create a trainer to use with the machine learning method.
	 * @param method The method that is being used.
	 * @param dataSet The data set that is being used for training.
	 * @param trainType The training type.
	 * @param args Any training arguments to be used.
	 * @return The newly created trainer.
	 */
	public MLTrain createTrainer(MLMethod method,MLDataSet dataSet,String trainType,String args) {
		MLTrainFactory factory = new MLTrainFactory();
		MLTrain train = factory.create(method, dataSet, trainType, args);
		// reset if improve is less than 1% over 5 cycles
		train.addStrategy(new RequiredImprovementStrategy(5));	
		System.out.println("Creating trainer method: " + trainType);
		System.out.println("with args: " + args);
		return train;
	}
	
	/**
	 * Demonstrate a feedforward network with RPROP.
	 */
	public void xorRPROP() {
		MLMethod method = createMethod(MLMethodFactory.TYPE_FEEDFORWARD,METHOD_FEEDFORWARD_A);
		MLDataSet dataSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		MLTrain train = createTrainer(method,dataSet,MLTrainFactory.TYPE_RPROP,"");
		EncogUtility.trainToError(train, 0.01);
		EncogUtility.evaluate((MLRegression)method, dataSet);
	}
	
	/**
	 * Demonstrate a feedforward network with backpropagation.
	 */
	public void xorBackProp() {
		MLMethod method = createMethod(MLMethodFactory.TYPE_FEEDFORWARD,METHOD_FEEDFORWARD_A);
		MLDataSet dataSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		MLTrain train = createTrainer(method,dataSet,MLTrainFactory.TYPE_BACKPROP,"");
		EncogUtility.trainToError(train, 0.01);
		EncogUtility.evaluate((MLRegression)method, dataSet);
	}
	
	/**
	 * Display usage information.
	 */
	public void usage() {
		System.out.println("Usage:\nXORFactory [mode]\n\nWhere mode is one of:\n");
		System.out.println("backprop - Feedforward with backpropagation");
		System.out.println("rprop - Feedforward with resilient propagation");
	}
	
	/**
	 * Run the program in the specific mode.
	 * @param mode
	 */
	public void run(String mode) {
		if( mode.equalsIgnoreCase("backprop") ) {
			xorBackProp();
		} if( mode.equalsIgnoreCase("rprop") ) {
			xorRPROP();
		} else {
			usage();
		}
	}

	public static void main(final String args[]) {

		XORFactory program = new XORFactory();
		if( args.length>0 ) {
			program.run(args[0]);
		} else {
			program.usage();
		}
	}
}
