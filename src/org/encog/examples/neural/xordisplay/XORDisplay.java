package org.encog.examples.neural.xordisplay;



import java.util.Arrays;

import org.encog.engine.network.flat.FlatNetwork;
import org.encog.engine.network.train.prop.TrainFlatNetworkResilient;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.mathutil.randomize.Randomizer;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

public class XORDisplay {

	public final static int ITERATIONS = 10;

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void displayWeights(FlatNetwork network) {
		System.out.println("Weights:"
				+ Arrays.toString(network.getWeights()));
	}
	
	public static void evaluate(FlatNetwork network, NeuralDataSet trainingSet )
	{
		double[] output = new double[1];
		for(NeuralDataPair pair: trainingSet ) {
			network.compute(pair.getInput().getData(), output);
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output[0] + ",ideal=" + pair.getIdeal().getData(0));
		}
		
	}

	public static FlatNetwork createNetwork() {
		BasicNetwork network = EncogUtility
				.simpleFeedForward(2, 4, 0, 1, false);
		Randomizer randomizer = new ConsistentRandomizer(-1, 1);
		randomizer.randomize(network);
		return network.getStructure().getFlat().clone();
	}

	public static void main(String[] args) {
		Logging.stopConsoleLogging();
		
		NeuralDataSet trainingSet = new BasicNeuralDataSet(XOR_INPUT, XOR_IDEAL);

		FlatNetwork network = createNetwork();

		System.out.println("Starting Weights:");
		displayWeights(network);
		evaluate(network,trainingSet);

		final TrainFlatNetworkResilient train = new TrainFlatNetworkResilient(
				network, trainingSet);

		for (int iteration = 1; iteration <= ITERATIONS; iteration++) {
			train.iteration();

			System.out.println();
			System.out.println("*** Iteration #" + iteration);
			System.out.println("Error: " + train.getError());
			evaluate(network,trainingSet);
			
			System.out.println("LastGrad:"
					+ Arrays.toString(train.getLastGradient()));
			System.out.println("Updates :"
					+ Arrays.toString(train.getUpdateValues()));

			displayWeights(network);
		}
	}
}
