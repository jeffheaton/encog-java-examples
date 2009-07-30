package org.encog.examples.neural.hopfield;

import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.logic.HopfieldLogic;
import org.encog.neural.pattern.HopfieldPattern;

/**
 * Simple class to recognize some patterns with a Hopfield Neural Network.
 * This is very loosely based on a an example by Karsten Kutza, 
 * written in C on 1996-01-30.
 * http://www.neural-networks-at-your-fingertips.com/hopfield.html
 * 
 * I translated it to Java and adapted it to use Encog for neural
 * network processing.  I mainly kept the patterns from the 
 * original example.
 *
 */
public class HopfieldAssociate {

	final static int HEIGHT = 10;
	final static int WIDTH = 10;
	
	/**
	 * The neural network will learn these patterns.
	 */
	public static final String[][] PATTERN  = { { 
		"O O O O O ",
        " O O O O O",
        "O O O O O ",
        " O O O O O",
        "O O O O O ",
        " O O O O O",
        "O O O O O ",
        " O O O O O",
        "O O O O O ",
        " O O O O O"  },

      { "OO  OO  OO",
        "OO  OO  OO",
        "  OO  OO  ",
        "  OO  OO  ",
        "OO  OO  OO",
        "OO  OO  OO",
        "  OO  OO  ",
        "  OO  OO  ",
        "OO  OO  OO",
        "OO  OO  OO"  },

      { "OOOOO     ",
        "OOOOO     ",
        "OOOOO     ",
        "OOOOO     ",
        "OOOOO     ",
        "     OOOOO",
        "     OOOOO",
        "     OOOOO",
        "     OOOOO",
        "     OOOOO"  },

      { "O  O  O  O",
        " O  O  O  ",
        "  O  O  O ",
        "O  O  O  O",
        " O  O  O  ",
        "  O  O  O ",
        "O  O  O  O",
        " O  O  O  ",
        "  O  O  O ",
        "O  O  O  O"  },

      { "OOOOOOOOOO",
        "O        O",
        "O OOOOOO O",
        "O O    O O",
        "O O OO O O",
        "O O OO O O",
        "O O    O O",
        "O OOOOOO O",
        "O        O",
        "OOOOOOOOOO"  } };

	/**
	 * The neural network will be tested on these patterns, to see
	 * which of the last set they are the closest to.
	 */
	public static final String[][] PATTERN2 = { { 
		"          ",
        "          ",
        "          ",
        "          ",
        "          ",
        " O O O O O",
        "O O O O O ",
        " O O O O O",
        "O O O O O ",
        " O O O O O"  },

      { "OOO O    O",
        " O  OOO OO",
        "  O O OO O",
        " OOO   O  ",
        "OO  O  OOO",
        " O OOO   O",
        "O OO  O  O",
        "   O OOO  ",
        "OO OOO  O ",
        " O  O  OOO"  },

      { "OOOOO     ",
        "O   O OOO ",
        "O   O OOO ",
        "O   O OOO ",
        "OOOOO     ",
        "     OOOOO",
        " OOO O   O",
        " OOO O   O",
        " OOO O   O",
        "     OOOOO"  },

      { "O  OOOO  O",
        "OO  OOOO  ",
        "OOO  OOOO ",
        "OOOO  OOOO",
        " OOOO  OOO",
        "  OOOO  OO",
        "O  OOOO  O",
        "OO  OOOO  ",
        "OOO  OOOO ",
        "OOOO  OOOO"  },

      { "OOOOOOOOOO",
        "O        O",
        "O        O",
        "O        O",
        "O   OO   O",
        "O   OO   O",
        "O        O",
        "O        O",
        "O        O",
        "OOOOOOOOOO"  } };

	public BiPolarNeuralData convertPattern(String[][] data, int index)
	{
		int resultIndex = 0;
		BiPolarNeuralData result = new BiPolarNeuralData(WIDTH*HEIGHT);
		for(int row=0;row<HEIGHT;row++)
		{
			for(int col=0;col<WIDTH;col++)
			{
				char ch = data[index][row].charAt(col);
				result.setData(resultIndex++, ch=='O');
			}
		}
		return result;
	}
	
	public void display(BiPolarNeuralData pattern1,BiPolarNeuralData pattern2)
	{
		int index1 = 0;
		int index2 = 0;
		
		for(int row = 0;row<HEIGHT;row++)
		{
			StringBuilder line = new StringBuilder();
			
			for(int col = 0;col<WIDTH;col++)
			{
				if(pattern1.getBoolean(index1++))
					line.append('O');
				else
					line.append(' ');
			}
			
			line.append("   ->   ");
			
			for(int col = 0;col<WIDTH;col++)
			{
				if(pattern2.getBoolean(index2++))
					line.append('O');
				else
					line.append(' ');
			}
			
			
			
			System.out.println(line.toString());
		}
	}

	
	public void evaluate(BasicNetwork hopfield, String[][] pattern)
	{
		HopfieldLogic hopfieldLogic = (HopfieldLogic)hopfield.getLogic();
		for(int i=0;i<pattern.length;i++)
		{
			BiPolarNeuralData pattern1 = convertPattern(pattern,i);
			hopfieldLogic.setCurrentState(pattern1);
			int cycles = hopfieldLogic.runUntilStable(100);
			BiPolarNeuralData pattern2 = (BiPolarNeuralData)hopfieldLogic.getCurrentState();
			System.out.println("Cycles until stable(max 100): " + cycles + ", result=");
			display( pattern1, pattern2);
			System.out.println("----------------------");
		}
	}
	
	public void run()
	{
		HopfieldPattern pattern = new HopfieldPattern();
		pattern.setInputNeurons(WIDTH*HEIGHT);
		BasicNetwork hopfield = pattern.generate();
		HopfieldLogic hopfieldLogic = (HopfieldLogic)hopfield.getLogic();

		for(int i=0;i<PATTERN.length;i++)
		{
			hopfieldLogic.addPattern(convertPattern(PATTERN,i));
		}
		
		evaluate(hopfield,PATTERN);
		evaluate(hopfield,PATTERN2);
	}
	
	public static void main(String[] args)
	{
		HopfieldAssociate program = new HopfieldAssociate();
		program.run();
	}
	
}
