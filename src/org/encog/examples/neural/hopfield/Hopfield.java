package org.encog.examples.neural.hopfield;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.BasicNetwork;

/**
 * ConsoleHopfield: Simple console application that shows how to
 * use a Hopfield Neural Network.
 */
public class Hopfield {

	/**
	 * Convert a boolean array to the form [T,T,F,F]
	 * 
	 * @param b
	 *            A boolen array.
	 * @return The boolen array in string form.
	 */
	public static String formatBoolean(BiPolarNeuralData b) {
		final StringBuilder result = new StringBuilder();
		result.append('[');
		for (int i = 0; i < b.size(); i++) {
			if (b.getBoolean(i)) {
				result.append("T");
			} else {
				result.append("F");
			}
			if (i != b.size() - 1) {
				result.append(",");
			}
		}
		result.append(']');
		return (result.toString());
	}

	/**
	 * A simple main method to test the Hopfield neural network.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(final String args[]) {

		// Create the neural network.
		BasicNetwork network = new BasicNetwork();
		//network.addLayer(new HopfieldLayer(4));
		// This pattern will be trained
		final boolean[] pattern1 = { true, true, false, false };
		// This pattern will be presented
		final boolean[] pattern2 = { true, false, false, false };
		BiPolarNeuralData result;
		
		BiPolarNeuralData data1 = new BiPolarNeuralData(pattern1);
		BiPolarNeuralData data2 = new BiPolarNeuralData(pattern2);

		// train the neural network with pattern1
		System.out.println("Training Hopfield network with: "
				+ formatBoolean(data1));

		//TrainHopfield train = new TrainHopfield(data1, network);
		//train.iteration();
		// present pattern1 and see it recognized
		result = (BiPolarNeuralData)network.compute(data1);
		System.out.println("Presenting pattern:" + formatBoolean(data1)
				+ ", and got " + formatBoolean(result));
		// Present pattern2, which is similar to pattern 1. Pattern 1
		// should be recalled.
		result = (BiPolarNeuralData)network.compute(data2);
		System.out.println("Presenting pattern:" + formatBoolean(data2)
				+ ", and got " + formatBoolean(result));

	}

}
