package org.encog.examples.neural.benchmark;

import org.encog.StatusReportable;
import org.encog.util.benchmark.EncogBenchmark;

public class Benchmark implements StatusReportable {
	


	public void report(int total, int current, String message) {
		System.out.println( current + " of " + total + ":"+message);
		
	}
	
	public double run()
	{
		EncogBenchmark mark = new EncogBenchmark(this);
		return mark.process();	
	}
	
	public static void main(String args[])
	{
		Benchmark b = new Benchmark();
		System.out.println("Benchmark result: " + b.run() );
	}
	
}
