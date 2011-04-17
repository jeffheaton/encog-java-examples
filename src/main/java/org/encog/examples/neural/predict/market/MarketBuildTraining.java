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
package org.encog.examples.neural.predict.market;

import java.util.Date;
import java.util.GregorianCalendar;

import org.encog.app.csv.basic.FileData;
import org.encog.app.csv.normalize.NormalizationAction;
import org.encog.app.csv.normalize.NormalizeCSV;
import org.encog.app.csv.sort.SortCSV;
import org.encog.app.csv.sort.SortType;
import org.encog.app.csv.sort.SortedField;
import org.encog.app.csv.temporal.TemporalType;
import org.encog.app.csv.temporal.TemporalWindowCSV;
import org.encog.app.quant.indicators.MovingAverage;
import org.encog.app.quant.indicators.ProcessIndicators;
import org.encog.app.quant.loader.yahoo.YahooDownload;
import org.encog.neural.activation.ActivationLinear;
import org.encog.neural.activation.ActivationTANH;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.csv.CSVFormat;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * Build the training data for the prediction and store it in an Encog file for
 * later training.
 * 
 * @author jeff
 * 
 */
public class MarketBuildTraining {

    public static void generate(Date begin, Date end, boolean forTraining)
    {
        System.out.println("Downloading market data");
        Logging.stopConsoleLogging();

        YahooDownload loader = new YahooDownload();
        loader.loadAllData(Config.TICKER,Config.STEP1,CSVFormat.ENGLISH,begin,end);

        if (forTraining)
        {
        	System.out.println("Building training data");
        }
        else
        {
        	System.out.println("Building evaluation data");
        }

        // sort the downloaded market data
        SortCSV sort = new SortCSV();
        sort.getSortOrder().add(new SortedField(0,SortType.SortInteger,true));
        sort.process(Config.STEP1, Config.STEP2, true, CSVFormat.ENGLISH);

        // calculate moving average
        ProcessIndicators indicators = new ProcessIndicators();
        indicators.analyze(Config.STEP2, true, CSVFormat.ENGLISH);
        indicators.getColumns().get(0).setOutput( true );
        indicators.getColumns().get(1).setOutput( false );
        indicators.getColumns().get(2).setOutput( false );
        indicators.getColumns().get(3).setOutput( false );
        indicators.getColumns().get(4).setOutput( false );
        indicators.getColumns().get(5).setOutput( false );
        indicators.getColumns().get(6).setOutput( false );
        indicators.getColumns().get(7).setOutput( false );
        indicators.renameColumn(5, FileData.CLOSE);
        indicators.addColumn(new MovingAverage(30, true));
        indicators.process(Config.STEP3);

        // normalize
        if (forTraining)
        {
            NormalizeCSV normalize = new NormalizeCSV();
            normalize.analyze(Config.STEP3, true, CSVFormat.ENGLISH);
            normalize.getStats().getStats()[0].setAction( NormalizationAction.PassThrough );
            normalize.normalize(Config.STEP4);
            normalize.writeStatsFile(Config.STEP4STATS);
        }
        else
        {
            NormalizeCSV normalize = new NormalizeCSV();
            normalize.setSourceFile(Config.STEP3, true, CSVFormat.ENGLISH);
            normalize.readStatsFile(Config.STEP4STATS);
            normalize.normalize(Config.STEP4);
        }

        // build temporal training data
        TemporalWindowCSV window = new TemporalWindowCSV();
        window.analyze(Config.STEP4, true, CSVFormat.ENGLISH);
        window.setInputWindow( Config.INPUT_WINDOW);
        window.setPredictWindow( Config.PREDICT_WINDOW);
        window.getFields()[0].setAction( forTraining ? TemporalType.Ignore : TemporalType.PassThrough );
        window.getFields()[1].setAction( TemporalType.InputAndPredict );

        if (forTraining)
        {
            System.out.println("Generating training data");
            window.process(Config.STEP5);
        }
        else
        {
        	System.out.println("Generating prediction data");
            window.process(Config.FILENAME_PREDICT);
        }

        System.out.println("Done processing data.");

    }

    public static void run()
    {
        Date begin = new GregorianCalendar(
            Config.TRAIN_BEGIN_YEAR,
            Config.TRAIN_BEGIN_MONTH-1,
            Config.TRAIN_BEGIN_DAY).getTime();

        Date end = new GregorianCalendar(
            Config.TRAIN_END_YEAR,
            Config.TRAIN_END_MONTH-1,
            Config.TRAIN_END_DAY).getTime();

        generate(begin, end, true);

        NeuralDataSet training = (BasicNeuralDataSet)EncogUtility.loadCSV2Memory(Config.STEP5.toString(), Config.INPUT_WINDOW, Config.PREDICT_WINDOW, true, CSVFormat.ENGLISH);

        // build a neural network
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(new ActivationTANH(), true, Config.INPUT_WINDOW));
        network.addLayer(new BasicLayer(new ActivationTANH(), true, Config.HIDDEN1_COUNT));
        network.addLayer(new BasicLayer(new ActivationLinear(), true, Config.PREDICT_WINDOW));
        network.getStructure().finalizeStructure();
        network.reset();
        
        EncogDirectoryPersistence.saveObject(Config.FILENAME, network);

    }

}
