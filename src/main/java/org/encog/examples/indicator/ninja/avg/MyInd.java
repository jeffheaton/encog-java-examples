package org.encog.examples.indicator.ninja.avg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.encog.EncogError;
import org.encog.cloud.indicator.basic.BasicIndicator;
import org.encog.cloud.indicator.basic.InstrumentHolder;
import org.encog.cloud.indicator.server.IndicatorPacket;

public class MyInd extends BasicIndicator {

	private InstrumentHolder holder = new InstrumentHolder();
	private int rowsDownloaded;
	private boolean collectMode;
	private File path;

	public MyInd(boolean theCollectMode, File thePath) {
		super(!theCollectMode);
		this.collectMode = theCollectMode;
		this.path = thePath;
		
		requestData("CLOSE[1]");
		requestData("SMA(10)[3]");
		requestData("SMA(25)[3]");
	}

	@Override
	public void notifyPacket(IndicatorPacket packet) {
		String security = packet.getArgs()[1];
		long when = Long.parseLong(packet.getArgs()[0]);
		String key = security.toLowerCase();

		if (this.collectMode) {
			if (holder.record(when, 2, packet.getArgs())) {
				this.rowsDownloaded++;
			}
		} else {

		}
	}

	public File nextFile() {
		int mx = -1;
		File[] list = this.path.listFiles();

		for (File file : list) {
			String fn = file.getName();
			if (fn.startsWith("collected") && fn.endsWith(".csv")) {
				int idx = fn.indexOf(".csv");
				String str = fn.substring(9, idx);
				int n = Integer.parseInt(str);
				mx = Math.max(n, mx);
			}
		}

		return new File(path, "collected" + (mx + 1) + ".csv");
	}

	public void writeCollectedFile() {
		File targetFile = nextFile();

		try {
			FileWriter outFile = new FileWriter(targetFile);
			PrintWriter out = new PrintWriter(outFile);

			// output header
			out.print("\"WHEN\"");
			int index = 0;
			for (String str : this.getDataRequested()) {
				String str2;
				
				// strip off [ if needed
				int ix = str.indexOf('[');
				if (ix != -1) {
					str2 = str.substring(0, ix).trim();
				} else {
					str2 = str;
				}
				
				int c = getDataCount().get(index++);
				if (c <= 1) {
					out.print(",\"" + str2 + "\"");
				} else {
					for (int i = 0; i < c; i++) {						
						out.print(",\"" + str2 + "-b" + i + "\"");
					}
				}
			}
			out.println();

			// output data

			for (Long key : holder.getSorted()) {
				String str = holder.getData().get(key);
				out.println(key + "," + str);
			}

			out.close();
		} catch (IOException ex) {
			throw new EncogError(ex);
		}
	}

	@Override
	public void notifyTermination() {
		if (this.collectMode) {
			writeCollectedFile();
		}
	}

}
