package org.encog.examples.neural.gui.maze;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class NeuralMouse {

	private BasicNetwork brain;
	private Maze environment;
	private int x;
	private int y;
	NeuralData vision;

	public NeuralMouse(BasicNetwork brain, Maze environment) {
		this.brain = brain;
		this.environment = environment;
		this.x = 0;
		this.y = 0;
		this.vision = new BasicNeuralData(Constants.VISION_POINTS);
	}

	public void updateVision() {
		// North-East Corner
		int xNE = x + 1;
		int yNE = y - 1;

		// North-West Corner
		int xNW = x - 1;
		int yNW = y - 1;

		// South-East Corner
		int xSE = x + 1;
		int ySE = y + 1;

		// South-West Corner
		int xSW = x - 1;
		int ySW = y + 1;

		// twelve o'clock
		this.vision.setData(Constants.VISION_POINT_12_OCLOCK, environment
				.isWall(x, y, Maze.NORTH) ? Constants.HI:Constants.LO);

		// one o'clock
		this.vision.setData(Constants.VISION_POINT_1_OCLOCK, environment
				.isWall(xNE, yNE, Maze.WEST) ? Constants.HI:Constants.LO);

		// two o'clock
		this.vision.setData(Constants.VISION_POINT_2_OCLOCK, environment
				.isWall(xNE, yNE, Maze.SOUTH) ? Constants.HI:Constants.LO);

		// three o'clock
		this.vision.setData(Constants.VISION_POINT_3_OCLOCK, environment
				.isWall(x, y, Maze.EAST) ? Constants.HI:Constants.LO);

		// four o'clock
		this.vision.setData(Constants.VISION_POINT_4_OCLOCK, environment
				.isWall(xSE, ySE, Maze.NORTH) ? Constants.HI:Constants.LO);

		// five o'clock
		this.vision.setData(Constants.VISION_POINT_5_OCLOCK, environment
				.isWall(xSE, ySE, Maze.WEST) ? Constants.HI:Constants.LO);

		// six o'clock
		this.vision.setData(Constants.VISION_POINT_6_OCLOCK, environment
				.isWall(x, y, Maze.SOUTH) ? Constants.HI:Constants.LO);

		// seven o'clock
		this.vision.setData(Constants.VISION_POINT_7_OCLOCK, environment
				.isWall(xSW, ySW, Maze.EAST) ? Constants.HI:Constants.LO);

		// eight o'clock
		this.vision.setData(Constants.VISION_POINT_8_OCLOCK, environment
				.isWall(xSW, ySW, Maze.NORTH) ? Constants.HI:Constants.LO);

		// nine o'clock
		this.vision.setData(Constants.VISION_POINT_9_OCLOCK, environment
				.isWall(x, y, Maze.WEST) ? Constants.HI:Constants.LO);

		// ten o'clock
		this.vision.setData(Constants.VISION_POINT_10_OCLOCK, environment
				.isWall(xNW, yNW, Maze.SOUTH) ? Constants.HI:Constants.LO);

		// eleven o'clock
		this.vision.setData(Constants.VISION_POINT_11_OCLOCK, environment
				.isWall(xNW, yNW, Maze.EAST) ? Constants.HI:Constants.LO);
	}

	public boolean move(int direction) {
		
		if( this.environment.isWall(this.x, this.y, direction))
			return false;

		
		switch (direction) {
		case Maze.NORTH:
			y--;
			break;
		case Maze.SOUTH:
			y++;
			break;
		case Maze.EAST:
			x++;
			break;
		case Maze.WEST:
			x--;
			break;
		}

		return true;
	}
	
	private int directionFromIndex(int i)
	{
		if( i==Constants.MOTOR_NORTH )
			return Maze.NORTH;
		else if( i==Constants.MOTOR_SOUTH )
			return Maze.SOUTH;
		else if( i==Constants.MOTOR_EAST )
			return Maze.EAST;
		else
			return Maze.WEST;
	}
	
	public int autonomousMoveDirection()
	{
		updateVision();
		NeuralData result = this.brain.compute(this.vision);
		
		double winningOutput = Double.NEGATIVE_INFINITY;
		int winningDirection = 0;
		
		for(int i=0;i<result.size();i++)
		{
			// determine direction
			int direction = directionFromIndex(i);			
			
			if( this.environment.isWall(this.x, this.y, direction))
				continue;
			
			// evaluate if this is a "winning" direction
			double thisOutput = result.getData(i);
			if( thisOutput>winningOutput)
			{
				winningOutput = thisOutput;
				winningDirection = direction;
			}
		}
		
		return winningDirection;		
	}
	
	public boolean move()
	{
		int direction = autonomousMoveDirection();
		return move(direction);		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	public BasicNetwork getBrain() {
		return brain;
	}

	public void setBrain(BasicNetwork brain) {
		this.brain = brain;
	}

	public Maze getEnvironment() {
		return environment;
	}

	public void setEnvironment(Maze environment) {
		this.environment = environment;
	}

	

}
