package org.encog.examples.neural.neat.boxes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.encog.neural.hyperneat.substrate.Substrate;
import org.encog.neural.hyperneat.substrate.SubstrateFactory;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.training.NEATTraining;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.Format;

public class VisualizeBoxesMain extends JFrame implements Runnable, ActionListener {
	
	private JButton btnTraining;
	private JButton btnExample;
	private JComboBox methodChoice;
	private boolean trainingUnderway;
	private JLabel labelIterations;
	private JLabel labelError;
	private boolean requestStop = false;
	private NEATPopulation pop;
	private NEATTraining train;
	
	public VisualizeBoxesMain() {
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Visualize Boxes");
		setSize(400,200);
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(this.btnTraining = new JButton("Start Training"));
		buttonPanel.add(this.btnExample = new JButton("Run Example"));
		content.add(buttonPanel,BorderLayout.SOUTH);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,2));
		content.add(mainPanel, BorderLayout.NORTH);
		mainPanel.add(new JLabel("Target (best) Score:"));
		mainPanel.add(new JLabel("110"));
		mainPanel.add(new JLabel("Current Score:"));
		mainPanel.add(this.labelError = new JLabel("N/A"));
		mainPanel.add(new JLabel("Iteration Count:"));
		mainPanel.add(this.labelIterations = new JLabel("0"));
		
		this.btnTraining.addActionListener(this);
		this.btnExample.addActionListener(this);
		this.btnExample.setEnabled(false);		
	}
	
	public void resetTraining() {
		Substrate substrate = SubstrateFactory.factorSandwichSubstrate(11, 11);
		BoxesScore score = new BoxesScore(11);
		pop = new NEATPopulation(substrate,500);
		pop.reset();
		train = new NEATTraining(score,pop);
		//train.setThreadCount(1);
	}
	
	public static void main(String[] args) {
		VisualizeBoxesMain boxes = new VisualizeBoxesMain();		
		boxes.setVisible(true);
	}

	@Override
	public void run() {
	
		if( this.pop==null ) {
			this.btnTraining.setText("Setting up...");
			this.btnTraining.setEnabled(false);
			resetTraining();
		}
		
		// update the GUI
		this.btnTraining.setText("Stop Training");
		this.btnTraining.setEnabled(true);
		this.btnExample.setEnabled(false);		
		this.trainingUnderway = true;
	
		this.requestStop = false;
		while(!this.requestStop) {
			this.train.iteration();
			this.labelError.setText(Format.formatDouble(train.getError(),2));
			this.labelIterations.setText(Format.formatInteger(this.train.getIteration()));
			EncogDirectoryPersistence.saveObject(new File("/Users/jheaton/test.eg"), pop);
		}
		
		this.btnTraining.setText("Start Training");
		this.btnExample.setEnabled(true);
		this.trainingUnderway = false;
	}
	
	private void beginTraining() {
		Thread t = new Thread(this);
		t.start();
	}
	
	public void handleTraining() {
		if(this.trainingUnderway) {
			this.requestStop = true;
		} else {
			beginTraining();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if( ev.getSource() == this.btnTraining ) {
			handleTraining();
		} if( ev.getSource() == this.btnExample ) {
			DisplayBoxes display = new DisplayBoxes(pop);
			display.setVisible(true);
		}
	}
}
