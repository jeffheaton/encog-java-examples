/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
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
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
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
