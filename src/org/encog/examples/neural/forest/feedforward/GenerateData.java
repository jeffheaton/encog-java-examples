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

import org.encog.app.csv.balance.BalanceCSV;
import org.encog.app.csv.filter.FilterCSV;
import org.encog.app.csv.normalize.NormalizationAction;
import org.encog.app.csv.normalize.NormalizeCSV;
import org.encog.app.csv.segregate.SegregateCSV;
import org.encog.app.csv.segregate.SegregateTargetPercent;
import org.encog.app.csv.shuffle.ShuffleCSV;
import org.encog.util.Format;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;

public class GenerateData  {
	
    public void step1()
    {
        System.out.println("Step 1: Filter to single wilderness area");
        FilterCSV filter = new FilterCSV();
        filter.analyze(Constant.COVER_TYPE_FILE, false, CSVFormat.ENGLISH);
        //filter.Exclude(10, "1");
        filter.exclude(11, "1");
        filter.exclude(12, "1");
        filter.exclude(13, "1");
        filter.process(Constant.FILTERED_FILE);
        System.out.println("Origional row count: " + Format.formatInteger(filter.getRecordCount()) + ", Filtered row count:" + Format.formatInteger(filter.getFilteredRowCount()));
    }

    public void step2()
    {
    	System.out.println("Step 2: Shuffle source file");
        ShuffleCSV shuffle = new ShuffleCSV();
        shuffle.analyze(Constant.FILTERED_FILE, false, CSVFormat.ENGLISH);
        shuffle.process(Constant.RANDOM_FILE);
    }

    public void step3()
    {
        SegregateCSV seg = new SegregateCSV();
        seg.getTargets().add(new SegregateTargetPercent(Constant.TRAINING_FILE, 75));
        seg.getTargets().add(new SegregateTargetPercent(Constant.EVALUATE_FILE, 25));
        System.out.println("Step 3: Generate training and evaluation files");
        seg.analyze(Constant.RANDOM_FILE, false, CSVFormat.ENGLISH);            
        seg.process();            
    }

    public void step4()
    {
    	System.out.println("Step 4: Balance training to have the same number of each tree");
        BalanceCSV balance = new BalanceCSV();
        balance.analyze(Constant.TRAINING_FILE, false, CSVFormat.ENGLISH);
        balance.process(Constant.BALANCE_FILE, 54, 500);
        System.out.println("Count per Tree:");
        System.out.println(balance.dumpCounts());
    }

    public int step5(NormalizationAction normType)
    {
    	System.out.println("Step 5: Normalize and classify training data");
        NormalizeCSV norm = new NormalizeCSV();
        norm.analyze(Constant.BALANCE_FILE, false, CSVFormat.ENGLISH);

        int index = 0;
        norm.getStats().getStats()[index++].setName("elevation");
        norm.getStats().getStats()[index++].setName("aspect");
        norm.getStats().getStats()[index++].setName("slope");
        norm.getStats().getStats()[index++].setName("hydro_h");
        norm.getStats().getStats()[index++].setName("hydro_v");
        norm.getStats().getStats()[index++].setName("roadway");
        norm.getStats().getStats()[index++].setName("shade_9");
        norm.getStats().getStats()[index++].setName("shade_12");
        norm.getStats().getStats()[index++].setName("shade_3");
        norm.getStats().getStats()[index++].setName("fire");

        norm.getStats().getStats()[index].setName("area1");
        norm.getStats().getStats()[index++].setAction( NormalizationAction.Ignore);
        norm.getStats().getStats()[index].setName("area2");
        norm.getStats().getStats()[index++].setAction( NormalizationAction.Ignore);
        norm.getStats().getStats()[index].setName("area3");
        norm.getStats().getStats()[index++].setAction( NormalizationAction.Ignore);
        norm.getStats().getStats()[index].setName("area4");
        norm.getStats().getStats()[index++].setAction( NormalizationAction.Ignore);

        for (int i = 0; i < 40; i++)
        {
        	norm.getStats().getStats()[index].setName("soil" + (i + 1));
            norm.getStats().getStats()[index].setActualHigh(1);
            norm.getStats().getStats()[index++].setActualLow(0);
        }
        
        norm.getStats().getStats()[index].makeClass(normType, 1, 7, 1, -1);

        norm.normalize(Constant.NORMALIZED_FILE);
        norm.writeStatsFile(Constant.NORMALIZED_STATS_FILE);
        return norm.getStats().getStats()[index].getColumnsNeeded();
    }

    public void step6(int outputCount)
    {
    	System.out.println("Step 7: Converting training file to binary");
        int inputCount = Constant.INPUT_COUNT;
        EncogUtility.convertCSV2Binary(Constant.NORMALIZED_FILE, Constant.BINARY_FILE, inputCount, outputCount, true);

    }
}
