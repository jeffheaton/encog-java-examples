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

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Basic config info for the market prediction example.
 * 
 * @author jeff
 * 
 */
public class Config {
	
	public static final String DIRECTORY = "d:\\data\\";
    public static final String STEP1 = Config.DIRECTORY + "step1_yahoo.csv";
    public static final String STEP2 = Config.DIRECTORY + "step2_sort.csv";
    public static final String STEP3 = Config.DIRECTORY + "step3_ind.csv";
    public static final String STEP4 = Config.DIRECTORY + "step4_norm.csv";
    public static final String STEP4STATS = Config.DIRECTORY + "step4_stats.csv";
    public static final String STEP5 = Config.DIRECTORY + "step5_training.csv";
    public static final String FILENAME_PREDICT = Config.DIRECTORY + "step5_predict.csv";
    public static final String FILENAME = Config.DIRECTORY + "marketdata.eg";
    public static final String MARKET_NETWORK = "market-network";
    public static final String MARKET_TRAIN = "market-train";

    public static final int TRAINING_MINUTES = 1;
    public static final int HIDDEN1_COUNT = 20;
    public static final int HIDDEN2_COUNT = 0;

    public static final int TRAIN_BEGIN_YEAR = 2000;
    public static final int TRAIN_BEGIN_MONTH = 1;
    public static final int TRAIN_BEGIN_DAY = 1;

    public static final int TRAIN_END_YEAR = 2008;
    public static final int TRAIN_END_MONTH = 12;
    public static final int TRAIN_END_DAY = 31;

    public static final int INPUT_WINDOW = 10;
    public static final int PREDICT_WINDOW = 1;
    public static final String TICKER = "GE";
}
