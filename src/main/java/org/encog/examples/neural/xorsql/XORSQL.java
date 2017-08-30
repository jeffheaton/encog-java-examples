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
package org.encog.examples.neural.xorsql;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.platformspecific.j2se.data.SQLNeuralDataSet;

/**
 * XOR SQL: This program uses a SQL data source to train a neural network.  
 * This example is setup to use MySQL, but it could easily be adapted to
 * other databases.  It assumes that a database is already setup that contains
 * XOR training data.  A proper database can be setup with the following SQL:
 * 
 * DROP TABLE IF EXISTS `xordata`;
 * CREATE TABLE `xordata` (
 *  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 *  `input1` double NOT NULL,
 *  `input2` double NOT NULL,
 *  `ideal1` double NOT NULL,
 *  PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
 *
 *
 * INSERT INTO `xordata` VALUES ('1', '0', '0', '0');
 * INSERT INTO `xordata` VALUES ('2', '1', '0', '1');
 * INSERT INTO `xordata` VALUES ('3', '0', '1', '1');
 * INSERT INTO `xordata` VALUES ('4', '1', '1', '0');
 *  
 * @author $Author$
 * @version $Revision$
 */
public class XORSQL {	
	public final static String SQL = "SELECT INPUT1,INPUT2,IDEAL1 FROM XORDATA ORDER BY ID";
	public final static int INPUT_SIZE = 2;
	public final static int IDEAL_SIZE = 1;
	public final static String SQL_DRIVER = "com.mysql.jdbc.Driver";
	public final static String SQL_URL = "jdbc:mysql://localhost/xor";
	public final static String SQL_UID = "xoruser";
	public final static String SQL_PWD = "xorpassword";
	
	public static void main(final String args[]) {
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(1));
		network.getStructure().finalizeStructure();
		network.reset();

		MLDataSet trainingSet = new SQLNeuralDataSet(
				XORSQL.SQL,
				XORSQL.INPUT_SIZE,
				XORSQL.IDEAL_SIZE,
				XORSQL.SQL_DRIVER,
				XORSQL.SQL_URL,
				XORSQL.SQL_UID,
				XORSQL.SQL_PWD);
		
		// train the neural network
		final MLTrain train = new ResilientPropagation(network, trainingSet);
		// reset if improve is less than 1% over 5 cycles
		train.addStrategy(new RequiredImprovementStrategy(5));
		
		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01);

		// test the neural network
		System.out.println("Neural Network Results:");
		for(MLDataPair pair: trainingSet ) {
			final MLData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}
		
		Encog.getInstance().shutdown();
	}
}
