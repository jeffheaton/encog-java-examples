package org.encog.examples.neural.adaline;

import org.encog.neural.activation.ActivationBiPolar;
import org.encog.neural.activation.ActivationLinear;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.Layer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.simple.TrainAdaline;
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
		
		BasicNetwork network = new BasicNetwork();
		
		Layer inputLayer = new BasicLayer(new ActivationLinear(), false, inputNeurons );
		Layer outputLayer = new BasicLayer(new ActivationLinear(), true, outputNeurons );
		
		network.addLayer(inputLayer);
		network.addLayer(outputLayer);
		network.getStructure().finalizeStructure();
		
		(new RangeRandomizer(-0.5,0.5)).randomize(network);
		
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
