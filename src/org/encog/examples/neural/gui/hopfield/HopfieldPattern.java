package org.encog.examples.neural.gui.hopfield;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.encog.engine.util.EngineArray;
import org.encog.examples.neural.gui.predict.GraphPanel;
import org.encog.examples.neural.gui.predict.PredictSIN;
import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.thermal.HopfieldNetwork;
import org.encog.util.simple.EncogUtility;

public class HopfieldPattern extends JFrame  implements ActionListener {

	private HopfieldPanel panel;
	
	public HopfieldPattern()
	{
		this.setTitle("Hopfield Pattern");
		this.setSize(640, 480);
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		
		this.buttonTrain = new JButton("Train");
		this.buttonGo = new JButton("Go");
		this.buttonClear = new JButton("Clear");
		this.buttonClearMatrix = new JButton("Clear Matrix");		
		this.buttonPanel = new JPanel();
		this.buttonPanel.add(this.buttonTrain);
		this.buttonPanel.add(this.buttonGo);
		this.buttonPanel.add(this.buttonClear);
		this.buttonPanel.add(this.buttonClearMatrix);
		content.add(this.buttonPanel, BorderLayout.SOUTH);

		this.buttonTrain.addActionListener(this);
		this.buttonGo.addActionListener(this);
		this.buttonClear.addActionListener(this);
		this.buttonClearMatrix.addActionListener(this);
		
		this.panel = new HopfieldPanel(20,20);
		content.add(this.panel, BorderLayout.CENTER);
	}
	

	private JPanel buttonPanel;
	private JButton buttonTrain;
	private JButton buttonGo;
	private JButton buttonClear;
	private JButton buttonClearMatrix;

	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == this.buttonClear) {
			this.panel.clear();
		} else if (e.getSource() == this.buttonClearMatrix) {
			this.panel.clearMatrix();
		} else if (e.getSource() == this.buttonGo) {
			this.panel.go();
		} else if (e.getSource() == this.buttonTrain) {
			this.panel.train();
		}
	}



	public static void main(String[] args)
	{
		HopfieldPattern program = new HopfieldPattern();
		program.setVisible(true);
	}
}
