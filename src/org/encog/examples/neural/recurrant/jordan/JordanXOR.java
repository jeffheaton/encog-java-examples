package org.encog.examples.neural.recurrant.jordan;



import org.encog.bot.browse.extract.ListExtractListener;
import org.encog.examples.neural.util.TemporalXOR;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.ContextLayer;
import org.encog.neural.networks.layers.Layer;
import org.encog.neural.networks.synapse.SynapseType;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.backpropagation.Backpropagation;
import org.encog.neural.networks.training.strategy.Greedy;
import org.encog.neural.networks.training.strategy.SmartLearningRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JordanXOR {
	
	
	
	public static void main(String args[])
	{
		
		TemporalXOR temp = new TemporalXOR();
		NeuralDataSet trainingSet = temp.generate(100);
		
		// construct an Jordan type network
		Layer hidden,output;
		Layer context = new ContextLayer(2);
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(1));
		network.addLayer(hidden = new BasicLayer(2));
		network.addLayer(output = new BasicLayer(1));
		
		output.addNext(context,SynapseType.OneToOne);
		context.addNext(hidden);
		
		network.reset();
		
		// train the neural network
		final Train train = new Backpropagation(network, trainingSet,
				0.01, 0.0);

		int epoch = 1;
		
		train.addStrategy(new SmartLearningRate());

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while ((epoch < 50000) && (train.getError() > 0.001));
		
	}
}
