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
package org.encog.examples.neural.benchmark;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.benchmark.RandomTrainingFactory;

public class ThreadCount {
	
	public static final int INPUT_COUNT = 40;
	public static final int HIDDEN_COUNT = 60;
	public static final int OUTPUT_COUNT = 20;
	
	public static void perform(int thread)
	{
		long start = System.currentTimeMillis();
		final BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(MultiBench.INPUT_COUNT));
		network.addLayer(new BasicLayer(MultiBench.HIDDEN_COUNT));
		network.addLayer(new BasicLayer(MultiBench.OUTPUT_COUNT));
		network.getStructure().finalizeStructure();
		network.reset();
		
		final MLDataSet training = RandomTrainingFactory.generate(1000,50000,
				INPUT_COUNT, OUTPUT_COUNT, -1, 1);
		
		ResilientPropagation rprop = new ResilientPropagation(network,training);
		rprop.setThreadCount(thread);
		for(int i=0;i<5;i++)
		{
			rprop.iteration();
		}
		long stop = System.currentTimeMillis();
		System.out.println("Result with " + thread + " was " + (stop-start));
	}
	

	
	public static void main(String[] args)
	{
		for(int i=1;i<16;i++)
		{
			perform(i);
		}
		
		Encog.getInstance().shutdown();
	}
}
