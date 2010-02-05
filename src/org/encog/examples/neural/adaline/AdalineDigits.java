/*
 * Encog(tm) Artificial Intelligence Framework v2.3
 * Java Examples
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

package org.encog.examples.neural.adaline;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.simple.TrainAdaline;
import org.encog.neural.pattern.ADALINEPattern;

public class AdalineDigits {

	public final static int CHAR_WIDTH = 5;
	public final static int CHAR_HEIGHT = 7;
	
	public static String[][] DIGITS = { 
      { " OOO ",
        "O   O",
        "O   O",
        "O   O",
        "O   O",
        "O   O",
        " OOO "  },

      { "  O  ",
        " OO  ",
        "O O  ",
        "  O  ",
        "  O  ",
        "  O  ",
        "  O  "  },

      { " OOO ",
        "O   O",
        "    O",
        "   O ",
        "  O  ",
        " O   ",
        "OOOOO"  },

      { " OOO ",
        "O   O",
        "    O",
        " OOO ",
        "    O",
        "O   O",
        " OOO "  },

      { "   O ",
        "  OO ",
        " O O ",
        "O  O ",
        "OOOOO",
        "   O ",
        "   O "  },

      { "OOOOO",
        "O    ",
        "O    ",
        "OOOO ",
        "    O",
        "O   O",
        " OOO "  },

      { " OOO ",
        "O   O",
        "O    ",
        "OOOO ",
        "O   O",
        "O   O",
        " OOO "  },

      { "OOOOO",
        "    O",
        "    O",
        "   O ",
        "  O  ",
        " O   ",
        "O    "  },

      { " OOO ",
        "O   O",
        "O   O",
        " OOO ",
        "O   O",
        "O   O",
        " OOO "  },

      { " OOO ",
        "O   O",
        "O   O",
        " OOOO",
        "    O",
        "O   O",
        " OOO "  } };
	
	public static NeuralDataSet generateTraining()
	{
		NeuralDataSet result = new BasicNeuralDataSet();
		for(int i=0;i<DIGITS.length;i++)
		{			
			BasicNeuralData ideal = new BasicNeuralData(DIGITS.length);
			
			// setup input
			NeuralData input = image2data(DIGITS[i]);
			
			// setup ideal
			for(int j=0;j<DIGITS.length;j++)
			{
				if( j==i )
					ideal.setData(j,1);
				else
					ideal.setData(j,-1);
			}
			
			// add training element
			result.add(input,ideal);
		}
		return result;
	}
	
	public static NeuralData image2data(String[] image)
	{
		NeuralData result = new BasicNeuralData(CHAR_WIDTH*CHAR_HEIGHT);
		
		for(int row = 0; row<CHAR_HEIGHT; row++)
		{
			for(int col = 0; col<CHAR_WIDTH; col++)
			{
				int index = (row*CHAR_WIDTH) + col;
				char ch = image[row].charAt(col);
				result.setData(index,ch=='O'?1:-1 );
			}
		}
		
		return result;
	}

	public static void main(String args[])
	{
		int inputNeurons = CHAR_WIDTH * CHAR_HEIGHT;
		int outputNeurons = DIGITS.length;
		
		ADALINEPattern pattern = new ADALINEPattern();
		pattern.setInputNeurons(inputNeurons);
		pattern.setOutputNeurons(outputNeurons);
		BasicNetwork network = pattern.generate();
		
		// train it
		NeuralDataSet training = generateTraining();
		Train train = new TrainAdaline(network,training,0.01);
		
		int epoch = 1;
		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01);
		
		//
		System.out.println("Error:" + network.calculateError(training));
		
		// test it
		for(int i=0;i<DIGITS.length;i++)
		{
			int output = network.winner(image2data(DIGITS[i]));
			
			for(int j=0;j<CHAR_HEIGHT;j++)
			{
				if( j==CHAR_HEIGHT-1 )
					System.out.println(DIGITS[i][j]+" -> "+output);
				else
					System.out.println(DIGITS[i][j]);
				
			}
			
			System.out.println();
		}
	}	
}
