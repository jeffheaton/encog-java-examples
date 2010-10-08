package org.encog.examples.neural.resume;

import java.io.IOException;
import java.util.Arrays;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.TrainingContinuation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.strategy.RequiredImprovementStrategy;
import org.encog.util.logging.Logging;
import org.encog.util.obj.SerializeObject;
import org.encog.util.simple.EncogUtility;

public class TrainResume {
	
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
		{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	
	public static void main(String[] args)
	{
		Logging.stopConsoleLogging();
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 4, 0, 1, false);
		ResilientPropagation train = new ResilientPropagation(network, trainingSet);
		train.addStrategy(new RequiredImprovementStrategy(5));
		
		System.out.println("Perform initial train.");
		EncogUtility.trainToError(train,network, trainingSet, 0.01);
		TrainingContinuation cont = train.pause();
		System.out.println(Arrays.toString((double[])cont.getContents().get(ResilientPropagation.LAST_GRADIENTS)));
		System.out.println(Arrays.toString((double[])cont.getContents().get(ResilientPropagation.UPDATE_VALUES)));
		
		try
		{
		SerializeObject.save("resume.ser", cont);
		cont = (TrainingContinuation)SerializeObject.load("resume.ser");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		System.out.println("Now trying a second train, with continue from the first.  Should stop after one iteration");
		ResilientPropagation train2 = new ResilientPropagation(network, trainingSet);
		train2.resume(cont);
		EncogUtility.trainToError(train2,network, trainingSet, 0.01);	
	}
}
