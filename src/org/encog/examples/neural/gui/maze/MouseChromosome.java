package org.encog.examples.neural.gui.maze;

import org.encog.neural.networks.training.genetic.NeuralChromosome;

public class MouseChromosome extends NeuralChromosome {

	private final NeuralMouse mouse;
	private final EvaluateMouse eval;
	
	public MouseChromosome(MouseGA ga,NeuralMouse mouse, EvaluateMouse eval)
	{
		this.mouse = mouse;
		this.eval = eval;
		this.setGeneticAlgorithm(ga.getGenetic());
		this.setNetwork(mouse.getBrain());
	}
	
	@Override
	public void calculateCost() {
		// update the network with the new gene values
		updateNetwork();
		
		// calculate the cost
		this.setCost(this.eval.evaluate(this.mouse));		
	}

	public NeuralMouse getMouse() {
		return mouse;
	}

	public EvaluateMouse getEval() {
		return eval;
	}
	
	

}
