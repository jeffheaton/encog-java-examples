package org.encog.examples.neural.xor;

import org.encog.ml.MLMethod;
import org.encog.ml.MLRegression;
import org.encog.ml.MLTrain;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.strategy.RequiredImprovementStrategy;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * This example shows how to use the Encog machine learning factory to 
 * generate a number of machine learning methods and training techniques 
 * to learn the XOR operator.
 */
public class XORFactory {

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };
	
	public static final String METHOD_FEEDFORWARD_A = "?:B->SIGMOID->3:B->SIGMOID->?:B";
	
	public MLMethod createMethod(String methodType, String architecture) {
		MLMethodFactory factory = new MLMethodFactory();
		return factory.create(methodType, architecture, 2, 1);
	}
	
	public MLTrain createTrainer(MLMethod method,MLDataSet dataSet,String trainType,String args) {
		MLTrainFactory factory = new MLTrainFactory();
		MLTrain train = factory.create(method, dataSet, trainType, args);
		// reset if improve is less than 1% over 5 cycles
		//train.addStrategy(new RequiredImprovementStrategy(5));	
		return train;
	}
	
	
	public void xorRPROP() {
		/*MLMethod method = createMethod(MLMethodFactory.TYPE_FEEDFORWARD,METHOD_FEEDFORWARD_A);
		MLDataSet dataSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		MLTrain train = createTrainer(method,dataSet);
		EncogUtility.trainToError(train, 0.01);
		EncogUtility.evaluate((MLRegression)method, dataSet);*/
	}

	public static void main(final String args[]) {

		XORFactory program = new XORFactory();
		program.xorRPROP();
	}
}
