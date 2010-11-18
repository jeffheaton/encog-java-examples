/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.examples.neural.benchmark;

import org.encog.Encog;
import org.encog.engine.StatusReportable;
import org.encog.util.benchmark.EncogBenchmark;
import org.encog.util.logging.Logging;

/**
 * Simple console app that uses the Encog benchmarking class.
 * This will print out a number that shows how fast your computer is
 * with Encog.  The lower the better.
 * @author jeff
 *
 */
public class Benchmark implements StatusReportable {

	public static void main(final String args[]) {
		Logging.stopConsoleLogging();
		final Benchmark b = new Benchmark();
		System.out.println("Benchmark result: " + b.run());
		
		Encog.getInstance().shutdown();
	}

	public void report(final int total, final int current, final String message) {
		System.out.println(current + " of " + total + ":" + message);

	}

	public String run() {
		final EncogBenchmark mark = new EncogBenchmark(this);
		String result = mark.process();
		if( mark.getDevice()!=null )
		{
			System.out.println("OpenCL Device Used: " + mark.getDevice().toString() );
		}
		return result;
	}

}
