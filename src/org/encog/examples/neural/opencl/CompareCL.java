package org.encog.examples.neural.opencl;

import java.util.Arrays;

import org.encog.Encog;
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

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void displayWeights(BasicNetwork networkCPU,
			BasicNetwork networkGPU) {
		System.out.println("CPU-Network:"
				+ Arrays.toString(NetworkCODEC.networkToArray(networkCPU)));
		System.out.println("GPU-Network:"
				+ Arrays.toString(NetworkCODEC.networkToArray(networkGPU)));
	}

	public static void main(String[] args) {
		Logging.stopConsoleLogging();
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);

		BasicNetwork networkCPU = EncogUtility.simpleFeedForward(2, 4, 0, 1,
				false);
		BasicNetwork networkGPU = EncogUtility.simpleFeedForward(2, 4, 0, 1,
				false);
		Randomizer randomizer = new ConsistentRandomizer(-1, 1);
		randomizer.randomize(networkCPU);
		randomizer.randomize(networkGPU);
		networkCPU.getStructure().updateFlatNetwork();
		networkGPU.getStructure().updateFlatNetwork();
		System.out.println("Starting Weights:");
		displayWeights(networkCPU, networkGPU);

		final Propagation trainCPU = new ResilientPropagation(networkCPU,
				trainingSet);
		final Propagation trainGPU = new ResilientPropagation(networkGPU,
				trainingSet);
		Encog.getInstance().initCL();
		trainGPU.assignOpenCL();

		for (int iteration = 1; iteration <= 3; iteration++) {
			trainCPU.iteration();
			trainGPU.iteration();

			System.out.println("Iteration #" + iteration);
			System.out.println("CPU-Error: " + trainCPU.getError());
			System.out.println("GPU-Error: " + trainGPU.getError());
			displayWeights(networkCPU, networkGPU);
		}
	}
}
