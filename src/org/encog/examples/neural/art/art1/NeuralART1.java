/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010, Heaton Research Inc., and individual contributors.
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
package org.encog.examples.neural.art.art1;

import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.logic.ART1Logic;
import org.encog.neural.pattern.ART1Pattern;

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
		ART1Pattern pattern = new ART1Pattern();
		pattern.setInputNeurons(INPUT_NEURONS);
		pattern.setOutputNeurons(OUTPUT_NEURONS);
		BasicNetwork network = pattern.generate();
		ART1Logic logic = (ART1Logic)network.getLogic();
		
		
		for(int i=0;i<PATTERN.length;i++)
		{
			BiPolarNeuralData in = new BiPolarNeuralData(this.input[i]);
			BiPolarNeuralData out = new BiPolarNeuralData(OUTPUT_NEURONS);
			logic.compute(in, out);
			if( logic.hasWinner() )
			{
				System.out.println(PATTERN[i] + " - " + logic.getWinner() );
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
