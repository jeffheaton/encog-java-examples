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
package org.encog.examples.neural.adaline;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.simple.TrainAdaline;
import org.encog.neural.pattern.ADALINEPattern;

/**
 * This example demonstrates the ADALINE neural network.  The ADALINE network 
 * is a very simple network that is often used for pattern recognition.  The 
 * input pattern must match EXACTLY with what the network was trained with.
 * 
 * This example teaches the ADALINE to recognize the 10 digits.
 * 
 * This is based on a an example by Karsten Kutza, 
 * written in C on 1996-01-24.
 * http://www.neural-networks-at-your-fingertips.com
 */
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
	
	public static MLDataSet generateTraining()
	{
		MLDataSet result = new BasicMLDataSet();
		for(int i=0;i<DIGITS.length;i++)
		{			
			BasicMLData ideal = new BasicMLData(DIGITS.length);
			
			// setup input
			MLData input = image2data(DIGITS[i]);
			
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
	
	public static MLData image2data(String[] image)
	{
		MLData result = new BasicMLData(CHAR_WIDTH*CHAR_HEIGHT);
		
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
		BasicNetwork network = (BasicNetwork)pattern.generate();
		
		// train it
		MLDataSet training = generateTraining();
		MLTrain train = new TrainAdaline(network,training,0.01);
		
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
		
		Encog.getInstance().shutdown();
	}	
}
