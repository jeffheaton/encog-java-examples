package org.encog.examples.neural.opencl;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.engine.util.Format;
import org.encog.engine.util.Stopwatch;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.concurrent.ConcurrentTrainingManager;
import org.encog.neural.networks.training.concurrent.jobs.RPROPJob;
import org.encog.neural.networks.training.concurrent.jobs.TrainingJob;
import org.encog.neural.networks.training.strategy.end.EndIterationsStrategy;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * Demonstrates concurrent training.  Will make use of multiple OpenCL devices, as well as your CPU.
 *
 */
public class BenchmarkConcurrent {

	public static final int OUTPUT_SIZE = 2;
	public static final int INPUT_SIZE = 10;
	public static final int HIDDEN1 = 6;
	public static final int HIDDEN2 = 0;
	public static final int TRAINING_SIZE = 1000;
	public static final int ITERATIONS = 1000;
	public static final int JOBS = 50;
	
	/**
	 * The OpenCL ratio.  This specifies how much each cycle should split up the training.
	 * 1.0 is the maximum, but it might timeout your GPU.
	 */
	public static final double OPENCL_RATIO = 1.0;

	/**
	 * Iterations per cycle.  Higher numbers load up OpenCL more, but too high may 
	 * timeout your GPU, if your OS has a timeout.  Do not set this higher than one
	 * unless you are using the MAX openCL ratio of 1.0, otherwise it is pointless, 
	 * and will throw an error.
	 */
	public static final int ITERATIONS_PER = 1;
	
	public static final double LOCAL_RATIO = 1.0;
	public static final double GLOBAL_RATIO = 1.0;
	public static final double SEGMENTATION_RATIO = 1.0;

	/**
	 * Max cores to use, 0=autodetect, -1=no CPU cores, other number is the # of cores.
	 */
	public static final int MAX_CORES = 0;

	public TrainingJob generateTrainingJob(ConcurrentTrainingManager manager) {
		NeuralDataSet training = RandomTrainingFactory.generate(1000,
				TRAINING_SIZE, INPUT_SIZE, OUTPUT_SIZE, -1, 1);
		BasicNetwork network = EncogUtility.simpleFeedForward(
				training.getInputSize(), HIDDEN1, HIDDEN2,
				training.getIdealSize(), true);
		network.reset();

		RPROPJob job = new RPROPJob(network,training,true,LOCAL_RATIO,GLOBAL_RATIO,SEGMENTATION_RATIO,ITERATIONS_PER);
		job.getStrategies().add(new EndIterationsStrategy(ITERATIONS));
		
		manager.addTrainingJob(job);
		return job;

	}

	public int benchmark(boolean splitCores) {
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();
		ConcurrentTrainingManager manager = ConcurrentTrainingManager
				.getInstance();

		manager.setReport(new ConsoleStatusReportable());
		manager.detectPerformers(splitCores, 0);
		System.out.println("Device(s) in use:");
		System.out.println(manager.toString());
		manager.clearQueue();
		for (int i = 0; i < JOBS; i++)
			generateTrainingJob(manager);

		manager.start();
		System.out.println("Manager has started.");
		manager.join();
		System.out.println("Manager has stopped.");

		stopWatch.stop();
		return (int) (stopWatch.getElapsedMilliseconds() / 1000);
	}

	public void run() {
		Logging.stopConsoleLogging();
		System.out.println("* * * Performing CPU-Only Test * * *");
		int cpu = benchmark(false);
		System.out.println("CPU-only took: " + cpu + " seconds.");

		System.out.println();
		System.out.println("* * * Performing CPU-Only(split cores) Test * * *");
		int cpuSplit = benchmark(true);
		System.out.println("CPU-only(split cores took: " + cpuSplit
				+ " seconds.");
		Logging.stopConsoleLogging();
		Encog.getInstance().initCL();
		System.out.println();

		System.out.println("* * * Performing OpenCL Test * * *");
		Encog.getInstance().initCL();
		int gpu = benchmark(true);

		System.out.println("OpenCL took: " + gpu + " seconds.");
		System.out.println();
		System.out.println("Final times:");
		System.out.println("CPU-Only       : " + cpu + "ms");
		System.out.println("CPU-Split Cores: " + cpuSplit + "ms");
		System.out.println("CPU and OpenCL : " + gpu + "ms");
	}

	public static void main(String[] args) {
		BenchmarkConcurrent program = new BenchmarkConcurrent();
		program.run();
		Encog.getInstance().shutdown();
	}
}
