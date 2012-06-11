package org.encog.examples.indicator.dl;

import java.io.File;

import org.encog.cloud.indicator.IndicatorConnectionListener;
import org.encog.cloud.indicator.basic.DownloadIndicatorFactory;
import org.encog.cloud.indicator.server.IndicatorLink;
import org.encog.cloud.indicator.server.IndicatorServer;

public class ImportIndicatorData implements IndicatorConnectionListener {
	
	public static final int PORT = 5128;	
	
	public void run()
	{
		DownloadIndicatorFactory ind = new DownloadIndicatorFactory(new File("d:\\ninja.csv"));		
		ind.requestData("HIGH[5]");
		ind.requestData("LOW[1]");
		ind.requestData("OPEN[1]");
		ind.requestData("CLOSE[1]");
		ind.requestData("VOL[1]");	
		ind.requestData("MACD(12,26,9).0[1]");
		
		IndicatorServer server = new IndicatorServer();
		server.addListener(this);
		server.addIndicatorFactory(ind);
		server.start();
		server.waitForIndicatorCompletion();
	}
	
	public static void main(String[] args) {
		System.out.println("Waiting for connections on port " + PORT);
		ImportIndicatorData program = new ImportIndicatorData();
		program.run();		
	}


	@Override
	public void notifyConnections(IndicatorLink link, boolean hasOpened) {
		if( hasOpened ) {
			System.out.println("Connection from " + link.getSocket().toString() + " established.");
		} else if( !hasOpened ) {
			System.out.println("Connection from " + link.getSocket().toString() + " terminated.");
		}
		
	}
}
