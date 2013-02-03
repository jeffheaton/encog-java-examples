package org.encog.examples.neural.neat.boxes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.encog.neural.neat.NEATPopulation;

public class DisplayBoxes extends JFrame implements ActionListener {
	
	private DisplayBoxesPanel display;
	private JButton newCase;
	
	public DisplayBoxes(NEATPopulation thePopulation) {
		setSize(400,400);
		setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(newCase=new JButton("New Case"));
		this.add(buttonPanel,BorderLayout.NORTH);
		this.display = new DisplayBoxesPanel(thePopulation);
		this.add(this.display, BorderLayout.CENTER);
		
		this.newCase.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if( evt.getSource()==this.newCase ) {
			this.display.createNewCase();
		}
	}
	
	
}
