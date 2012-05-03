package org.encog.examples.indicator.ninja.ema;

import org.encog.Encog;
import org.encog.cloud.indicator.basic.BasicIndicator;
import org.encog.cloud.indicator.server.IndicatorLink;
import org.encog.cloud.indicator.server.IndicatorPacket;
import org.encog.util.csv.CSVFormat;

public class EMA extends BasicIndicator {

	public EMA() {
		super(true);
		requestData("CLOSE");
		requestData("THIS[2]");
	}

	@Override
	public void notifyPacket(IndicatorPacket packet) {
		if (packet.getCommand().equalsIgnoreCase(IndicatorLink.PACKET_BAR)) {
			String security = packet.getArgs()[1];
			long when = Long.parseLong(packet.getArgs()[0]);
			
			double dataClose = CSVFormat.EG_FORMAT.parse(packet.getArgs()[2]);
			double lastValue = CSVFormat.EG_FORMAT.parse(packet.getArgs()[4]);
			double period = 14;
			
			double result;
			
			if( Double.isNaN(lastValue) )
				result = dataClose;
			else
				result =  dataClose * (2.0 / (1 + period)) + (1 - (2.0 / (1 + period))) * lastValue;
			
			String[] args = { 
					CSVFormat.EG_FORMAT.format(result,Encog.DEFAULT_PRECISION),
					"?",
					"?",
					"?",
					"?",
					"?",
					"?",
					"?"};
			
			this.getLink().writePacket("ind", args);
		}
	}

	@Override
	public void notifyTermination() {
		// don't really care		
	}


}
