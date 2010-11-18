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
package org.encog.examples.neural.gui.mpg;

import java.io.File;
import java.util.List;

import org.encog.neural.data.NeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.normalize.DataNormalization;
import org.encog.normalize.output.OutputField;
import org.encog.normalize.output.OutputFieldRangeMapped;
import org.encog.persist.EncogPersistedCollection;

public class CalculateMPG {

	private EncogPersistedCollection encog;
	private DataNormalization norm;
	private BasicNetwork network;
	
	public CalculateMPG(File encogFile)
	{
		this.encog = new EncogPersistedCollection(encogFile);
		this.norm = (DataNormalization) encog.find("norm");
		this.network = (BasicNetwork)encog.find("network");
	}
	
	public double calulate(
			double cylinders,
			double displacement,
			double horsePower,
			double weight,
			double acceleration)
	{
		double[] data = new double[5];
		data[0] = cylinders;
		data[1] = displacement;
		data[2] = horsePower;
		data[3] = weight;
		data[4] = acceleration;
		
		NeuralData input = norm.buildForNetworkInput(data);
		NeuralData output = network.compute(input);
		
		OutputFieldRangeMapped mpgField = (OutputFieldRangeMapped)((List<OutputField>)norm.getOutputFields()).get(5);
		
		return mpgField.convertBack(output.getData(0));
	}
}
