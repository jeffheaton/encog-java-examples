/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
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
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
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
	public static final String METHOD_RBF_A = "?->gaussian(c=4)->?";
	public static final String METHOD_PNNC_A = "?->C(kernel=gaussian)->?";
	public static final String METHOD_PNNR_A = "?->R(kernel=gaussian)->?";
	
	/**
	 * Demonstrate a feedforward network with RPROP.
	 */
	public void xorRPROP() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_RPROP,
				"",1);		
	}
	
	/**
	 * Demonstrate a feedforward biasless network with RPROP.
	 */
	public void xorBiasless() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_BIASLESS_A,
				MLTrainFactory.TYPE_RPROP,
				"",1);		
	}
	
	/**
	 * Demonstrate a feedforward network with backpropagation.
	 */
	public void xorBackProp() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_BACKPROP,
				"",1);		
	}
	

	/**
	 * Demonstrate a feedforward network with backpropagation.
	 */
	public void xorQProp() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_QPROP,
				"",1);		
	}
	
	/**
	 * Demonstrate a SVM-classify.
	 */
	public void xorSVMClassify() {
		process( 
				MLMethodFactory.TYPE_SVM,
				XORFactory.METHOD_SVMC_A,
				MLTrainFactory.TYPE_SVM,
				"",1);		
	}
	
	/**
	 * Demonstrate a SVM-regression.
	 */
	public void xorSVMRegression() {
		process( 
				MLMethodFactory.TYPE_SVM,
				XORFactory.METHOD_SVMR_A,
				MLTrainFactory.TYPE_SVM,
				"",1);		
	}
	
	/**
	 * Demonstrate a SVM-regression search.
	 */
	public void xorSVMSearchRegression() {
		process( 
				MLMethodFactory.TYPE_SVM,
				XORFactory.METHOD_SVMR_A,
				MLTrainFactory.TYPE_SVM_SEARCH,
				"",1);		
	}
	
	/**
	 * Demonstrate a XOR annealing.
	 */
	public void xorAnneal() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_ANNEAL,
				"",1);		
	}
	
	/**
	 * Demonstrate a XOR genetic.
	 */
	public void xorGenetic() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_GENETIC,
				"",1);		
	}
	
	/**
	 * Demonstrate a XOR LMA.
	 */
	public void xorLMA() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_LMA,
				"",1);		
	}
	
	/**
	 * Demonstrate a XOR LMA.
	 */
	public void xorManhattan() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_MANHATTAN,
				"lr=0.0001",1);		
	}
	
	/**
	 * Demonstrate a XOR SCG.
	 */
	public void xorSCG() {
		process( 
				MLMethodFactory.TYPE_FEEDFORWARD,
				XORFactory.METHOD_FEEDFORWARD_A,
				MLTrainFactory.TYPE_SCG,
				"",1);		
	}
	
	/**
	 * Demonstrate a XOR RBF.
	 */
	public void xorRBF() {
		process( 
				MLMethodFactory.TYPE_RBFNETWORK,
				XORFactory.METHOD_RBF_A,
				MLTrainFactory.TYPE_RPROP,
				"",1);		
	}
	
	/**
	 * Demonstrate a XOR RBF.
	 */
	public void xorSVD() {
		process( 
				MLMethodFactory.TYPE_RBFNETWORK,
				XORFactory.METHOD_RBF_A,
				MLTrainFactory.TYPE_SVD,
				"",1);		
	}
	
	/**
	 * Demonstrate a XOR RBF.
	 */
	public void xorPNNC() {
		process( 
				MLMethodFactory.TYPE_PNN,
				XORFactory.METHOD_PNNC_A,
				MLTrainFactory.TYPE_PNN,
				"",2);		
	}
	
	/**
	 * Demonstrate a XOR RBF.
	 */
	public void xorPNNR() {
		process( 
				MLMethodFactory.TYPE_PNN,
				XORFactory.METHOD_PNNR_A,
				MLTrainFactory.TYPE_PNN,
				"",1);		
	}
	
	public void process(String methodName, String methodArchitecture,String trainerName, String trainerArgs,int outputNeurons) {
		
		// first, create the machine learning method
		MLMethodFactory methodFactory = new MLMethodFactory();		
		MLMethod method = methodFactory.create(methodName, methodArchitecture, 2, outputNeurons);
		
		// second, create the data set		
		MLDataSet dataSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		
		// third, create the trainer
		MLTrainFactory trainFactory = new MLTrainFactory();	
		MLTrain train = trainFactory.create(method,dataSet,trainerName,trainerArgs);				
		// reset if improve is less than 1% over 5 cycles
		if( method instanceof MLResettable && !(train instanceof ManhattanPropagation) ) {
			train.addStrategy(new RequiredImprovementStrategy(500));
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
		}  else if( mode.equalsIgnoreCase("svm-search-r") ) {
			xorSVMSearchRegression();
		} else if( mode.equalsIgnoreCase("anneal") ) {
			xorAnneal();
		} else if( mode.equalsIgnoreCase("genetic") ) {
			xorGenetic();
		} else if( mode.equalsIgnoreCase("lma") ) {
			xorLMA();
		} else if( mode.equalsIgnoreCase("manhattan") ) {
			xorManhattan();
		} else if( mode.equalsIgnoreCase("scg") ) {
			xorSCG();
		} else if( mode.equalsIgnoreCase("rbf") ) {
			xorRBF();
		} else if( mode.equalsIgnoreCase("svd") ) {
			xorSVD();
		} else if( mode.equalsIgnoreCase("pnn-c") ) {
			xorPNNC();
		} else if( mode.equalsIgnoreCase("pnn-r") ) {
			xorPNNR();
		} else if( mode.equalsIgnoreCase("qprop") ) {
			xorQProp();
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
