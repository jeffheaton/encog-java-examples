package org.encog.examples.neural.predict.simple;

import org.encog.neural.activation.ActivationLinear;
import org.encog.neural.activation.ActivationSigmoid;
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
