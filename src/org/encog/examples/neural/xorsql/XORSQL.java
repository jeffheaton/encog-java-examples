/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.encog.examples.neural.xorsql;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.sql.SQLNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.logging.Logging;

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
		
		Logging.stopConsoleLogging();
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(2));
		network.addLayer(new BasicLayer(1));
		network.getStructure().finalizeStructure();
		network.reset();

		NeuralDataSet trainingSet = new SQLNeuralDataSet(
				XORSQL.SQL,
				XORSQL.INPUT_SIZE,
				XORSQL.IDEAL_SIZE,
				XORSQL.SQL_DRIVER,
				XORSQL.SQL_URL,
				XORSQL.SQL_UID,
				XORSQL.SQL_PWD);
		
		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);

		
		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01);

		// test the neural network
		System.out.println("Neural Network Results:");
		for(NeuralDataPair pair: trainingSet ) {
			final NeuralData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}
	}
}
