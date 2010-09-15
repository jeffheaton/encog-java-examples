package org.encog.examples.neural.opencl;

import java.util.Arrays;

import org.encog.Encog;
import org.encog.engine.network.flat.FlatNetwork;
import org.encog.engine.network.train.TrainFlatNetwork;
import org.encog.engine.network.train.prop.TrainFlatNetworkOpenCL;
import org.encog.engine.network.train.prop.TrainFlatNetworkResilient;
import org.encog.engine.opencl.EncogCLDevice;
import org.encog.engine.util.EngineArray;
import org.encog.engine.util.ErrorCalculation;
import org.encog.engine.util.ErrorCalculationMode;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.mathutil.randomize.Randomizer;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.structure.NetworkCODEC;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

public class CompareCL {

	public final static int ITERATIONS = 10;
	
	public static double XOR_INPUT[][] = { 
		{ 0.0, 0.0 }, 
		{ 1.0, 0.0 },
		{ 0.0, 1.0 }, 
		{ 1.0, 1.0 } 
		};

	public static double XOR_IDEAL[][] = { 
		{ 0.0 }, 
		{ 1.0 }, 
		{ 1.0 }, 
		{ 0.0 } 
		};

	public static void displayWeights(FlatNetwork networkCPU,
			FlatNetwork networkGPU) {
		System.out.println("CPU-Weights:"
				+ Arrays.toString(networkCPU.getWeights()));
		System.out.println("GPU-Weights:"
				+ Arrays.toString(networkGPU.getWeights()));
	}
	
	public static FlatNetwork createNetwork()
	{
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 4, 0, 1,
				false);
		Randomizer randomizer = new ConsistentRandomizer(-1, 1);
		randomizer.randomize(network);
		return network.getStructure().getFlat().clone();
	}

	public static void main(String[] args) {
		Logging.stopConsoleLogging();
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);

		FlatNetwork networkCPU = createNetwork();
		FlatNetwork networkGPU = createNetwork();
		
		System.out.println("Starting Weights:");
		displayWeights(networkCPU, networkGPU);

		Encog.getInstance().initCL();
		EncogCLDevice device = Encog.getInstance().getCL().getDevices().get(0);
		
		final TrainFlatNetworkResilient trainCPU = new TrainFlatNetworkResilient(networkCPU,
				trainingSet);
		final TrainFlatNetworkOpenCL trainGPU = new TrainFlatNetworkOpenCL(networkGPU,
				trainingSet, device);
		
		trainGPU.learnRPROP();

		for (int iteration = 1; iteration <= ITERATIONS; iteration++) {
			trainCPU.iteration();
			trainGPU.iteration();

			System.out.println();
			System.out.println("*** Iteration #" + iteration);
			System.out.println("CPU-Error: " + trainCPU.getError());
			System.out.println("GPU-Error: " + trainGPU.getError());
			System.out.println("CPU-LastGrad:" + Arrays.toString(trainCPU.getLastGradient()));
			System.out.println("GPU-LastGrad:" + Arrays.toString(trainGPU.getLastGradient()));
			System.out.println("CPU-Updates :" + Arrays.toString(trainCPU.getUpdateValues()));
			System.out.println("GPU-Updates :" + Arrays.toString(trainGPU.getUpdateValues()));
			displayWeights(networkCPU, networkGPU);
		}
	}
}
