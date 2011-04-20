package org.encog.examples.neural.xor;

import org.encog.ml.MLMethod;
import org.encog.ml.MLRegression;
import org.encog.ml.MLResettable;
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
	public static final String METHOD_BIASLESS_A = "?->SIGMOID->4->SIGMOID->?";
	public static final String METHOD_SVMC_A = "?->C->?";
	public static final String METHOD_SVMR_A = "?->R->?";
	
	/**
	 * Demonstrate a feedforward network with RPROP.
	 */
	public void xorRPROP() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_RPROP,
				"");		
	}
	
	/**
	 * Demonstrate a feedforward biasless network with RPROP.
	 */
	public void xorBiasless() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_BIASLESS_A,
				MLTrainFactory.TYPE_RPROP,
				"");		
	}
	
	/**
	 * Demonstrate a feedforward network with backpropagation.
	 */
	public void xorBackProp() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_BACKPROP,
				"");		
	}
	
	/**
	 * Demonstrate a SVM-classify.
	 */
	public void xorSVMClassify() {
		process( 
				MLMethodFactory.TYPE_SVM,
				XORFactory.METHOD_SVMC_A,
				MLTrainFactory.TYPE_SVM,
				"");		
	}
	
	/**
	 * Demonstrate a SVM-regression.
	 */
	public void xorSVMRegression() {
		process( 
				MLMethodFactory.TYPE_SVM,
				XORFactory.METHOD_SVMR_A,
				MLTrainFactory.TYPE_SVM,
				"");		
	}
	
	/**
	 * Demonstrate a XOR annealing.
	 */
	public void xorAnneal() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_ANNEAL,
				"");		
	}
	
	/**
	 * Demonstrate a XOR genetic.
	 */
	public void xorGenetic() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_GENETIC,
				"");		
	}
	
	public void process(String methodName, String methodArchitecture,String trainerName, String trainerArgs) {
		
		// first, create the machine learning method
		MLMethodFactory methodFactory = new MLMethodFactory();		
		MLMethod method = methodFactory.create(methodName, methodArchitecture, 2, 1);
		
		// second, create the data set		
		MLDataSet dataSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		
		// third, create the trainer
		MLTrainFactory trainFactory = new MLTrainFactory();	
		MLTrain train = trainFactory.create(method,dataSet,trainerName,trainerArgs);				
		// reset if improve is less than 1% over 5 cycles
		if( method instanceof MLResettable ) {
			train.addStrategy(new RequiredImprovementStrategy(5));
		}

		// fourth, train and evaluate.
		EncogUtility.trainToError(train, 0.01);
		EncogUtility.evaluate((MLRegression)method, dataSet);
		
		// finally, write out what we did
		System.out.println("Machine Learning Type: " + methodName);
		System.out.println("Machine Learning Architecture: " + methodArchitecture);

		System.out.println("Training Method: " + trainerName);
		System.out.println("Training Args: " + trainerArgs);
		
	}
	
	/**
	 * Display usage information.
	 */
	public void usage() {
		System.out.println("Usage:\nXORFactory [mode]\n\nWhere mode is one of:\n");
		System.out.println("backprop - Feedforward biased with backpropagation");
		System.out.println("biasless - Feedforward biasless with resilient");
		System.out.println("rprop - Feedforward biased with resilient propagation");
	}
	
	/**
	 * Run the program in the specific mode.
	 * @param mode
	 */
	public void run(String mode) {
		if( mode.equalsIgnoreCase("backprop") ) {
			xorBackProp();
		} else if( mode.equalsIgnoreCase("rprop") ) {
			xorRPROP();
		} else if( mode.equalsIgnoreCase("biasless") ) {
			xorBiasless();
		} else if( mode.equalsIgnoreCase("svm-c") ) {
			xorSVMClassify();
		} else if( mode.equalsIgnoreCase("svm-r") ) {
			xorSVMRegression();
		} else if( mode.equalsIgnoreCase("anneal") ) {
			xorAnneal();
		} else if( mode.equalsIgnoreCase("genetic") ) {
			xorGenetic();
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
