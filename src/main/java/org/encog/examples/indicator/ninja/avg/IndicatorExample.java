package org.encog.examples.indicator.ninja.avg;

import java.io.File;

import org.encog.cloud.indicator.IndicatorConnectionListener;
import org.encog.cloud.indicator.IndicatorFactory;
import org.encog.cloud.indicator.IndicatorListener;
import org.encog.cloud.indicator.server.IndicatorLink;
import org.encog.cloud.indicator.server.IndicatorServer;

public class IndicatorExample implements IndicatorConnectionListener {

	public static final int PORT = 5128;
	private File path;

	public IndicatorExample(File thePath) {
		this.path = thePath;
	}

	public void train() {

	}

	private void generate() {
		GenerateTraining gen = new GenerateTraining(this.path);
		gen.generate();
	}

	private void clear() {
		File[] list = this.path.listFiles();

		for (File file : list) {
			String fn = file.getName();
			if (fn.startsWith("collected") && fn.endsWith(".csv")) {
				file.delete();
			}
		}
		
		System.out.println("Directory cleared of captured financial data.");

	}

	public void run(final boolean collectMode) {

		if (collectMode) {
			System.out.println("Ready to collect data from remote indicator.");
		} else {
			System.out.println("Indicator ready.");
		}

		System.out.println("Waiting for connections on port " + PORT);

		IndicatorServer server = new IndicatorServer();
		server.addListener(this);

		server.addIndicatorFactory(new IndicatorFactory() {
			@Override
			public String getName() {
				return "MYIND";
			}

			@Override
			public IndicatorListener create() {
				return new MyInd(collectMode, path);
			}
		});

		server.start();

	}

	public static void main(String[] args) {		

		if (args.length != 2) {
			System.out
					.println("Usage: IndicatorExample [clear/collect/generate/train/run] [work path]");
		} else {
			IndicatorExample program = new IndicatorExample(new File(args[1]));
			if (args[0].equalsIgnoreCase("collect")) {
				program.run(true);
			} else if (args[0].equalsIgnoreCase("train")) {
				program.train();
			} else if (args[0].equalsIgnoreCase("run")) {
				program.run(false);
			} else if (args[0].equalsIgnoreCase("clear")) {
				program.clear();
			} else if (args[0].equalsIgnoreCase("generate")) {
				program.generate();
			}
		}
	}

	@Override
	public void notifyConnections(IndicatorLink link, boolean hasOpened) {
		if (hasOpened) {
			System.out.println("Connection from " + link.getSocket().toString()
					+ " established.");
		} else if (!hasOpened) {
			System.out.println("Connection from " + link.getSocket().toString()
					+ " terminated.");
		}
	}
}
