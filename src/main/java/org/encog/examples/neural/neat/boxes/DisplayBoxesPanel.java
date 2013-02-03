package org.encog.examples.neural.neat.boxes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.training.NEATGenome;

public class DisplayBoxesPanel extends JPanel {
	
	private int resolution = 11;
	private BoxTrial testCase = new BoxTrial(new Random());
	private NEATPopulation pop;
	
	public DisplayBoxesPanel(NEATPopulation thePopulation) {
		testCase.initTestCase(0);
		this.pop = thePopulation;
	}

	public void paint(Graphics g) {
		NEATGenome genome = (NEATGenome)this.pop.getGenomes().get(0);
		NEATNetwork phenotype = (NEATNetwork)this.pop.getCODEC().decode(genome);
		
		TrialEvaluation trial = new TrialEvaluation(phenotype, this.testCase);
		
		trial.
		
		//
		int boxWidth = this.getWidth()/resolution;
		int boxHeight = this.getHeight()/resolution;
		double delta = 2.0 / resolution;
		
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
					g.setColor(Color.black);
					
					g.drawRect(boxX, boxY, boxWidth, boxHeight);
					g.drawRect(boxX+1, boxY+1, boxWidth-2, boxHeight-2);
				}
				
			}
		}
	}
	
	
}
