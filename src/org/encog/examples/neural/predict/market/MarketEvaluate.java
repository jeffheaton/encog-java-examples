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

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.encog.app.quant.loader.MarketLoader;
import org.encog.app.quant.normalize.NormalizeCSV;
import org.encog.app.quant.normalize.NormalizedField;
import org.encog.engine.util.Format;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogMemoryCollection;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.time.NumericDateUtil;

public class MarketEvaluate {

	public enum Direction
    {
        up,
        down
    };

    public static Direction determineDirection(double d)
    {
        if (d < 0)
            return Direction.down;
        else
            return Direction.up;
    }



    public static void run()
    {            
        Date end = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(end);
        gc.add(Calendar.YEAR, -1);
        Date begin = gc.getTime();

        MarketBuildTraining.generate(begin, end, false);

        EncogMemoryCollection encog = new EncogMemoryCollection();
        encog.load(Config.FILENAME);
        BasicNetwork network = (BasicNetwork)encog.find(Config.MARKET_NETWORK);

        NormalizeCSV norm = new NormalizeCSV();
        norm.readStatsFile(Config.STEP4STATS);

        NormalizedField n = norm.getStats().getStats()[1];

        BasicNeuralData input = new BasicNeuralData(Config.INPUT_WINDOW);

        ReadCSV csv = new ReadCSV(Config.FILENAME_PREDICT, true, CSVFormat.ENGLISH);
        while (csv.next())
        {
            StringBuilder line = new StringBuilder();
            int index = 0;
            long d = Long.parseLong( csv.get(index++) );
            Date dt = NumericDateUtil.long2Date(d);
            line.append(dt.toString());

            // prepare input
            for (int i = 0; i < input.size(); i++)
            {
                input.setData(i, csv.getDouble(index++) );
            }

            // query neural network
            NeuralData actualData = network.compute(input);
            double prediction = actualData.getData(0);
            double ideal = csv.getDouble(index++);

            // 
            line.append(" Prediction=");
            line.append(Format.formatDouble(n.deNormalize(prediction),2));
            line.append(", Actual= ");
            line.append(Format.formatDouble(n.deNormalize(ideal),2));

            System.out.println(line.toString());
        }
    }
}
