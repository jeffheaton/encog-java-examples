package org.encog.examples.indicator.ema;

import org.encog.cloud.indicator.IndicatorConnectionListener;
import org.encog.cloud.indicator.IndicatorFactory;
import org.encog.cloud.indicator.IndicatorListener;
import org.encog.cloud.indicator.server.IndicatorLink;
import org.encog.cloud.indicator.server.IndicatorServer;

public class RemoteEMA implements IndicatorConnectionListener {

	public static final int PORT = 5128;

	public void run() {
		IndicatorServer server = new IndicatorServer();
		server.addListener(this);

		server.addIndicatorFactory(new IndicatorFactory() {
			@Override
			public String getName() {
				return "EMA";
			}

			@Override
			public IndicatorListener create() {
				return new EMA();
			}
		});

		server.start();
		
	}

	public static void main(String[] args) {
		System.out.println("Waiting for connections on port " + PORT);
		RemoteEMA program = new RemoteEMA();
		program.run();
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
