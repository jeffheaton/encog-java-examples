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
package org.encog.examples.neural.freeform;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.freeform.FreeformNetwork;
import org.encog.neural.freeform.training.FreeformBackPropagation;
import org.encog.neural.freeform.training.FreeformResilientPropagation;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.Format;

public class FreeformCompare {
	
	public static final boolean useRPROP = false;
	public static final boolean dualHidden = true;
	public static final int ITERATIONS = 2;
	
	
	public static BasicNetwork basicNetwork;
	public static FreeformNetwork freeformNetwork;
	
	/**
	 * The input necessary for XOR.
	 */
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for XOR.
	 */
	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };
	
	public static void main(String[] args) {
				
		// create the basic network
		basicNetwork = new BasicNetwork();
		basicNetwork.addLayer(new BasicLayer(null,true,2));
		basicNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),true,2));
		if( dualHidden ) {
			basicNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),true,3));
		}
		basicNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
		basicNetwork.getStructure().finalizeStructure();
		basicNetwork.reset();
		basicNetwork.reset(1000);
		
		// create the freeform network
		freeformNetwork = new FreeformNetwork(basicNetwork);

		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		
		// create two trainers
		
		MLTrain freeformTrain;
		
		if( useRPROP ) {
			freeformTrain = new FreeformResilientPropagation(freeformNetwork,trainingSet);
		} else {
			freeformTrain = new FreeformBackPropagation(freeformNetwork,trainingSet, 0.7, 0.3);
		}
		
		MLTrain basicTrain;
		
		if( useRPROP ) {
			basicTrain = new ResilientPropagation(basicNetwork,trainingSet);
		} else {
			basicTrain = new Backpropagation(basicNetwork,trainingSet, 0.7, 0.3);
		}
		
		// perform both
		for(int i=1;i<=ITERATIONS;i++) {
			freeformTrain.iteration();
			basicTrain.iteration();
			System.out.println("Iteration #" + i + " : "
					+ "Freeform: " + Format.formatPercent(freeformTrain.getError())
					+ ", Basic: " + Format.formatPercent(basicTrain.getError()));
		}
		
		

		
	}
}
