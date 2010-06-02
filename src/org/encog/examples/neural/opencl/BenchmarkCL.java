package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.Format;
import org.encog.util.Stopwatch;
import org.encog.util.benchmark.RandomTrainingFactory;
import org.encog.util.cl.EncogCLError;
import org.encog.util.simple.EncogUtility;

public class BenchmarkCL {
	
    public static long benchmarkCPU(BasicNetwork network, NeuralDataSet training)
    {
        ResilientPropagation train = new ResilientPropagation(network, training);

        train.iteration();

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for (int i = 0; i < 100; i++)
        {
            train.iteration();
        }
        stopwatch.stop();

        return stopwatch.getElapsedMilliseconds();
    }

    public static long benchmarkCL(BasicNetwork network, NeuralDataSet training)
    {
        ResilientPropagation train = new ResilientPropagation(network, training);
        
        train.iteration();

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for (int i = 0; i < 100; i++)
        {
            train.iteration();
        }
        stopwatch.stop();

        return stopwatch.getElapsedMilliseconds();
    }


    /// <summary>
    /// Program entry point.
    /// </summary>
    /// <param name="args">Not used.</param>
    public static void main(String[] args)
    {
        try
        {
            int outputSize = 2;
            int inputSize = 10;
            int trainingSize = 100000;

            NeuralDataSet training = RandomTrainingFactory.generate(
                trainingSize, inputSize, outputSize, -1, 1);
            BasicNetwork network = EncogUtility.simpleFeedForward(
                training.getInputSize(), 6, 0, training.getIdealSize(), true);
            network.reset();

            System.out.println("Running non-OpenCL test.");
            long cpuTime = benchmarkCPU(network, training);
            System.out.println("Non-OpenCL test took " + cpuTime + "ms.");
            System.out.println("Starting OpenCL");
            Encog.getInstance().initCL();
            System.out.println("Running OpenCL test.");
            long clTime = benchmarkCL(network, training);
            System.out.println("OpenCL test took " + clTime + "ms.");

            double diff = ((double)cpuTime - (double)clTime);
            String percent = Format.formatPercent((double)cpuTime/(double)clTime);
            System.out.println("OpenCL Performed at " + percent + " the speed of non-OpenCL");
        }
        catch (EncogCLError ex)
        {
        	System.out.println("Can't startup CL, make sure you have drivers loaded.");
            System.out.println(ex.toString());
        }
    }

}
