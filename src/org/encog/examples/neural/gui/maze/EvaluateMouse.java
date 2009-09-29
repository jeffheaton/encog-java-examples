package org.encog.examples.neural.gui.maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EvaluateMouse {
	
	private List<Maze> mazes = new ArrayList<Maze>();
	
	public EvaluateMouse(int size)
	{
		for(int i=0;i<size;i++)
		{
			Maze maze = new Maze(30,30);
			maze.generateMaze();
			this.mazes.add(maze);
		}
	}
	
	public int evaluate(NeuralMouse mouse)
	{
		int score = 0;
		
		for( Maze maze: mazes )
		{
			mouse.setEnvironment(maze);
			mouse.setX(0);
			mouse.setY(0);
			
			Set<String> segmentScore = new HashSet<String>();
			for(int i=0;i<100;i++)
			{
				String key = mouse.getX()+":"+mouse.getY();
				segmentScore.add(key);
				mouse.move();
			} 
			
			score+=segmentScore.size();
		}
		
		return score;
	}
	
	public static void main(String args[])
	{
		Maze maze1 = new Maze(30,30);
		maze1.generateMaze();
		Maze maze2 = new Maze(30,30);
		maze2.generateMaze();
		
		NeuralMouse mouse = MouseFactory.generateMouse(maze1);
		EvaluateMouse eval = new EvaluateMouse(10);
		for(;;)
		{
			mouse.getBrain().reset();
			int score = eval.evaluate(mouse);
			System.out.println(score);
		}
	}
}
