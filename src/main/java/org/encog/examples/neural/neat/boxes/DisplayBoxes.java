package org.encog.examples.neural.neat.boxes;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.encog.neural.neat.NEATPopulation;

public class DisplayBoxes extends JFrame {
	
	private DisplayBoxesPanel display;
	
	public DisplayBoxes(NEATPopulation thePopulation) {
		setSize(400,400);
		setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		this.add(buttonPanel,BorderLayout.NORTH);
		this.display = new DisplayBoxesPanel(thePopulation);
		this.add(this.display, BorderLayout.CENTER);
	}
	
	
}
