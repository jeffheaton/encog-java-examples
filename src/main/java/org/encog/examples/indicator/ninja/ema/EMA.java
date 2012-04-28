package org.encog.examples.indicator.ninja.ema;

import org.encog.Encog;
import org.encog.cloud.indicator.basic.BasicIndicator;
import org.encog.cloud.indicator.server.IndicatorPacket;
import org.encog.util.csv.CSVFormat;

public class EMA extends BasicIndicator {

	public EMA() {
		super(true);
		requestData("CLOSE");
	}

	@Override
	public void notifyPacket(IndicatorPacket packet) {
		if (packet.getCommand().equalsIgnoreCase("bar")) {
			String security = packet.getArgs()[1];
			long when = Long.parseLong(packet.getArgs()[0]);
			
			double result = Math.random();
			String[] args = { 
					CSVFormat.EG_FORMAT.format(result,Encog.DEFAULT_PRECISION),
					CSVFormat.EG_FORMAT.format(result,Encog.DEFAULT_PRECISION),
					CSVFormat.EG_FORMAT.format(result,Encog.DEFAULT_PRECISION) };
			
			this.getLink().writePacket("ind", args);
		}
	}

	@Override
	public void notifyTermination() {
		// don't really care		
	}


}
