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
package org.encog.examples.ml.world;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This GUI application displays a grid that is used to train a Hopfield 
 * neural network.  To make use of this application draw small localized 
 * patterns on the grid and click train once each pattern has been drawn.  
 * The grid can be cleared between patterns.  Each pattern should be 
 * trained.  TO test, draw part of one of the patterns and select "GO".
 *
 */
public class QLearningPattern extends JFrame  implements ActionListener {

	private QLearningPanel panel;
	
	public QLearningPattern()
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
		
		this.panel = new QLearningPanel(20,20);
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
		QLearningPattern program = new QLearningPattern();
		program.setVisible(true);
	}
}
