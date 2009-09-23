package org.encog.examples.neural.maze;

import java.awt.*;
import javax.swing.*;

public class MouseMaze extends JFrame implements Runnable {
	MazePanel maze = new MazePanel();
	JTextField stateNorth = new JTextField("00");
	JTextField stateSouth = new JTextField("00");
	JTextField stateEast = new JTextField("00");
	JTextField stateWest = new JTextField("00");

	public MouseMaze() {

		Container content = getContentPane();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(gridbag);

		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		// maze
		c.gridwidth = 1;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.NORTHWEST;

		maze.setSize(300, 300);
		content.add(maze, c);

		// Current state
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		content.add(new JLabel("Current State"), c);

		// north
		c.gridwidth = 1;
		stateNorth.setEditable(false);
		stateSouth.setEditable(false);
		stateEast.setEditable(false);
		stateWest.setEditable(false);

		content.add(new JLabel("N:"), c);
		content.add(stateNorth, c);
		content.add(new JLabel("S:"), c);
		content.add(stateSouth, c);
		content.add(new JLabel("E:"), c);
		content.add(stateEast, c);
		content.add(new JLabel("W:"), c);
		content.add(stateWest, c);

		// adjust size and position
		pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		setLocation((int) (d.width - this.getSize().getWidth()) / 2,
				(int) (d.height - this.getSize().getHeight()) / 2);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		Thread t = new Thread(this);
		t.start();

	}

	public void run() {
		try {
			for (;;) {
				Thread.sleep(100);

				double array[][] = new double[1][4];
				array[0][0] = maze.getMouseState(MazePanel.NORTH);
				array[0][1] = maze.getMouseState(MazePanel.SOUTH);
				array[0][2] = maze.getMouseState(MazePanel.EAST);
				array[0][3] = maze.getMouseState(MazePanel.WEST);

			}
		} catch (InterruptedException e) {
		}
	}

	protected void displayState() {
		stateNorth.setText("" + maze.getMouseState(MazePanel.NORTH));
		stateSouth.setText("" + maze.getMouseState(MazePanel.SOUTH));
		stateEast.setText("" + maze.getMouseState(MazePanel.EAST));
		stateWest.setText("" + maze.getMouseState(MazePanel.WEST));
	}

	public static void main(String args[]) {
		(new MouseMaze()).show();
	}
}