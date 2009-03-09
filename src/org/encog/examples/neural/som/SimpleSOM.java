package org.encog.examples.neural.som;

import org.encog.neural.activation.ActivationBiPolar;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class SimpleSOM {
	
	public static void main(String args[])
	{
		// Create the neural network.
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(4));
		network.addLayer(new BasicLayer(2));
	}
}
