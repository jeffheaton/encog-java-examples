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
