/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.unfinished.maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.util.logging.Logging;

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
		
		return ((30*30) - score);
	}
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		List<NeuralMouse> population = new ArrayList<NeuralMouse>();
		
		EvaluateMouse eval = new EvaluateMouse(10);
		
		/*while(population.size()<1000)
		{
			NeuralMouse mouse = MouseFactory.generateMouse(null);
			int score = eval.evaluate(mouse);
			if(score<1000 ) {
				System.out.println(population.size() + " - " + score);
				population.add(mouse);
			}
		}*/
		
		/*MouseGA ga = new MouseGA(1000,0.5,0.1,eval);
		
		int epoch = 1;

		do {
			ga.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + ga.getError());
			epoch++;
			
		for(int i=0;i<500;i++)
			{
				((MouseChromosome)ga.getGenetic().getChromosome(999-i)).getMouse().getBrain().reset();
				((MouseChromosome)ga.getGenetic().getChromosome(999-i)).updateGenes();
			}
		} while (ga.getError()>1);*/
		
		
	}
}
