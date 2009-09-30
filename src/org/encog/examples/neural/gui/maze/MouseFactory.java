package org.encog.examples.neural.gui.maze;

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
