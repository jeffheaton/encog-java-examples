package org.encog.examples.neural.gui.maze;

import org.encog.neural.networks.BasicNetwork;

public class NeuralMouse {
	
	private final BasicNetwork brain;
	private final Maze environment;
	private int row;
	private int column;

	public NeuralMouse(BasicNetwork brain, Maze environment)
	{
		this.brain = brain;
		this.environment = environment;
		this.row = 0;
		this.column = 0;
	}
	
	public int getRow()
	{
		return this.row;
	}
	
	public int getColumn()
	{
		return this.column;
	}
	
}
