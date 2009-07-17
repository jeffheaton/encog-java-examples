package org.encog.examples.neural.adaline;

import org.encog.neural.activation.ActivationBiPolar;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.Layer;
import org.encog.util.randomize.RangeRandomizer;

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
			BasicNeuralData input = new BasicNeuralData(CHAR_WIDTH*CHAR_HEIGHT);
			BasicNeuralData ideal = new BasicNeuralData(DIGITS.length);
			
			// setup input
			for(int row = 0; row<CHAR_HEIGHT; row++)
			{
				for(int col = 0; col<CHAR_WIDTH; col++)
				{
					int index = (row*CHAR_WIDTH) + col;
					char ch = DIGITS[i][row].charAt(col);
					input.setData(index,ch=='O'?1:-1 );
				}
			}
			
			// setup ideal
			for(int j=0;j<DIGITS.length;j++)
			{
				if( j==i )
					ideal.setData(i,1);
				else
					ideal.setData(i,-1);
			}
			
			// add training element
			result.add(input,ideal);
		}
		return result;
	}

	public static void main(String args[])
	{
		int inputNeurons = CHAR_WIDTH * CHAR_HEIGHT;
		int outputNeurons = DIGITS.length;
		
		BasicNetwork network = new BasicNetwork();
		
		Layer inputLayer = new BasicLayer(new ActivationBiPolar(), false, inputNeurons );
		Layer outputLayer = new BasicLayer(new ActivationBiPolar(), false, outputNeurons );
		
		network.addLayer(inputLayer);
		network.addLayer(outputLayer);
		
		(new RangeRandomizer(-0.5,0.5)).randomize(network);
		
	}
	
}
