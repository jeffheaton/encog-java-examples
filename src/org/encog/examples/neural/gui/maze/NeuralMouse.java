package org.encog.examples.neural.gui.maze;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;

public class NeuralMouse {

	private final BasicNetwork brain;
	private final Maze environment;
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
				.isWall(x, y, Maze.NORTH) ? 1 : -1);

		// one o'clock
		this.vision.setData(Constants.VISION_POINT_1_OCLOCK, environment
				.isWall(xNE, yNE, Maze.WEST) ? 1 : -1);

		// two o'clock
		this.vision.setData(Constants.VISION_POINT_2_OCLOCK, environment
				.isWall(xNE, yNE, Maze.SOUTH) ? 1 : -1);

		// three o'clock
		this.vision.setData(Constants.VISION_POINT_3_OCLOCK, environment
				.isWall(x, y, Maze.EAST) ? 1 : -1);

		// four o'clock
		this.vision.setData(Constants.VISION_POINT_4_OCLOCK, environment
				.isWall(xSE, ySE, Maze.NORTH) ? 1 : -1);

		// five o'clock
		this.vision.setData(Constants.VISION_POINT_5_OCLOCK, environment
				.isWall(xSE, ySE, Maze.WEST) ? 1 : -1);

		// six o'clock
		this.vision.setData(Constants.VISION_POINT_6_OCLOCK, environment
				.isWall(x, y, Maze.SOUTH) ? 1 : -1);

		// seven o'clock
		this.vision.setData(Constants.VISION_POINT_7_OCLOCK, environment
				.isWall(xSW, ySW, Maze.EAST) ? 1 : -1);

		// eight o'clock
		this.vision.setData(Constants.VISION_POINT_8_OCLOCK, environment
				.isWall(xSW, ySW, Maze.NORTH) ? 1 : -1);

		// nine o'clock
		this.vision.setData(Constants.VISION_POINT_9_OCLOCK, environment
				.isWall(x, y, Maze.WEST) ? 1 : -1);

		// ten o'clock
		this.vision.setData(Constants.VISION_POINT_10_OCLOCK, environment
				.isWall(xNW, yNW, Maze.SOUTH) ? 1 : -1);

		// eleven o'clock
		this.vision.setData(Constants.VISION_POINT_11_OCLOCK, environment
				.isWall(xNW, yNW, Maze.EAST) ? 1 : -1);
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
	
	public int autonomousMoveDirection()
	{
		updateVision();
		NeuralData result = this.brain.compute(this.vision);
		
		double winningOutput = Double.NEGATIVE_INFINITY;
		int winningIndex = -1;
		
		for(int i=0;i<result.size();i++)
		{
			double thisOutput = result.getData(i);
			if( thisOutput>winningOutput)
			{
				winningOutput = thisOutput;
				winningIndex = i;
			}
		}
		
		if( winningIndex==Constants.MOTOR_NORTH )
			return Maze.NORTH;
		else if( winningIndex==Constants.MOTOR_SOUTH )
			return Maze.SOUTH;
		else if( winningIndex==Constants.MOTOR_EAST )
			return Maze.EAST;
		else
			return Maze.WEST;
		
	}
	
	public boolean move()
	{
		int direction = autonomousMoveDirection();
		return false;
		
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

}
