package org.encog.examples.indicator.convert;

import java.io.File;

import org.encog.app.quant.ninja.NinjaFileConvert;
import org.encog.util.csv.CSVFormat;

/**
 * This simple example shows how to convert financial data into the
 * form that NinjaTrader can recognize.  The input data must have CSV
 * headers that indicate the following fields:
 * 
 * date
 * time
 * high
 * low
 * open
 * close
 * volume
 * 
 * This data will be rearranged to fit the NinjaTrader format, which is
 * documented here:
 * 
 *  http://www.ninjatrader.com/support/helpGuides/nt7/index.html?importing.htm
 *
 */
public class NinjaConvert {
	public static void main(String[] args)
	{
		if( args.length!=2 ) {
			System.out.println("convert [from file] [to file]");
			System.exit(0);
		} else {
			File sourceFile = new File(args[0]);
			File targetFile = new File(args[1]);
			
			NinjaFileConvert convert = new NinjaFileConvert();
			convert.analyze(sourceFile, true, CSVFormat.ENGLISH);
			System.out.println("Performing conversion.");
			convert.process(targetFile);
		}
	}
}
