package org.encog.examples.neural.benchmark;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.flat.FlatNetwork;
import org.encog.engine.network.train.prop.TrainFlatNetworkBackPropagation;
import org.encog.engine.util.EngineArray;
import org.encog.engine.util.Format;
import org.encog.engine.util.Stopwatch;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.logging.Logging;

public class SimpleBenchmark {
	
	
    public static final int ROW_COUNT = 100000;
    public static final int INPUT_COUNT = 10;
    public static final int OUTPUT_COUNT = 1;
    public static final int HIDDEN_COUNT = 20;
    public static final int ITERATIONS = 10;

    public static void BenchmarkEncog(double[][] input, double[][] output)
    {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, input[0].length));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, HIDDEN_COUNT));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, output[0].length));
        network.getStructure().finalizeStructure();
        network.reset();

        NeuralDataSet trainingSet = new BasicNeuralDataSet(input, output);

        // train the neural network
        Train train = new Backpropagation(network, trainingSet,
               0.7, 0.9);

        Stopwatch sw = new Stopwatch();
        sw.start();
        // run epoch of learning procedure
        for (int i = 0; i < ITERATIONS; i++)
        {
            train.iteration();
        }
        sw.stop();

        System.out.println("Encog:" + Format.formatInteger((int)sw.getElapsedMilliseconds()) + "ms" );
    }

    public static void BenchmarkEncogFlat(double[][] input, double[][] output)
    {
        FlatNetwork network = new FlatNetwork(input[0].length, HIDDEN_COUNT, 0, output[0].length, false);

        BasicNeuralDataSet trainingSet = new BasicNeuralDataSet(input, output);

        TrainFlatNetworkBackPropagation train = new TrainFlatNetworkBackPropagation(network, trainingSet, 0.7, 0.7);

        double[] a = new double[2];
        double[] b = new double[1];

        Stopwatch sw = new Stopwatch();
        sw.start();
        // run epoch of learning procedure
        for (int i = 0; i < ITERATIONS; i++)
        {
            //network.Compute(a,b);
            train.iteration();
        }
        sw.stop();

        System.out.println("EncogFlat:" + Format.formatInteger((int)sw.getElapsedMilliseconds()) + "ms" );
    }


    static double[][] Generate(int rows, int columns)
    {
        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                result[i][j] = Math.random();
            }
        }

        return result;
    }

    
    
	public static void main(String[] args)
	{
		Logging.stopConsoleLogging();
        // initialize input and output values
        double[][] input = Generate(ROW_COUNT, INPUT_COUNT);
        double[][] output = Generate(ROW_COUNT, OUTPUT_COUNT);

        BenchmarkEncog(input, output);
        BenchmarkEncogFlat(input, output);        
	}
}
