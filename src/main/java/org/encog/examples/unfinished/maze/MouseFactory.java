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

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class MouseFactory {
	
	public static NeuralMouse generateMouse(Maze maze)
	{
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(Constants.INPUT_NEURON_COUNT));
		network.addLayer(new BasicLayer(60));
		//network.addLayer(new BasicLayer(30));
		network.addLayer(new BasicLayer(Constants.OUTPUT_NEURON_COUNT));
		network.getStructure().finalizeStructure();
		network.reset();
		
		NeuralMouse mouse = new NeuralMouse(network,maze);
		return mouse;
	}
	
	public static NeuralMouse generateSmartMouse()
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
			if( score>50 )
				return mouse;
		}
	}
}
