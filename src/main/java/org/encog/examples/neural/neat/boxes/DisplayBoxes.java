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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.encog.neural.neat.NEATPopulation;

/**
 * Display the boxes on the screen using Swing.
 *
 */
public class DisplayBoxes extends JFrame implements ActionListener {
	/**
	 * The serial id.
	 */
	private static final long serialVersionUID = 1L;
	public static final String[] RESOLUTIONS = { "11", "22", "33", "44", "55" };
	private JComboBox resolution;
	private DisplayBoxesPanel display;
	private JButton newCase;
	
	public DisplayBoxes(NEATPopulation thePopulation) {
		setSize(400,400);
		setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.resolution=new JComboBox(RESOLUTIONS));
		buttonPanel.add(newCase=new JButton("New Case"));
		this.add(buttonPanel,BorderLayout.NORTH);
		this.display = new DisplayBoxesPanel(thePopulation);
		this.add(this.display, BorderLayout.CENTER);
		
		this.newCase.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if( evt.getSource()==this.newCase ) {
			this.display.createNewCase(Integer.parseInt(this.resolution.getSelectedItem().toString()));
		}
	}
	
	
}
