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
package org.encog.examples.neural.art.art1;

import org.encog.Encog;
import org.encog.ml.data.specific.BiPolarNeuralData;
import org.encog.neural.art.ART1;

/**
 * This example presents a series of 5-value images to an ART1 network.  
 * ART1 learns new patterns as it goes, and classifies them into groups.
 * 
 * This is based on a an example by Karsten Kutza, 
 * written in C on 1996-01-24.
 * http://www.neural-networks-at-your-fingertips.com
 */
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

	public void setupInput() {
		this.input = new boolean[PATTERN.length][INPUT_NEURONS];
		for (int n = 0; n < PATTERN.length; n++) {
			for (int i = 0; i < INPUT_NEURONS; i++) {
				this.input[n][i] = (PATTERN[n].charAt(i) == 'O');
			}
		}
	}

	public void run() {
		this.setupInput();
		ART1 logic = new ART1(INPUT_NEURONS,OUTPUT_NEURONS);

		for (int i = 0; i < PATTERN.length; i++) {
			BiPolarNeuralData in = new BiPolarNeuralData(this.input[i]);
			BiPolarNeuralData out = new BiPolarNeuralData(OUTPUT_NEURONS);
			logic.compute(in, out);
			if (logic.hasWinner()) {
				System.out.println(PATTERN[i] + " - " + logic.getWinner());
			} else {
				System.out.println(PATTERN[i]
						+ " - new Input and all Classes exhausted");
			}
		}
	}

	public static void main(String[] args) {
		NeuralART1 art = new NeuralART1();
		art.run();
		Encog.getInstance().shutdown();
	}
}
