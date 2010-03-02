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

package org.encog.examples.neural.forest.som;

import java.io.File;

import org.encog.neural.data.NeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.normalize.DataNormalization;
import org.encog.normalize.output.nominal.OutputEquilateral;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.csv.ReadCSV;

public class Evaluate {
	private int[] treeCount = new int[10];
	private int[] treeCorrect = new int[10];
	
	public void keepScore(int actual,int ideal)
	{
		treeCount[ideal]++;
		if(actual==ideal)
			treeCorrect[ideal]++;	
	}
	
	public BasicNetwork loadNetwork()
	{
		File file = Constant.TRAINED_NETWORK_FILE;
		
		if( !file.exists() )
		{
			System.out.println("Can't read file: " + file.getAbsolutePath() );
			return null;
		}
		
		EncogPersistedCollection encog = new EncogPersistedCollection(file);					
		BasicNetwork network = (BasicNetwork) encog.find(Constant.TRAINED_NETWORK_NAME);
	
		if( network==null )
		{
			System.out.println("Can't find network resource: " + Constant.TRAINED_NETWORK_NAME );
			return null;
		}
				
		return network;
	}
	

	
	public DataNormalization loadNormalization()
	{
		File file = Constant.TRAINED_NETWORK_FILE;
		
		EncogPersistedCollection encog = new EncogPersistedCollection(file);
		
		DataNormalization norm = (DataNormalization) encog.find(Constant.NORMALIZATION_NAME);
		if( norm==null )
		{
			System.out.println("Can't find normalization resource: " + Constant.NORMALIZATION_NAME );
			return null;
		}
				
		return norm;
	}
	
	public int determineTreeType(OutputEquilateral eqField, NeuralData output)
	{
		int result = 0;
		
		if( eqField!=null )
		{
			result = eqField.getEquilateral().decode(output.getData());			
		}
		else
		{
			double maxOutput = Double.NEGATIVE_INFINITY;
			result = -1;
			
			for(int i=0;i<output.size();i++)
			{
				if( output.getData(i)>maxOutput )
				{
					maxOutput = output.getData(i);
					result = i;
				}
			}
		}
			
		return result;
	}
	
	
	public void evaluate()
	{
		BasicNetwork network = loadNetwork();
		DataNormalization norm = loadNormalization();
		
		ReadCSV csv = new ReadCSV(Constant.EVALUATE_FILE.toString(),false,',');
		int[][][] cube = new int[Constant.OUTPUT_COUNT][Constant.OUTPUT_COUNT][7];
		double[] input = new double[norm.getInputFields().size()];
		
		int total = 0;
		while(csv.next())
		{
			total++;
			for(int i=0;i<input.length;i++)
			{
				input[i] = csv.getDouble(i);
			}
			NeuralData inputData = norm.buildForNetworkInput(input);
			int output = network.winner(inputData);
			int outputY = output/10;
			int outputX = output-(outputY*10);
			
			int coverTypeIdeal = (int)csv.getDouble(54)-1;
			cube[outputX][outputY][coverTypeIdeal]++;
		}
		
		for(int x=0;x<10;x++)
		{
			StringBuilder str = new StringBuilder();
			for(int y=0;y<10;y++)
			{
				int max = Integer.MIN_VALUE;
				int maxIndex = -1;
				for(int z=0;z<7;z++)
				{
					int value = cube[x][y][z];
					if( value>max && value>0 )
					{
						max = value;
						maxIndex = z;
					}
				}
				if( maxIndex==-1)
					str.append('-');
				else
					str.append(maxIndex);
				
			}
			System.out.println(str.toString());
		}
	}
}
