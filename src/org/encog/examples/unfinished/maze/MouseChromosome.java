/*
 * Encog(tm) Artificial Intelligence Framework v2.3
 * Java Examples
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
