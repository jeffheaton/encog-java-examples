package org.encog.examples.neural.art.art1;

import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.util.network.ART1Holder;

public class NeuralART1 {
	
	public static final int INPUT_NEURONS = 5;
	public static final int OUTPUT_NEURONS = 10;

	public static final String[] PATTERN = { 
			"   O ",
            "  O O",
            "    O",
            "  O O",
            "    O",
            "  O O",
            "    O",
            " OO O",
            " OO  ",
            " OO O",
            " OO  ",
            "OOO  ",
            "OO   ",
            "O    ",
            "OO   ",
            "OOO  ",
            "OOOO ",
            "OOOOO",
            "O    ",
            " O   ",
            "  O  ",
            "   O ",
            "    O",
            "  O O",
            " OO O",
            " OO  ",
            "OOO  ",
            "OO   ",
            "OOOO ",
            "OOOOO"  };
	
	private boolean[][] input;

	public void setupInput()
	{
		this.input = new boolean[PATTERN.length][INPUT_NEURONS];
		for (int n=0; n<PATTERN.length; n++) {
		    for (int i=0; i<INPUT_NEURONS; i++) {
		      this.input[n][i] = (PATTERN[n].charAt(i) == 'O');
		    }
		  }
	}
	
	
	public void run()
	{
		this.setupInput();
		ART1Holder network = new ART1Holder(INPUT_NEURONS,OUTPUT_NEURONS);
		for(int i=0;i<PATTERN.length;i++)
		{
			BiPolarNeuralData in = new BiPolarNeuralData(this.input[i]);
			BiPolarNeuralData out = new BiPolarNeuralData(OUTPUT_NEURONS);
			network.compute(in, out);
			if( network.hasWinner() )
			{
				System.out.println(PATTERN[i] + " - " + network.getWinner() );
			}
			else
			{
				System.out.println(PATTERN[i] + " - new Input and all Classes exhausted" );
			}
		}
	}
	
	public static void main(String[] args)
	{
		NeuralART1 art = new NeuralART1();
		art.run();
	}
}
