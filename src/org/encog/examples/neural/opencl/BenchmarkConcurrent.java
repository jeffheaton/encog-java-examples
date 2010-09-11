package org.encog.examples.neural.opencl;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.engine.util.Stopwatch;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.concurrent.ConcurrentTrainingManager;
import org.encog.neural.networks.training.concurrent.TrainingJob;
import org.encog.neural.networks.training.concurrent.performers.ConcurrentTrainingPerformerCPU;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.strategy.end.EndIterationsStrategy;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

public class BenchmarkConcurrent {
	public TrainingJob generateTrainingJob()
	{
        int outputSize = 2;
        int inputSize = 10;
        int trainingSize = 100000;

        NeuralDataSet training = RandomTrainingFactory.generate(1000,
            trainingSize, inputSize, outputSize, -1, 1);
        BasicNetwork network = EncogUtility.simpleFeedForward(
            training.getInputSize(), 6, 0, training.getIdealSize(), true);
        network.reset();
        
        Train train = new ResilientPropagation(network,training);
        train.addStrategy(new EndIterationsStrategy(100));
        
        return new TrainingJob(network,train);
	}
	
	public int benchmark()
	{
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();
		ConcurrentTrainingManager manager = ConcurrentTrainingManager.getInstance();
		
		manager.setReport(new ConsoleStatusReportable());
		manager.detectPerformers();

		manager.clearQueue();
		for(int i=0;i<10;i++)
			manager.addTrainingJob(generateTrainingJob());
		
		manager.start();
		System.out.println("Manager has started.");
		manager.join();
		System.out.println("Manager has stopped.");
		Encog.getInstance().shutdown();
		stopWatch.stop();
		return (int)(stopWatch.getElapsedMilliseconds()/1000);
	}
	
	public void run()
	{
		Logging.stopConsoleLogging();
		System.out.println("Performing CPU-only test.");
		int cpu = benchmark();
		System.out.println("CPU-only took: " + cpu + " seconds.");
		Logging.stopConsoleLogging();
		Encog.getInstance().initCL();
		System.out.println("Performing CPU&GPU test.");
		int gpu = benchmark();
		System.out.println("CPU-only took: " + gpu + " seconds.");
	}
	
	public static void main(String[] args)
	{		
		BenchmarkConcurrent program = new BenchmarkConcurrent();
		program.run();
	}
}
