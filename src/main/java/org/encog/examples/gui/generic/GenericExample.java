package org.encog.examples.gui.generic;
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

import org.encog.ca.program.CAProgram;
import org.encog.ca.program.generic.GenericCA;
import org.encog.ca.program.generic.GenericIO;
import org.encog.ca.runner.BasicCARunner;
import org.encog.ca.runner.CARunner;
import org.encog.ca.universe.Universe;
import org.encog.ca.universe.UniverseListener;
import org.encog.ca.universe.basic.BasicCellFactory;
import org.encog.ca.universe.basic.BasicUniverse;
import org.encog.ca.visualize.CAVisualizer;
import org.encog.ca.visualize.basic.BasicCAVisualizer;
import org.encog.persist.EncogDirectoryPersistence;

public class GenericExample extends JFrame implements ActionListener, WindowListener, UniverseListener, ItemListener {

	private JButton iterationButton;
	private JButton startButton;
	private JButton stopButton;
	private JButton resetButton;
	private JButton loadButton;
	private JButton saveButton;
	private JComboBox<String> zoomCombo;

	private JLabel status;
	private CARunner worldRunner;
	private CAVisualizer visualizer;
	private GenericPanel worldArea;
	private JScrollPane scroll;

	public GenericExample() {
		setSize(500, 500);
		setTitle("Generic Game of Life");
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		c.add(buttonPanel, BorderLayout.NORTH);
		c.add(this.status=new JLabel(), BorderLayout.SOUTH);
		buttonPanel.add(iterationButton = new JButton("Iteration"));
		buttonPanel.add(startButton = new JButton("Start"));
		buttonPanel.add(stopButton = new JButton("Stop"));
		buttonPanel.add(resetButton = new JButton("Reset"));		
		buttonPanel.add(loadButton = new JButton("Load"));
		buttonPanel.add(saveButton = new JButton("Save"));
		
		this.worldArea = new GenericPanel();
		this.scroll = new JScrollPane(this.worldArea);
		c.add(this.scroll, BorderLayout.CENTER);		
		iterationButton.addActionListener(this);
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		resetButton.addActionListener(this);
		loadButton.addActionListener(this);
		saveButton.addActionListener(this);
		
		
		String[] test = { "1x", "2x", "3x", "5x", "10x" };
		this.zoomCombo = new JComboBox<String>(test);
		buttonPanel.add(new JLabel("Zoom:"));
		buttonPanel.add(zoomCombo);
		zoomCombo.addItemListener(this);		
		this.addWindowListener(this);	
		
		this.stopButton.setEnabled(false);
	}

	public void performIteration() {
		this.worldRunner.iteration();
	}

	public void performStart() {
		this.iterationButton.setEnabled(false);
		this.stopButton.setEnabled(true);
		this.startButton.setEnabled(false);
		this.loadButton.setEnabled(false);
		this.saveButton.setEnabled(false);
		this.worldRunner.start();
	}

	public void performStop() {
		this.iterationButton.setEnabled(true);
		this.stopButton.setEnabled(false);
		this.startButton.setEnabled(true);
		this.loadButton.setEnabled(true);
		this.saveButton.setEnabled(true);		
		this.worldRunner.stop();
	}
	
	public void performReset() {
		boolean shouldRestart = false;
		
		if( this.worldRunner!=null && this.worldRunner.isRunning() ) {
			shouldRestart = true;
			performStop();
		}
		
		Universe universe = new BasicUniverse(this.worldArea.getHeight(),this.worldArea.getWidth(),new BasicCellFactory(3,-1,1)); 
		universe.randomize();
		
		setupUniverse(universe);
		
		if( shouldRestart ) {
			performStart();
		}
	}
	
	private void setupUniverse(Universe theUniverse) {
		CAProgram physics = new GenericCA(theUniverse,5);
		physics.randomize();
		
		this.worldRunner = new BasicCARunner(
				theUniverse,
				physics);
		this.worldRunner.addListener(this);
		this.visualizer = new BasicCAVisualizer(theUniverse);
		this.worldArea.setCurrentImage(this.visualizer.visualize());
	}
	
	public void performLoad() {
		final JFileChooser fc = new JFileChooser();
		int rc = fc.showOpenDialog(this);
		if( rc == JFileChooser.APPROVE_OPTION ) {
			File f = fc.getSelectedFile();
			GenericIO.load(f, this.worldRunner);
			this.visualizer = new BasicCAVisualizer(this.worldRunner.getUniverse());
			this.worldArea.setCurrentImage(this.visualizer.visualize());
		}
	}
	
	public void performSave() {
		final JFileChooser fc = new JFileChooser();
		int rc = fc.showSaveDialog(this);
		if( rc == JFileChooser.APPROVE_OPTION ) {
			File f = fc.getSelectedFile();
			GenericIO.save(this.worldRunner, f);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == iterationButton) {
			performIteration();
		} else if (ev.getSource() == startButton) {
			performStart();
		} else if (ev.getSource() == stopButton) {
			performStop();
		} else if (ev.getSource() == resetButton) {
			performReset();
		} else if (ev.getSource() == saveButton) {
			performSave();
		} else if (ev.getSource() == loadButton) {
			performLoad();
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
		this.worldRunner.stop();
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
		performReset();
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
			JFrame f = new GenericExample();
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
					performStop();
				}
				
				String str = ev.getItem().toString();
				int zoom = Integer.parseInt(str.substring(0, str.length()-1));
				this.visualizer.setZoom(zoom);
				
				if( shouldRestart ) {
					performStart();
				}
			}
		}	
	}
}