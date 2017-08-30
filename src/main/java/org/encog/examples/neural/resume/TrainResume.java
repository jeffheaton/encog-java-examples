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
package org.encog.examples.neural.resume;

import java.io.File;
import java.util.Arrays;

import org.encog.Encog;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.TrainingContinuation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.obj.SerializeObject;
import org.encog.util.simple.EncogUtility;

/**
 * This example shows how to begin training a neural network, stop, and then resume.
 *
 */
public class TrainResume {
	
	public static String FILENAME = "resume.ser";
	
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
		{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	
	public static void main(String[] args)
	{
		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 3, 0, 1, false);
		
		// randomize consistent so that we get weights we know will converge
		(new ConsistentRandomizer(-1,1,100)).randomize(network);
		
		ResilientPropagation train = new ResilientPropagation(network, trainingSet);
		
		System.out.println("Perform initial train.");
		EncogUtility.trainToError(train,0.01);
		TrainingContinuation cont = train.pause();
		System.out.println(Arrays.toString((double[])cont.getContents().get(ResilientPropagation.LAST_GRADIENTS)));
		System.out.println(Arrays.toString((double[])cont.getContents().get(ResilientPropagation.UPDATE_VALUES)));
		
		
		
		try
		{
			SerializeObject.save(new File(FILENAME), cont);
			cont = (TrainingContinuation)SerializeObject.load(new File(FILENAME));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		System.out.println("Now trying a second train, with continue from the first.  Should stop after one iteration");
		ResilientPropagation train2 = new ResilientPropagation(network, trainingSet);
		train2.resume(cont);
		EncogUtility.trainToError(train2,0.01);	
		Encog.getInstance().shutdown();
	}
}
