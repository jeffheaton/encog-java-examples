package org.encog.examples.neural.forest.som;

import org.encog.normalize.DataNormalization;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

public class ForestCoverSOM {
	public static void generate() {
		GenerateData generate = new GenerateData();
		generate.step1();
		DataNormalization norm = generate.step2();

		EncogPersistedCollection encog = new EncogPersistedCollection(
				Constant.TRAINED_NETWORK_FILE);
		encog.add(Constant.NORMALIZATION_NAME, norm);
	}

	public static void train(boolean useGUI) {
		TrainNetwork program = new TrainNetwork();
		program.train(useGUI);
	}

	public static void evaluate() {
		Evaluate evaluate = new Evaluate();
		evaluate.evaluate();
	}

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out
					.println("Usage: ForestCover [generate [e/o]/train/traingui/evaluate] ");
		} else {
			Logging.stopConsoleLogging();
			if (args[0].equalsIgnoreCase("generate")) {
				generate();
			} else if (args[0].equalsIgnoreCase("train"))
				train(false);
			else if (args[0].equalsIgnoreCase("traingui"))
				train(true);
			else if (args[0].equalsIgnoreCase("evaluate"))
				evaluate();
		}
	}
}
