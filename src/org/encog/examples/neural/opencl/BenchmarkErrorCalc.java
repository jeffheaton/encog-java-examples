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
package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.concurrency.calc.ConcurrentCalculate;
import org.encog.engine.data.EngineDataSet;
import org.encog.engine.network.flat.FlatNetwork;
import org.encog.engine.util.Stopwatch;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.simple.EncogUtility;

public class BenchmarkErrorCalc {
	
	public static final int TRAINING_SIZE = 100000;
	public static final int INPUT_SIZE = 300;
	public static final int IDEAL_SIZE = 1;
	public static final int HIDDEN1_SIZE = 300;
	public static final int HIDDEN2_SIZE = 300;
	
	
	public static void main(String[] args)
	{
		ConcurrentCalculate calc = ConcurrentCalculate.getInstance();
		
		
		EngineDataSet training = RandomTrainingFactory.generate(1000,
				TRAINING_SIZE, INPUT_SIZE, IDEAL_SIZE, -1, 1);
		BasicNetwork network = EncogUtility.simpleFeedForward(training
				.getInputSize(), HIDDEN1_SIZE, HIDDEN2_SIZE, training.getIdealSize(), true);
		network.reset();
		FlatNetwork flat = network.getStructure().getFlat();
		
		calc.setTrainingData(training);
		calc.setNetwork(flat);
		
		Stopwatch sw1 = new Stopwatch();
		sw1.start();
		double e1 = calc.calculateError();
		sw1.stop();
		
		Stopwatch sw2 = new Stopwatch();
		Encog.getInstance().initCL();
		calc.initCL();
		sw2.start();
		double e2 = calc.calculateError();
		sw2.stop();
		
		System.out.println("CPU-Only Error:" + e1 + ",time=" + sw1.getElapsedMilliseconds());
		System.out.println("CPU&GPU Error:" + e2  + ",time=" + sw2.getElapsedMilliseconds());
	}
}
