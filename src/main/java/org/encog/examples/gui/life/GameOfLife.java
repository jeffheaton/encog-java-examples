package org.encog.examples.gui.life;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.encog.ca.program.CAProgram;
import org.encog.ca.program.ConwayProgram;
import org.encog.ca.runner.BasicCARunner;
import org.encog.ca.runner.CARunner;
import org.encog.ca.universe.Universe;
import org.encog.ca.universe.UniverseListener;
import org.encog.ca.universe.basic.BasicCellFactory;
import org.encog.ca.universe.basic.BasicUniverse;
import org.encog.ca.visualize.CAVisualizer;
import org.encog.ca.visualize.basic.BasicCAVisualizer;

public class GameOfLife extends JFrame implements ActionListener, WindowListener, UniverseListener {

	private JButton goButton;
	private JButton startButton;
	private JButton stopButton;
	private JButton resetButton;
	private JPanel outputPanel;

	private JLabel status;
	private CARunner worldRunner;
	private CAVisualizer visualizer;
	
	private static final int HEIGHT = 500;
	private static final int WIDTH = 500;

	public GameOfLife() {
		setSize(400, 150);
		setTitle("Hello World");
		Universe world = new BasicUniverse(HEIGHT,WIDTH,new BasicCellFactory(1,1)); 
		CAProgram physics = new ConwayProgram(world);
		
		world.randomize();
		physics.randomize();
		
		this.worldRunner = new BasicCARunner(
				world,
				physics);
		this.visualizer = new BasicCAVisualizer(world);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		c.add(buttonPanel, BorderLayout.NORTH);
		c.add(this.status=new JLabel(), BorderLayout.SOUTH);
		buttonPanel.add(goButton = new JButton("Go"));
		buttonPanel.add(startButton = new JButton("Start"));
		buttonPanel.add(stopButton = new JButton("Stop"));
		buttonPanel.add(resetButton = new JButton("Reset"));
		c.add(outputPanel = new JPanel(), BorderLayout.CENTER);
		goButton.addActionListener(this);
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		resetButton.addActionListener(this);
		
		this.addWindowListener(this);
		this.worldRunner.addListener(this);
	}

	public void performGo() {
		this.worldRunner.iteration();
	}

	public void performStart() {
		this.worldRunner.start();
	}

	public void performStop() {
		this.worldRunner.stop();
	}
	
	public void performReset() {
		this.worldRunner.reset();
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == goButton) {
			performGo();
		} else if (ev.getSource() == startButton) {
			performStart();
		} else if (ev.getSource() == stopButton) {
			performStop();
		} else if (ev.getSource() == resetButton) {
			performReset();
		}

	}

	public static void test() throws IOException {
		int width = 400; // Dimensions of the image
		int height = 400;
		// Let's create a BufferedImage for a binary image.
		BufferedImage im = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_BINARY);
		// We need its raster to set the pixels' values.
		WritableRaster raster = im.getRaster();
		// Put the pixels on the raster. Note that only values 0 and 1 are used
		// for the pixels.
		// You could even use other values: in this type of image, even values
		// are black and odd
		// values are white.
		for (int h = 0; h < height; h++)
			for (int w = 0; w < width; w++)
				if (((h / 50) + (w / 50)) % 2 == 0)
					raster.setSample(w, h, 0, 0); // checkerboard pattern.
				else
					raster.setSample(w, h, 0, 1);
		// Store the image using the PNG format.
		// ImageIO.write(im, "PNG", new File("e:\\checkboard.png"));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void iterationComplete() {
		int width = WIDTH;
		int height = HEIGHT;

		Image img = this.visualizer.visualize();
		Graphics g = outputPanel.getGraphics();
		g.drawImage(img, 0, 0, null);
		
		String str = this.worldRunner.toString();
		this.status.setText(str);
		//System.out.println(str);
	}
	
	public static void main(String[] args) {
		try {
			JFrame f = new GameOfLife();
			f.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}