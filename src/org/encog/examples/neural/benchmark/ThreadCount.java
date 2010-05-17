package org.encog.examples.neural.benchmark;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.benchmark.RandomTrainingFactory;

public class ThreadCount {
	
	public static final int INPUT_COUNT = 40;
	public static final int HIDDEN_COUNT = 60;
	public static final int OUTPUT_COUNT = 20;
	
	public static void perform(int thread)
	{
		long start = System.currentTimeMillis();
		final BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(MultiBench.INPUT_COUNT));
		network.addLayer(new BasicLayer(MultiBench.HIDDEN_COUNT));
		network.addLayer(new BasicLayer(MultiBench.OUTPUT_COUNT));
		network.getStructure().finalizeStructure();
		network.reset();
		
		final NeuralDataSet training = RandomTrainingFactory.generate(50000,
				INPUT_COUNT, OUTPUT_COUNT, -1, 1);
		
		ResilientPropagation rprop = new ResilientPropagation(network,training);
		rprop.setNumThreads(thread);
		for(int i=0;i<5;i++)
		{
			rprop.iteration();
		}
		long stop = System.currentTimeMillis();
		System.out.println("Result with " + thread + " was " + (stop-start));
	}
	

	
	public static void main(String[] args)
	{
		for(int i=1;i<16;i++)
		{
			perform(i);
		}
	}
}
