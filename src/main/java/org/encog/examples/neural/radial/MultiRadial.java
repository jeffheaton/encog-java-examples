/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.examples.neural.radial;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.encog.Encog;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.pattern.RadialBasisPattern;
import org.encog.neural.rbf.RBFNetwork;
import org.encog.neural.rbf.training.SVDTraining;

/**
 * 
 * @author jheaton
 *
 */
public class MultiRadial {
    /// <summary>
    /// Input for the XOR function.
    /// </summary>
    public static double[][] INPUT;
    public static double[][] IDEAL;

    static public void main(String[] args) throws IOException
    {
        //Specify the number of dimensions and the number of neurons per dimension
        int dimensions = 2;
        int numNeuronsPerDimension = 7;

        //Set the standard RBF neuron width. 
        //Literature seems to suggest this is a good default value.
        double volumeNeuronWidth = 2.0 / numNeuronsPerDimension;

        //RBF can struggle when it comes to flats at the edge of the sample space.
        //We have added the ability to include wider neurons on the sample space boundary which greatly
        //improves fitting to flats
        boolean includeEdgeRBFs = true;

        //General setup is the same as before
        RadialBasisPattern pattern = new RadialBasisPattern();
        pattern.setInputNeurons(dimensions);
        pattern.setOutputNeurons(1);

        //Total number of neurons required.
        //Total number of Edges is calculated possibly for future use but not used any further here
        int numNeurons = (int)Math.pow(numNeuronsPerDimension, dimensions);
        int numEdges = (int)(dimensions * Math.pow(2, dimensions - 1));

        pattern.addHiddenLayer(numNeurons);

        RBFNetwork network = (RBFNetwork)pattern.generate();

        //Position the multidimensional RBF neurons, with equal spacing, within the provided sample space from 0 to 1.
        network.setRBFCentersAndWidthsEqualSpacing(0, 1, RBFEnum.Gaussian, volumeNeuronWidth, includeEdgeRBFs);

        //Create some training data that can not easily be represented by gaussians
        //There are other training examples for both 1D and 2D
        //Degenerate training data only provides outputs as 1 or 0 (averaging over all outputs for a given set of inputs would produce something approaching the smooth training data).
        //Smooth training data provides true values for the provided input dimensions.
        create2DSmoothTainingDataGit();

        //Create the training set and train.
        MLDataSet trainingSet = new BasicMLDataSet(INPUT, IDEAL);
        MLTrain train = new SVDTraining(network, trainingSet);

        //SVD is a single step solve
        int epoch = 1;
        do
        {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while ((epoch < 1) && (train.getError() > 0.001));

        // test the neural network
        System.out.println("Neural Network Results:");

        //Create a testing array which may be to a higher resoltion than the original training data
        set2DTestingArrays(100);
        trainingSet = new BasicMLDataSet(INPUT, IDEAL);

        FileWriter outFile = new FileWriter("results.csv");
        PrintWriter out = new PrintWriter(outFile);
        
        
            for (MLDataPair pair : trainingSet)
            {
                MLData output = network.compute(pair.getInput());
                //1D//sw.WriteLine(InverseScale(pair.Input[0]) + ", " + Chop(InverseScale(output[0])));// + ", " + pair.Ideal[0]);
                out.println(inverseScale(pair.getInputArray()[0]) + ", " + inverseScale(pair.getInputArray()[1]) + ", " + chop(inverseScale(output.getData(0))));// + ", " + pair.Ideal[0]);// + ",ideal=" + pair.Ideal[0]);
                //3D//sw.WriteLine(InverseScale(pair.Input[0]) + ", " + InverseScale(pair.Input[1]) + ", " + InverseScale(pair.Input[2]) + ", " + Chop(InverseScale(output[0])));// + ", " + pair.Ideal[0]);// + ",ideal=" + pair.Ideal[0]);
                //Console.WriteLine(pair.Input[0] + ", actual=" + output[0] + ",ideal=" + pair.Ideal[0]);
            }
            out.close();


        System.out.println("\nFit output saved to results.csv");
        
        Encog.getInstance().shutdown();
    }

    static double scale(double x)
    {
        return (x * 0.7) + 0.15;
    }

    static double inverseScale(double x)
    {
        return (x - 0.15) / 0.7;
    }

    static double chop(double x)
    {
        if (x > 0.99)
            return 0.99;
        else if (x < 0)
            return 0;
        else
            return x;
    }

    private static double[][] convertColumnsTo2DSurface(double[][] cols, int valueCol)
    {
        //if (cols[0].Length != 3)
        //    throw new Exception("Incorrect number of cols detected.");

        double sideLength = Math.sqrt(cols.length);
        double[][] surface = new double[(int)sideLength + 1][];

        for (int i = 0; i < surface.length; i++)
        {
            surface[i] = new double[surface.length];
        }

        for (int i = 0; i < cols.length; i++)
        {
            //[0] is x
            //[1] is y
            //Boundary bottom 
            //int rowIndex = (int)Math.Round(((cols[i][0]) * (sideLength-1)), 6);
            //int columnIndex = (int)Math.Round(((cols[i][1]) * (sideLength-1)), 6);

            //Boundary middle
            int rowIndex = (int)Math.round(((cols[i][0] - 0.05) * (sideLength)) * 6000)/6000;
            int columnIndex = (int)Math.round(((cols[i][1] - 0.05) * (sideLength)) * 6000)/6000;

            surface[0][rowIndex + 1] = cols[i][0];
            surface[columnIndex + 1][0] = cols[i][1];
            surface[columnIndex + 1][rowIndex + 1] = cols[i][valueCol];
        }

        //fix the 0,0 value
        surface[0][0] = Double.NaN;

        return surface;
    }

    private static double[][] convert2DSurfaceToColumns(double[][] surface)
    {
        int totalRows = (surface.length - 1) * (surface.length - 1);
        double[][] cols = new double[totalRows][];

        for (int i = 1; i < surface.length; i++)
        {
            for (int j = 1; j < surface[i].length; j++)
            {
                double cellWidth = (1.0 / (2.0 * (double)(surface.length - 1)));
                cols[(i - 1) * (surface.length - 1) + (j - 1)] = new double[3];
                //For midpoints
                cols[(i - 1) * (surface.length - 1) + (j - 1)][0] = ((double)(i - 1) / (double)(surface.length - 1)) + cellWidth;
                cols[(i - 1) * (surface.length - 1) + (j - 1)][1] = ((double)(j - 1) / (double)(surface.length - 1)) + cellWidth;
                //For actual value
                //cols[(i - 1) * (surface.Length - 1) + (j - 1)][0] = ((double)(i - 1) / (double)(surface.Length - 1));
                //cols[(i - 1) * (surface.Length - 1) + (j - 1)][1] = ((double)(j - 1) / (double)(surface.Length - 1));
                cols[(i - 1) * (surface.length - 1) + (j - 1)][2] = surface[j][i];
            }
        }

        return cols;
    }

    private static void set1DTestingArrays(int sideLength)
    {
        int iLimit = sideLength;

        INPUT = new double[(iLimit + 1)][1];
        IDEAL = new double[(iLimit + 1)][1];

        for (int i = 0; i <= iLimit; i++)
        {
            INPUT[i] = new double[1];
            IDEAL[i] = new double[1];

            double x = (double)i / (double)iLimit;

            INPUT[i][0] = scale(((double)i / ((double)iLimit)));
            IDEAL[i][0] = 0;
        }
    }

    private static void set2DTestingArrays(int sideLength)
    {
        int iLimit = sideLength;
        int kLimit = sideLength;

        INPUT = new double[(iLimit + 1) * (kLimit + 1)][];
        IDEAL = new double[(iLimit + 1) * (kLimit + 1)][];

        for (int i = 0; i <= iLimit; i++)
        {
            for (int k = 0; k <= kLimit; k++)
            {
                INPUT[i * (kLimit + 1) + k] = new double[2];
                IDEAL[i * (kLimit + 1) + k] = new double[1];

                double x = (double)i / (double)iLimit;
                double y = (double)k / (double)kLimit;

                INPUT[i * (kLimit + 1) + k][0] = scale(((double)i / ((double)iLimit)));
                INPUT[i * (kLimit + 1) + k][1] = scale(((double)k / ((double)kLimit)));
                IDEAL[i * (kLimit + 1) + k][0] = 0;
            }
        }
    }

    private static void set3DTestingArrays(int sideLength)
    {
        int iLimit = sideLength;
        int kLimit = sideLength;
        int jLimit = sideLength;

        INPUT = new double[(iLimit + 1) * (kLimit + 1) * (jLimit + 1)][];
        IDEAL = new double[(iLimit + 1) * (kLimit + 1) * (jLimit + 1)][];

        for (int i = 0; i <= iLimit; i++)
        {
            for (int k = 0; k <= kLimit; k++)
            {
                for (int j = 0; j <= jLimit; j++)
                {
                    int index = (i * (kLimit + 1) * (jLimit + 1)) + (j * (kLimit + 1)) + k;
                    INPUT[index] = new double[3];
                    IDEAL[index] = new double[1];

                    //double x = (double)i / (double)iLimit;
                    //double y = (double)k / (double)kLimit;
                    //double z = (double)j / (double)jLimit;

                    INPUT[index][0] = scale(((double)i / ((double)iLimit)));
                    INPUT[index][1] = scale(((double)k / ((double)kLimit)));
                    INPUT[index][2] = scale(((double)j / ((double)jLimit)));
                    IDEAL[index][0] = 0;
                }
            }
        }
    }

    static void create2DDegenerateTainingDataHill()
    {
        Random r = new Random();

        int iLimit = 30;
        int kLimit = 30;
        int jLimit = 1;

        INPUT = new double[jLimit * iLimit * kLimit][];
        IDEAL = new double[jLimit * iLimit * kLimit][];

        for (int i = 0; i < iLimit; i++)
        {
            for (int k = 0; k < kLimit; k++)
            {
                for (int j = 0; j < jLimit; j++)
                {
                    INPUT[i * jLimit * kLimit + k * jLimit + j] = new double[2];
                    IDEAL[i * jLimit * kLimit + k * jLimit + j] = new double[1];

                    double x = (double)i / (double)iLimit;
                    double y = (double)k / (double)kLimit;

                    INPUT[i * jLimit * kLimit + k * jLimit + j][0] = ((double)i / ((double)iLimit));
                    INPUT[i * jLimit * kLimit + k * jLimit + j][1] = ((double)k / ((double)iLimit));
                    IDEAL[i * jLimit * kLimit + k * jLimit + j][0] = (r.nextDouble() < (Math.exp(-((x - 0.6) * (x - 0.6) + (y - 0.5) * (y - 0.5)) * 3) - 0.1)) ? 1 : 0;

                }
            }
        }
    }

    static void create2DSmoothTainingDataHill()
    {
        Random r = new Random();

        int iLimit = 100;
        int kLimit = 100;
        int jLimit = 10000;

        INPUT = new double[(iLimit + 1) * (kLimit + 1)][];
        IDEAL = new double[(iLimit + 1) * (kLimit + 1)][];

        for (int i = 0; i <= iLimit; i++)
        {
            for (int k = 0; k <= kLimit; k++)
            {
                INPUT[i * (kLimit + 1) + k] = new double[2];
                IDEAL[i * (kLimit + 1) + k] = new double[1];

                double average = 0;

                double x = (double)i / (double)iLimit;
                double y = (double)k / (double)kLimit;

                double expression = (Math.exp(-((x - 0.5) * (x - 0.5) + (y - 0.6) * (y - 0.6)) * 3) - 0.1);

                //if (r.NextDouble() < 0.4) jLimit = 5; else jLimit = 10;

                for (int j = 0; j < jLimit; j++)
                {
                    average += (r.nextDouble() < expression) ? 1 : 0;
                }

                INPUT[i * (kLimit + 1) + k][0] = scale(((double)i / ((double)iLimit)));
                INPUT[i * (kLimit + 1) + k][1] = scale(((double)k / ((double)kLimit)));
                IDEAL[i * (kLimit + 1) + k][0] = scale((average / (double)jLimit));
            }
        }
    }

    static void create2DSmoothTainingDataGit()
    {
        Random r = new Random();

        int iLimit = 10;
        int kLimit = 10;
        //int jLimit = 100;

        INPUT = new double[(iLimit + 1) * (kLimit + 1)][];
        IDEAL = new double[(iLimit + 1) * (kLimit + 1)][];

        for (int i = 0; i <= iLimit; i++)
        {
            for (int k = 0; k <= kLimit; k++)
            {
                INPUT[i * (kLimit + 1) + k] = new double[2];
                IDEAL[i * (kLimit + 1) + k] = new double[1];

                double x = (double)i / (double)iLimit;
                double y = (double)k / (double)kLimit;

                double expression = ((x + 1.0 / 3.0) * (2 + Math.log10((y / (x + 0.1)) + 0.1))) / 3;

                INPUT[i * (kLimit + 1) + k][0] = scale(((double)i / ((double)iLimit)));
                INPUT[i * (kLimit + 1) + k][1] = scale(((double)k / ((double)kLimit)));
                IDEAL[i * (kLimit + 1) + k][0] = scale(expression);
            }
        }
    }

    static void create2DDegenerateTainingDataGit()
    {
        Random r = new Random();

        int iLimit = 10;
        int kLimit = 10;
        int jLimit = 10;

        INPUT = new double[jLimit * iLimit * kLimit][];
        IDEAL = new double[jLimit * iLimit * kLimit][];

        for (int i = 0; i < iLimit; i++)
        {
            for (int k = 0; k < kLimit; k++)
            {
                double x = (double)i / (double)iLimit;
                double y = (double)k / (double)kLimit;

                for (int j = 0; j < jLimit; j++)
                {
                    INPUT[i * jLimit * kLimit + k * jLimit + j] = new double[2];
                    IDEAL[i * jLimit * kLimit + k * jLimit + j] = new double[1];

                    double expression = ((x + 1.0 / 3.0) * (2 + Math.log10((y / (x + 0.1)) + 0.1))) / 3; ;

                    INPUT[i * jLimit * kLimit + k * jLimit + j][0] = ((double)i / ((double)iLimit));
                    INPUT[i * jLimit * kLimit + k * jLimit + j][1] = ((double)k / ((double)iLimit));
                    IDEAL[i * jLimit * kLimit + k * jLimit + j][0] = (r.nextDouble() < expression) ? 1 : 0;

                }
            }
        }
    }

    static void create1DDegenerateTrainingDataLine()
    {
        Random r = new Random(14768);

        int iLimit = 10;
        int jLimit = 100;

        INPUT = new double[iLimit * jLimit][];
        IDEAL = new double[iLimit * jLimit][];

        for (int i = 0; i < iLimit; i++)
        {
            for (int j = 0; j < jLimit; j++)
            {
                INPUT[i * jLimit + j] = new double[1];
                IDEAL[i * jLimit + j] = new double[1];

                double x = (double)i / (double)iLimit;

                INPUT[i * jLimit + j][0] = scale(x);
                IDEAL[i * jLimit + j][0] = scale((r.nextDouble() < x) ? 1 : 0);
            }
        }
    }

    static void create1DSmoothTrainingDataLine()
    {
        Random r = new Random(14768);

        int iLimit = 1000;
        int jLimit = 1;

        INPUT = new double[iLimit][];
        IDEAL = new double[iLimit][];

        for (int i = 0; i < iLimit; i++)
        {
            INPUT[i] = new double[1];
            IDEAL[i] = new double[1];
            double average = 0;
            double x = (double)i / (double)iLimit;

            for (int j = 0; j < jLimit; j++)
                average += (r.nextDouble() < x) ? 1 : 0;

            INPUT[i][0] = scale(x);
            IDEAL[i][0] = scale((double)average / (double)jLimit);
        }
    }

    static void create1DSmoothTrainingDataCurveSimple()
    {
        Random r = new Random(14768);

        int iLimit = 20;
        int jLimit = 10;

        INPUT = new double[iLimit][];
        IDEAL = new double[iLimit][];

        for (int i = 0; i < iLimit; i++)
        {
            INPUT[i] = new double[1];
            IDEAL[i] = new double[1];
            double average = 0;
            double x = (double)i / (double)iLimit;

            for (int j = 0; j < jLimit; j++)
                average += (r.nextDouble() < (-4 * Math.pow(x, 2) + 4 * x)) ? 1 : 0;

            INPUT[i][0] = scale(x);
            IDEAL[i][0] = scale((double)average / (double)jLimit);
        }
    }

    static void create1DSmoothTrainingDataCurveAdv()
    {
        Random r = new Random(14768);

        int iLimit = 100;
        int jLimit = 100;

        INPUT = new double[iLimit][];
        IDEAL = new double[iLimit][];

        for (int i = 0; i < iLimit; i++)
        {
            INPUT[i] = new double[1];
            IDEAL[i] = new double[1];
            double average = 0;
            double x = (double)i / (double)iLimit;

            //double y = (-7.5 * Math.Pow(x, 4)) + (21.3 * Math.Pow(x, 3)) + (-22.3 * Math.Pow(x, 2)) + (10.4 * x) - 0.8;
            double y = ((Math.exp(2.0 * (x * 4.0 - 1)) - 1.0) / (Math.exp(2.0 * (x * 4.0 - 1)) + 1.0)) / 2 + 0.5;

            for (int j = 0; j < jLimit; j++)
            {
                average += (r.nextDouble() < y) ? 1 : 0;
            }

            INPUT[i][0] = scale(x);
            IDEAL[i][0] = scale((double)average / (double)jLimit);
        }
    }


}
