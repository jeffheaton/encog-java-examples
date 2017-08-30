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
package org.encog.examples.neural.neat.boxes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import org.encog.mathutil.IntPair;
import org.encog.neural.hyperneat.HyperNEATCODEC;
import org.encog.neural.hyperneat.substrate.Substrate;
import org.encog.neural.hyperneat.substrate.SubstrateFactory;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.training.NEATGenome;

public class DisplayBoxesPanel extends JPanel {
	
	/**
	 * The serial.
	 */
	private static final long serialVersionUID = 1L;
	private BoxTrialCase testCase = new BoxTrialCase(new Random());
	private NEATPopulation pop;
	private int resolution = BoxTrialCase.BASE_RESOLUTION;
	
	public DisplayBoxesPanel(NEATPopulation thePopulation) {
		testCase.initTestCase(0);
		this.pop = thePopulation;
	}

	@Override
	public void paint(Graphics g) {
		
		NEATGenome genome = (NEATGenome) this.pop.getBestGenome();
		Substrate substrate = SubstrateFactory.factorSandwichSubstrate(resolution, resolution);
		HyperNEATCODEC codec = new HyperNEATCODEC();
		NEATNetwork phenotype = (NEATNetwork) codec.decode(this.pop, substrate, genome);		
				
		TrialEvaluation trial = new TrialEvaluation(phenotype, this.testCase);
		IntPair actualPos = trial.query(resolution);
		
		// clear what was there before
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//
		int boxWidth = this.getWidth()/resolution;
		int boxHeight = this.getHeight()/resolution;
		double delta = 2.0 / resolution;
		int index = 0;

		for(int row = 0; row < resolution; row++ ) {
			double y = -1 + (row*delta);
			int boxY = row * boxHeight;
			for(int col = 0; col< resolution; col++ ) {
				double x = -1 + (col*delta);
				int boxX = col*boxWidth;
				
				if( this.testCase.getPixel(x, y)>0 ) {
					g.setColor(Color.blue);
					g.fillRect(boxX, boxY, boxWidth, boxHeight);
				} else {
					double d = trial.getOutput().getData(index);
					int c = trial.normalize(d,255);
					g.setColor(new Color(255,c,255));
					g.fillRect(boxX, boxY, boxWidth, boxHeight);
					g.setColor(Color.black);
					g.drawRect(boxX, boxY, boxWidth, boxHeight);
					g.drawRect(boxX+1, boxY+1, boxWidth-2, boxHeight-2);
				}
				index++;
			}
		}
		
		g.setColor(Color.red);
		g.fillRect(actualPos.getX()*boxWidth, actualPos.getY()*boxHeight, boxWidth, boxHeight);
	}
	
	public void createNewCase(int theResolution) {
		Random r = new Random();
		this.resolution = theResolution;
		this.testCase.initTestCase(r.nextInt(3));
		this.repaint();
	}	
}
