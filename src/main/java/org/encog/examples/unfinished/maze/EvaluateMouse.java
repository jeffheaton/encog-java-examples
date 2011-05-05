/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
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
package org.encog.examples.unfinished.maze;

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
		
		return ((30*30) - score);
	}
	
	public static void main(String args[])
	{
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
