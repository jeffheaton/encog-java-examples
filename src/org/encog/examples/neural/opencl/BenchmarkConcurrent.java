package org.encog.examples.neural.opencl;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.engine.util.Stopwatch;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.concurrent.ConcurrentTrainingManager;
import org.encog.neural.networks.training.concurrent.jobs.TrainingJob;
import org.encog.neural.networks.training.strategy.end.EndIterationsStrategy;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

public class BenchmarkConcurrent {
	
	
	public static final int OUTPUT_SIZE = 2;
	public static final int INPUT_SIZE = 10;
	public static final int HIDDEN1 = 6;
	public static final int HIDDEN2 = 0;
	public static final int TRAINING_SIZE = 10000;
	public static final int ITERATIONS = 1000;
	public static final int JOBS = 4;
	
	public TrainingJob generateTrainingJob(ConcurrentTrainingManager manager)
	{
        NeuralDataSet training = RandomTrainingFactory.generate(1000,
        		TRAINING_SIZE, INPUT_SIZE, OUTPUT_SIZE, -1, 1);
        BasicNetwork network = EncogUtility.simpleFeedForward(
            training.getInputSize(), HIDDEN1, HIDDEN2, training.getIdealSize(), true);
        network.reset();
        
        return manager.addTrainRPROP(
        		     		network,training,new EndIterationsStrategy(ITERATIONS));
        

	}
	
	public int benchmark()
	{
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();
		ConcurrentTrainingManager manager = ConcurrentTrainingManager.getInstance();
		
		manager.setReport(new ConsoleStatusReportable());
		manager.detectPerformers();
		System.out.println(manager.toString());
		manager.clearQueue();
		for(int i=0;i<JOBS;i++)
			generateTrainingJob(manager);
		
		manager.start();
		System.out.println("Manager has started.");
		manager.join();
		System.out.println("Manager has stopped.");

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
		System.out.println("GPU&CPU-only took: " + gpu + " seconds.");
	}
	
	public static void main(String[] args)
	{		
		BenchmarkConcurrent program = new BenchmarkConcurrent();
		program.run();
		Encog.getInstance().shutdown();
	}
}
