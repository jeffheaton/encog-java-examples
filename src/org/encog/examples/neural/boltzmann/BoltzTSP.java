package org.encog.examples.neural.boltzmann;

import org.encog.neural.activation.ActivationBiPolar;
import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.Layer;
import org.encog.neural.networks.logic.BoltzmannLogic;
import org.encog.neural.networks.synapse.Synapse;
import org.encog.neural.pattern.BoltzmannPattern;
import org.encog.util.math.BoundMath;
import org.encog.util.randomize.RangeRandomizer;

public class BoltzTSP {

	public static final int NUM_CITIES = 10;
	public static final int NEURON_COUNT = NUM_CITIES * NUM_CITIES;

	private double gamma = 7;
	private double[][] distance;		

	public double sqr(double x) {
		return x * x;
	}

	public void createCities() {
		double x1, x2, y1, y2;
		double alpha1, alpha2;
		
		this.distance = new double[NUM_CITIES][NUM_CITIES];

		for (int n1 = 0; n1 < NUM_CITIES; n1++) {
			for (int n2 = 0; n2 < NUM_CITIES; n2++) {
				alpha1 = ((double) n1 / NUM_CITIES) * 2 * Math.PI;
				alpha2 = ((double) n2 / NUM_CITIES) * 2 * Math.PI;
				x1 = Math.cos(alpha1);
				y1 = Math.sin(alpha1);
				x2 = Math.cos(alpha2);
				y2 = Math.sin(alpha2);
				distance[n1][n2] = Math.sqrt(sqr(x1 - x2) + sqr(y1 - y2));
			}
		}		
	}

	public boolean isValidTour(BiPolarNeuralData data) {
		int cities, stops;

		for (int n1 = 0; n1 < NUM_CITIES; n1++) {
			cities = 0;
			stops = 0;
			for (int n2 = 0; n2 < NUM_CITIES; n2++) {
				if (data.getBoolean(n1 * NUM_CITIES + n2)) {
					if (++cities > 1)
						return false;
				}
				if (data.getBoolean(n2 * NUM_CITIES + n1)) {
					if (++stops > 1)
						return false;
				}
			}
			if ((cities != 1) || (stops != 1))
				return false;
		}
		return true;
	}

	public double lengthOfTour(BiPolarNeuralData data) {
		double result;
		int n1, n2, n3;

		result = 0;
		for (n1 = 0; n1 < NUM_CITIES; n1++) {
			for (n2 = 0; n2 < NUM_CITIES; n2++) {
				if (data.getBoolean(((n1) % NUM_CITIES) * NUM_CITIES + n2))
					break;
			}
			for (n3 = 0; n3 < NUM_CITIES; n3++) {
				if (data.getBoolean(((n1 + 1) % NUM_CITIES) * NUM_CITIES + n3))
					break;
			}
			result += distance[n2][n3];
		}
		return result;
	}

	String displayTour(BiPolarNeuralData data) {
		StringBuilder result = new StringBuilder();

		int n1, n2;
		boolean first;

		for (n1 = 0; n1 < NUM_CITIES; n1++) {
			first = true;
			result.append("[");
			for (n2 = 0; n2 < NUM_CITIES; n2++) {
				if (data.getBoolean(n1 * NUM_CITIES + n2)) {
					if (first) {
						first = false;
						result.append(n2);
					} else {
						result.append(", " + n2);
					}
				}
			}
			result.append("]");
			if (n1 != NUM_CITIES - 1) {
				result.append(" -> ");
			}
		}
		return result.toString();
	}

	public void calculateWeights(BasicNetwork network) {
		int n1, n2, n3, n4;
		int i, j;
		int predN3, succN3;
		double weight;
		
		BoltzmannLogic logic = (BoltzmannLogic)network.getLogic();

		for (n1 = 0; n1 < NUM_CITIES; n1++) {
			for (n2 = 0; n2 < NUM_CITIES; n2++) {
				i = n1 * NUM_CITIES + n2;
				for (n3 = 0; n3 < NUM_CITIES; n3++) {
					for (n4 = 0; n4 < NUM_CITIES; n4++) {
						j = n3 * NUM_CITIES + n4;
						weight = 0;
						if (i != j) {
							predN3 = (n3 == 0 ? NUM_CITIES - 1 : n3 - 1);
							succN3 = (n3 == NUM_CITIES - 1 ? 0 : n3 + 1);
							if ((n1 == n3) || (n2 == n4))
								weight = -gamma;
							else if ((n1 == predN3) || (n1 == succN3))
								weight = -distance[n2][n4];
						}
						logic.getThermalSynapse().getMatrix().set(i, j, weight);
					}
				}
				logic.getThermalLayer().setThreshold(i, -gamma / 2);
			}
		}
	}


	public void run() {
		BoltzmannPattern pattern = new BoltzmannPattern();
		pattern.setInputNeurons(NEURON_COUNT);
		BasicNetwork network = pattern.generate();
		BoltzmannLogic logic = (BoltzmannLogic)network.getLogic();

		createCities();
		calculateWeights(network);

		logic.setTemperature(100);
		do {
			logic.establishEquilibrium();
			System.out.println(logic.getTemperature()+" : "+displayTour(logic.getCurrentState()));
			logic.decreaseTemperature(0.99);
		} while (!isValidTour(logic.getCurrentState()));

		System.out.println("Final Length: " + this.lengthOfTour(logic.getCurrentState()) );
	}

	public static void main(String[] args) {
		BoltzTSP program = new BoltzTSP();
		program.run();
	}

}
