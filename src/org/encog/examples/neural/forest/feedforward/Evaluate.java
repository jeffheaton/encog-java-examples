/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
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
package org.encog.examples.neural.forest.feedforward;

import java.io.File;

import org.encog.app.quant.classify.ClassItem;
import org.encog.app.quant.classify.ClassifyStats;
import org.encog.app.quant.normalize.NormalizationStats;
import org.encog.app.quant.normalize.NormalizeCSV;
import org.encog.engine.util.Format;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.csv.ReadCSV;

public class Evaluate {

    private int[] treeCount = new int[10];
    private int[] treeCorrect = new int[10];

    public void keepScore(int actual, int ideal)
    {
        treeCount[ideal]++;
        if (actual == ideal)
            treeCorrect[ideal]++;
    }

    
    public BasicNetwork loadNetwork()
    {
        String file = Constant.TRAINED_NETWORK_FILE;

        if (!(new File(file)).exists() )
        {
            System.out.println("Can't read file: " + file);
            return null;
        }

        EncogPersistedCollection encog = new EncogPersistedCollection(file);
        BasicNetwork network = (BasicNetwork)encog.find(Constant.TRAINED_NETWORK_NAME);

        if (network == null)
        {
            System.out.println("Can't find network resource: " + Constant.TRAINED_NETWORK_NAME);
            return null;
        }

        return network;
    }

    public NeuralData buildForNetworkInput(NormalizationStats stats, double[] input)
    {
        NeuralData neuralInput = new BasicNeuralData(input.length);
        for (int i = 0; i < input.length; i++)
        {
            neuralInput.setData(i, stats.getStats()[i].normalize(input[i]));
        }

        return neuralInput;
    }

    public ClassItem determineType(ClassifyStats stats, NeuralData output)
    {
        ClassItem item = stats.determineClass(output.getData());
        return item;
    }


    public void evaluate()
    {
        BasicNetwork network = loadNetwork();
      
        ReadCSV csv = new ReadCSV(Constant.EVALUATE_FILE.toString(), false, ',');
        double[] input = new double[Constant.INPUT_COUNT];

        NormalizeCSV norm = new NormalizeCSV();
        norm.readStatsFile(Constant.NORMALIZED_STATS_FILE);
        ClassifyStats stats = new ClassifyStats();
        stats.readStatsFile(Constant.CLASSIFY_STATS_FILE);

        int correct = 0;
        int total = 0;
        while (csv.next())
        {
            total++;
            for (int i = 0; i < input.length; i++)
            {
                input[i] = csv.getDouble(i);
            }

            NeuralData inputData = buildForNetworkInput(norm.getStats(), input);
            NeuralData output = network.compute(inputData);
            ClassItem coverTypeActual = determineType(stats, output);
            String coverTypeIdealStr = csv.get(54);
            int coverTypeIdeal = stats.lookup(coverTypeIdealStr);

            keepScore(coverTypeActual.getIndex(), coverTypeIdeal);

            if (coverTypeActual.getIndex() == coverTypeIdeal)
            {
                correct++;
            }
        }

        System.out.println("Total cases:" + total);
        System.out.println("Correct cases:" + correct);
        double percent = (double)correct / (double)total;
        System.out.println("Correct percent:" + Format.formatPercentWhole(percent));
        for (int i = 0; i < 7; i++)
        {
            if (this.treeCount[i] > 0)
            {
                double p = ((double)this.treeCorrect[i] / (double)this.treeCount[i]);
                System.out.println("Tree Type #"
                        + i
                        + " - Correct/total: "
                        + this.treeCorrect[i]
                        + "/" + treeCount[i] + "(" + Format.formatPercentWhole(p) + ")");
            }
        }
    }

}
