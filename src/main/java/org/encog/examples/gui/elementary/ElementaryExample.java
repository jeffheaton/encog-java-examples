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
package org.encog.examples.gui.elementary;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.encog.ca.program.CAProgram;
import org.encog.ca.program.conway.ConwayProgram;
import org.encog.ca.program.elementary.ElementaryCA;
import org.encog.ca.runner.BasicCARunner;
import org.encog.ca.runner.CARunner;
import org.encog.ca.universe.Universe;
import org.encog.ca.universe.UniverseListener;
import org.encog.ca.universe.basic.BasicCellFactory;
import org.encog.ca.universe.basic.BasicUniverse;
import org.encog.ca.visualize.CAVisualizer;
import org.encog.ca.visualize.basic.BasicCAVisualizer;

public class ElementaryExample extends JFrame implements ActionListener, WindowListener, UniverseListener, ItemListener {

	private JTextField ruleText;
	private JTextField sizeText;
	private JButton generateButton;
	private JComboBox zoomCombo;

	private JLabel status;
	private CARunner worldRunner;
	private CAVisualizer visualizer;
	private DisplayPanel worldArea;
	private JScrollPane scroll;
	private int zoom = 1;

	public ElementaryExample() {
		setSize(500, 500);
		setTitle("Elementary CA");
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		c.add(buttonPanel, BorderLayout.NORTH);
		c.add(this.status=new JLabel(), BorderLayout.SOUTH);
		buttonPanel.add(new JLabel("Rule:"));
		buttonPanel.add(this.ruleText = new JTextField("110"));
		buttonPanel.add(new JLabel("Size:"));
		buttonPanel.add(this.sizeText = new JTextField("500"));		
		buttonPanel.add(generateButton = new JButton("Generate"));
		
		this.worldArea = new DisplayPanel();
		this.scroll = new JScrollPane(this.worldArea);
		c.add(this.scroll, BorderLayout.CENTER);		
		generateButton.addActionListener(this);
				
		String[] test = { "1x", "2x", "3x", "5x", "10x" };
		this.zoomCombo = new JComboBox(test);
		buttonPanel.add(new JLabel("Zoom:"));
		buttonPanel.add(zoomCombo);
		zoomCombo.addItemListener(this);		
		this.addWindowListener(this);	
	}

	public void performIteration() {
		this.worldRunner.iteration();
	}
	
	public void performGenerate() {
		
		int rule = Integer.parseInt(ruleText.getText());
		int size = Integer.parseInt(sizeText.getText());
		
		Universe universe = new BasicUniverse((int)(size*1.5),(int)(size*2.5),new BasicCellFactory(1,1));
		CAProgram physics = new ElementaryCA(universe,rule);
		
		
		this.worldRunner = new BasicCARunner(
				universe,
				physics);
		
		for(int i=0;i<size;i++)
		{
			physics.iteration();
		}
		
		this.visualizer = new BasicCAVisualizer(universe);
		this.visualizer.setZoom(this.zoom);
		this.worldArea.setCurrentImage(this.visualizer.visualize());
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == generateButton) {
			performGenerate();
		} 
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {

		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		//this.worldRunner.stop();
		System.exit(0);
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		//performReset();
	}

	@Override
	public void iterationComplete() {
		this.worldArea.setCurrentImage(this.visualizer.visualize());
		String str = this.worldRunner.toString();
		this.status.setText(str);
		repaint();
	}
	
	public static void main(String[] args) {
		try {
			JFrame f = new ElementaryExample();
			f.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void itemStateChanged(ItemEvent ev) {
		if( ev.getItemSelectable()==this.zoomCombo ) {
			if( ev.getStateChange()==ItemEvent.SELECTED ) {				
				String str = ev.getItem().toString();
				zoom = Integer.parseInt(str.substring(0, str.length()-1));
				this.visualizer.setZoom(zoom);
				this.performGenerate();
			}
		}	
	}
}
