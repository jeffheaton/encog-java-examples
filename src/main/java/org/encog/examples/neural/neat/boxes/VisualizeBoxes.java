package org.encog.examples.neural.neat.boxes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VisualizeBoxes extends JFrame implements Runnable, ActionListener {
	
	private JButton training;
	private JButton example;
	private JComboBox<String> methodChoice;
	
	public VisualizeBoxes() {
		String[] options = { "Feedforward", "NEAT", "HyperNEAT" };
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Visualize Boxes");
		setSize(400,200);
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(this.training = new JButton("Start Training"));
		buttonPanel.add(this.example = new JButton("Run Example"));
		content.add(buttonPanel,BorderLayout.SOUTH);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,2));
		content.add(mainPanel, BorderLayout.NORTH);
		mainPanel.add(new JLabel("Method:"));
		mainPanel.add(this.methodChoice = new JComboBox(options));
		mainPanel.add(new JLabel("Training Error:"));
		mainPanel.add(new JLabel("N/A"));
		mainPanel.add(new JLabel("Iteration Count:"));
		mainPanel.add(new JLabel("0"));
		
		this.training.addActionListener(this);
		this.example.addActionListener(this);
		
		
		this.example.setEnabled(false);
	}
	
	public static void main(String[] args) {
		VisualizeBoxes boxes = new VisualizeBoxes();		
		boxes.setVisible(true);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
