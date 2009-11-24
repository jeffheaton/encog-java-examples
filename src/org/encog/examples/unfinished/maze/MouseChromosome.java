package org.encog.examples.unfinished.maze;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.genetic.NeuralChromosome;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;

public class MouseChromosome extends NeuralChromosome {

	public MouseChromosome(NeuralGeneticAlgorithm genetic, BasicNetwork network) {
		super(genetic, network);
		this.eval = null;
		this.mouse = null;
	}

	private final NeuralMouse mouse;
	private final EvaluateMouse eval;
	
	/*public MouseChromosome(MouseGA ga,NeuralMouse mouse, EvaluateMouse eval)
	{
		this.mouse = mouse;
		this.eval = eval;
		this.setGeneticAlgorithm(ga.getGenetic());
		this.setNetwork(mouse.getBrain());
	}*/
	
	@Override
	public void calculateScore() {
		// update the network with the new gene values
		updateNetwork();
		
		// calculate the cost
		this.setScore(this.eval.evaluate(this.mouse));		
	}

	public NeuralMouse getMouse() {
		return mouse;
	}

	public EvaluateMouse getEval() {
		return eval;
	}
	
	

}
