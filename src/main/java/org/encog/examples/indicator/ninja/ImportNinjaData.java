package org.encog.examples.indicator.ninja;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.encog.EncogError;
import org.encog.cloud.indicator.IndicatorLink;
import org.encog.cloud.indicator.IndicatorListener;
import org.encog.cloud.indicator.IndicatorPacket;
import org.encog.cloud.indicator.IndicatorServer;
import org.encog.cloud.indicator.InstrumentHolder;
import org.encog.util.logging.EncogLogging;

public class ImportNinjaData implements IndicatorListener {
	
	public static final int PORT = 5128;
	
	private int rowsDownloaded;
	private Map<String, InstrumentHolder> data = new HashMap<String, InstrumentHolder>();
	private IndicatorLink mylink;
	private List<String> dataRequested = new ArrayList<String>();
	private IndicatorServer node;
	private boolean done;
	
	public ImportNinjaData() 
	{		
	}
	
	public void requestData(String str) {
		dataRequested.add(str);
	}
	
	public static void main(String[] args) {
		ImportNinjaData imp = new ImportNinjaData();
		imp.requestData("HIGH");
		imp.requestData("LOW");
		imp.requestData("OPEN");
		imp.requestData("CLOSE");
		imp.requestData("VOL");	
		imp.requestData("MACD(12,26,9).Avg[0]");
		imp.performImport(new File("d:\\ninja.csv"));
	}
	
	private void performImport(File file) {
		
		this.node = new IndicatorServer(PORT);
		this.node.start();
		this.node.addListener(this);
		System.out.println("Now accepting connections on port: " + PORT);
		
		while(!done) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		saveFile(file);
		System.out.println("Connection broken, downloaded " + this.rowsDownloaded + " rows.");
		node.shutdown();
		
	}

	public void saveFile(File targetFile) {
		try {
			FileWriter outFile = new FileWriter(targetFile);
			PrintWriter out = new PrintWriter(outFile);

			out.print("\"INSTRUMENT\",\"WHEN\"");
			for( String str: this.dataRequested ) {
				out.print(",\""+str+"\"");
			}
			out.println();
			
			
			for (String ins : this.data.keySet()) {
				InstrumentHolder holder = this.data.get(ins);
				for (Long key : holder.getSorted()) {
					String str = holder.getData().get(key);
					out.println("\"" + ins + "\"," + key + "," + str);
				}
			}

			out.close();
		} catch (IOException ex) {
			throw new EncogError(ex);
		}

	}


	@Override
	public void notifyPacket(IndicatorPacket packet) {
		if (packet.getCommand().equalsIgnoreCase("bar")) {
			try {
				String security = packet.getArgs()[1];
				long when = Long.parseLong(packet.getArgs()[0]);
				String key = security.toLowerCase();
				InstrumentHolder holder = null;

				if (this.data.containsKey(key)) {
					holder = this.data.get(key);
				} else {
					holder = new InstrumentHolder();
					this.data.put(key, holder);
				}

				if (holder.record(when, 2, packet.getArgs())) {
					this.rowsDownloaded++;
					System.out.println("Received row " + this.rowsDownloaded);
				}
			} catch (Exception ex) {
				EncogLogging.log(ex);
			}

		}

	}

	@Override
	public void notifyConnections(IndicatorLink link, boolean hasOpened) {
		if( hasOpened && mylink==null) {
			this.mylink = link;
			this.mylink.requestSignal(this.dataRequested);
			System.out.println("Connection from " + link.getSocket().toString() + " established.");
		} else if( !hasOpened && link==this.mylink ) {
			this.done = true;
		}
		
	}
}
