package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.network.train.prop.OpenCLTrainingProfile;
import org.encog.engine.network.train.prop.TrainFlatNetworkOpenCL;
import org.encog.engine.opencl.EncogCLDevice;
import org.encog.engine.opencl.EncogCLError;
import org.encog.engine.util.Format;
import org.encog.engine.util.Stopwatch;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.strategy.end.EndIterationsStrategy;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * Performs a simple benchmark of your first OpenCL device, compared to the CPU.
 * If you have multiple OpenCL devices(i.e. two GPU's) this benchmark will only
 * take advantage of one. To see multiple OpenCL devices used in parallel, use
 * the BenchmarkConcurrent example.
 * 
 */
public class BenchmarkCL {
	
	public static final int GLOBAL_SIZE = 200;
	public static final int BENCHMARK_ITERATIONS = 100;
	public static final double OPENCL_RATIO = 1.0;
	public static final int ITERATIONS_PER_CYCLE = 1;
	public static OpenCLTrainingProfile profile;
	
	public static long benchmarkCPU(BasicNetwork network, NeuralDataSet training) {
		ResilientPropagation train = new ResilientPropagation(network, training);
		EndIterationsStrategy stop;
		train.addStrategy(stop = new EndIterationsStrategy(BENCHMARK_ITERATIONS));
		train.iteration(); // warmup

		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		
		while( !stop.shouldStop() ) {
			train.iteration(ITERATIONS_PER_CYCLE);
		}
		stopwatch.stop();

		return stopwatch.getElapsedMilliseconds();
	}

	public static long benchmarkCL(BasicNetwork network, NeuralDataSet training) {
		profile = new OpenCLTrainingProfile(Encog.getInstance().getCL().chooseDevice());

		System.out.println("Using device: " + profile.getDevice().toString());
		ResilientPropagation train = new ResilientPropagation(network,
				training, profile);
		
		train.iteration(); // warmup
		
		EndIterationsStrategy stop;
		
		train.addStrategy(stop = new EndIterationsStrategy(BENCHMARK_ITERATIONS));
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();

		while( !stop.shouldStop() ) {
			train.iteration(ITERATIONS_PER_CYCLE);
		}
		stopwatch.stop();

		return stopwatch.getElapsedMilliseconds();
	}

	public static void main(String[] args) {
		try {
			Logging.stopConsoleLogging();
			int outputSize = 2;
			int inputSize = 10;
			int trainingSize = 100000;

			NeuralDataSet training = RandomTrainingFactory.generate(1000,
					trainingSize, inputSize, outputSize, -1, 1);
			BasicNetwork network = EncogUtility.simpleFeedForward(training
					.getInputSize(), 6, 0, training.getIdealSize(), true);
			network.reset();

			System.out.println("Running non-OpenCL test.");
			long cpuTime = benchmarkCPU(network, training);
			System.out.println("Non-OpenCL test took " + cpuTime + "ms.");
			System.out.println();

			System.out.println("Starting OpenCL");
			Encog.getInstance().initCL();

			int i = 0;
			System.out
					.println("OpenCL Devices: (Encog will use the first GPU, or CPU if no GPU's)");
			for (EncogCLDevice device : Encog.getInstance().getCL()
					.getDevices()) {
				System.out.println("Device " + i + ": " + device.toString());
				i++;
			}

			System.out.println("Running OpenCL test.");
			long clTime = benchmarkCL(network, training);
			System.out.println("OpenCL test took " + clTime + "ms.");
			System.out.println();
			
			System.out.println("ITERATIONS_PER_CYCLE: " +  ITERATIONS_PER_CYCLE);
			
			System.out.println();
			System.out.println(profile.toString());
			System.out.println();
			String percent = Format.formatPercent((double) cpuTime
					/ (double) clTime);
			System.out.println("OpenCL Performed at " + percent
					+ " the speed of non-OpenCL");
			System.out.println("You will likely get better performance by tuning: ITERATIONS_PER_CYCLE, local ratio, global ratio & segmentation ratio.");
			
		} catch (EncogCLError ex) {
			System.out
					.println("Can't startup CL, make sure you have drivers loaded.");
			System.out.println(ex.toString());
		} finally {
			Encog.getInstance().shutdown();
		}
	}

}
