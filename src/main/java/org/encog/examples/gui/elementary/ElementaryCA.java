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
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.encog.ca.program.CAProgram;
import org.encog.ca.program.conway.ConwayProgram;
import org.encog.ca.runner.BasicCARunner;
import org.encog.ca.runner.CARunner;
import org.encog.ca.universe.Universe;
import org.encog.ca.universe.UniverseListener;
import org.encog.ca.universe.basic.BasicCellFactory;
import org.encog.ca.universe.basic.BasicUniverse;
import org.encog.ca.visualize.CAVisualizer;
import org.encog.ca.visualize.basic.BasicCAVisualizer;
import org.encog.persist.EncogDirectoryPersistence;

public class ElementaryCA extends JFrame implements ActionListener, WindowListener, UniverseListener, ItemListener {

	private JTextField ruleText;
	private JTextField sizeText;
	private JButton generateButton;
	private JComboBox<String> zoomCombo;

	private JLabel status;
	private CARunner worldRunner;
	private CAVisualizer visualizer;
	private DisplayPanel worldArea;
	private JScrollPane scroll;

	public ElementaryCA() {
		setSize(500, 500);
		setTitle("Elementary CA");
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		c.add(buttonPanel, BorderLayout.NORTH);
		c.add(this.status=new JLabel(), BorderLayout.SOUTH);
		buttonPanel.add(new JLabel("Rule:"));
		buttonPanel.add(new JTextField("110"));
		buttonPanel.add(new JLabel("Size:"));
		buttonPanel.add(new JTextField("500"));		
		buttonPanel.add(generateButton = new JButton("Generate"));
		
		this.worldArea = new DisplayPanel();
		this.scroll = new JScrollPane(this.worldArea);
		c.add(this.scroll, BorderLayout.CENTER);		
		generateButton.addActionListener(this);
				
		String[] test = { "1x", "2x", "3x", "5x", "10x" };
		this.zoomCombo = new JComboBox<String>(test);
		buttonPanel.add(new JLabel("Zoom:"));
		buttonPanel.add(zoomCombo);
		zoomCombo.addItemListener(this);		
		this.addWindowListener(this);	
	}

	public void performIteration() {
		this.worldRunner.iteration();
	}
	
	private void setupUniverse(Universe theUniverse) {
		CAProgram physics = new ConwayProgram(theUniverse);
		
		this.worldRunner = new BasicCARunner(
				theUniverse,
				physics);
		this.worldRunner.addListener(this);
		this.visualizer = new BasicCAVisualizer(theUniverse);
		this.worldArea.setCurrentImage(this.visualizer.visualize());
	}
	
	public void performGenerate() {
		
		Universe universe = new BasicUniverse(this.worldArea.getHeight(),this.worldArea.getWidth(),new BasicCellFactory(1,1));
		CAProgram physics = new ConwayProgram(universe);
		
		
		this.worldRunner = new BasicCARunner(
				universe,
				physics);
		this.worldRunner.addListener(this);
		this.visualizer = new BasicCAVisualizer(universe);
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
			JFrame f = new ElementaryCA();
			f.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void itemStateChanged(ItemEvent ev) {
		if( ev.getItemSelectable()==this.zoomCombo ) {
			if( ev.getStateChange()==ItemEvent.SELECTED ) {				
				boolean shouldRestart = false;
				
				if( this.worldRunner!=null && this.worldRunner.isRunning() ) {
					shouldRestart = true;
					//performStop();
				}
				
				String str = ev.getItem().toString();
				int zoom = Integer.parseInt(str.substring(0, str.length()-1));
				this.visualizer.setZoom(zoom);
				
				if( shouldRestart ) {
					//performStart();
				}
			}
		}	
	}
}