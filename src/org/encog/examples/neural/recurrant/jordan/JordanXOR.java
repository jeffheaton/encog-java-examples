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
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.backpropagation.Backpropagation;
import org.encog.neural.networks.training.strategy.Greedy;
import org.encog.neural.networks.training.strategy.SmartLearningRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JordanXOR {
	
	static BasicNetwork createJordanNetwork()
	{
		// construct an Jordan type network
		Layer hidden,output;
		Layer context = new ContextLayer(1);
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(1));
		network.addLayer(hidden = new BasicLayer(2));
		network.addLayer(output = new BasicLayer(1));
		
		output.addNext(context,SynapseType.OneToOne);
		context.addNext(hidden);
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	static BasicNetwork createFeedforwardNetwork()
	{
		// construct a feedforward type network

		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(1));
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(1));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	public static double trainNetwork(BasicNetwork network,NeuralDataSet trainingSet)
	{
		// train the neural network
		final NeuralSimulatedAnnealing train = new NeuralSimulatedAnnealing(
				network, trainingSet, 10, 2, 100);
		
		train.addStrategy(new Greedy());

		for(int i=0;i<25;i++) {
			train.iteration();
			System.out.println("Epoch #" + i + " Error:" + train.getError());			
		} 	
		return train.getError();
	}
	
	public static void main(String args[])
	{
		
		TemporalXOR temp = new TemporalXOR();
		NeuralDataSet trainingSet = temp.generate(100);
		
		BasicNetwork jordanNetwork = createJordanNetwork();
		BasicNetwork feedforwardNetwork = createFeedforwardNetwork();
		
		double jordanError = trainNetwork(jordanNetwork,trainingSet);
		double feedforwardError = trainNetwork(feedforwardNetwork,trainingSet);
		
		System.out.println("Best error rate with Jordan Network: " + jordanError);
		System.out.println("Best error rate with Feedforward Network: " + feedforwardError);
	}
}
