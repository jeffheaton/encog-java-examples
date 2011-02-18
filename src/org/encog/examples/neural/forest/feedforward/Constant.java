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


public class Constant {
	
    /**
     * The base directory that all of the data for this example is stored in.
     */
    public final static String BASE_DIRECTORY = "c:\\data\\";

    /**
     * The source data file from which all others are built.  This file can
     * be downloaded from:
     * 
     * http://kdd.ics.uci.edu/databases/covertype/covertype.html
     */
    public final static String COVER_TYPE_FILE = BASE_DIRECTORY + "covtype.data";

    /**
     * The source file in random order.
     */
    public final static String FILTERED_FILE = BASE_DIRECTORY + "filtered.csv";

    /**
     * The source file in random order.
     */
    public final static String RANDOM_FILE = BASE_DIRECTORY + "random.csv";

    /// <summary>
    /// 75% of the data will be moved into this file to be used as training data.  The 
    /// data is still in "raw form" in this file.
    /// </summary>
    public final static String TRAINING_FILE = BASE_DIRECTORY + "training.csv";

    /// <summary>
    /// 25% of the data will be moved into this file to be used as evaluation data.  The 
    /// data is still in "raw form" in this file.
    /// </summary>
    public final static String EVALUATE_FILE = BASE_DIRECTORY + "evaluate.csv";

    /// <summary>
    /// We will limit the number of samples per "tree type" to 3000, this causes the data
    /// to be more balanced and will not allow one tree type to over-fit the network.
    /// The training file is narrowed and placed into this file in "raw form".
    /// </summary>
    public final static String BALANCE_FILE = BASE_DIRECTORY + "balance.csv";

    /**
     * The training file is normalized and placed into this file.
     */
    public final static String NORMALIZED_FILE = BASE_DIRECTORY + "normalized.csv";

    /**
     * The training file is normalized and placed into this file.
     */
    public final static String NORMALIZED_STATS_FILE = BASE_DIRECTORY + "normalized-stats.csv";

    /**
     * CSV files are slow to parse with because the text must be converted into numbers.
     * The balanced file will be converted to a binary file to be used for training.
     */
    public final static String BINARY_FILE = BASE_DIRECTORY + "normalized.egb";

    /**
     * The trained network and normalizer will be saved into an Encog EG file.
     */
    public final static String TRAINED_NETWORK_FILE = BASE_DIRECTORY + "forest.eg";

    /**
     * The name of the network inside of the EG file.
     */
    public final static String TRAINED_NETWORK_NAME = "forest-network";

    /**
     * How many minutes to train for (console mode only)
     */
    public final static int TRAINING_MINUTES = 10;

    /**
     * How many input neurons to use.
     */
    public final static int INPUT_COUNT = 50;

    /**
     * How many hidden neurons to use.
     */
    public final static int HIDDEN_COUNT = 100;

}
