package org.encog.examples.neural.lunar;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.CalculateScore;

public class PilotGA implements CalculateScore {
	@Override
	public double calculateScore(BasicNetwork network) {
		NeuralPilot pilot = new NeuralPilot(network, false);
		return pilot.scorePilot();
	}

	@Override
	public boolean shouldMinimize() {
		return false;
	}
}
