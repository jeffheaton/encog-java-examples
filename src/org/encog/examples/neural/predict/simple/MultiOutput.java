/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
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
package org.encog.examples.neural.predict.simple;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.temporal.TemporalDataDescription;
import org.encog.neural.data.temporal.TemporalNeuralDataSet;
import org.encog.neural.data.temporal.TemporalPoint;
import org.encog.neural.data.temporal.TemporalDataDescription.Type;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.logic.FeedforwardLogic;
import org.encog.util.simple.EncogUtility;

public class MultiOutput {
	public static void main(String[] args)
	{
		final int INPUT_SIZE = 5;
		final int OUTPUT_SIZE = 10;
		
		TemporalNeuralDataSet temporal = new TemporalNeuralDataSet(INPUT_SIZE,OUTPUT_SIZE);
		temporal.addDescription(new TemporalDataDescription(Type.RAW, true, true));
		
		for(int i=0;i<100;i++)
		{
			TemporalPoint tp = temporal.createPoint(i);
			tp.setData(0, i/100.0);
		}
		
		temporal.generate();

		//BasicNetwork network = EncogUtility.simpleFeedForward(INPUT_SIZE, 50, 0, OUTPUT_SIZE, true);
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,INPUT_SIZE));
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,30));
		network.addLayer(new BasicLayer(new ActivationLinear(),true,OUTPUT_SIZE));
		network.setLogic(new FeedforwardLogic());
		network.getStructure().finalizeStructure();
		network.reset();
		
		EncogUtility.trainToError(network, temporal, 0.002);
		double[] array = {0.1,0.2,0.3,0.4,0.5};
		NeuralData input = new BasicNeuralData(array);
		NeuralData output = network.compute(input);
		EncogUtility.evaluate(network, temporal);
		System.out.println(output.toString());
	}
	
}
