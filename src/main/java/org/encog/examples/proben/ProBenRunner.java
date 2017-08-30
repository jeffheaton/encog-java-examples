/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
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
package org.encog.examples.proben;

import java.io.File;

import org.encog.util.Stopwatch;
import org.encog.util.file.FileUtil;

public class ProBenRunner {
	private BenchmarkDefinition def;
	private File dir;
	private boolean mergeTest = true;
	private ProBenResultAccumulator accumulator = new ProBenResultAccumulator();
	
	public ProBenRunner(BenchmarkDefinition benchmarkDef) {
		this.def = benchmarkDef;
		this.dir = new File(def.getProBenFolder());
	}
	
	/**
	 * @return the mergeTest
	 */
	public boolean isMergeTest() {
		return mergeTest;
	}



	/**
	 * @param mergeTest the mergeTest to set
	 */
	public void setMergeTest(boolean mergeTest) {
		this.mergeTest = mergeTest;
	}



	public ProBenResultAccumulator run() {
		Stopwatch sw = new Stopwatch();
		sw.start();
		runDirectory(this.dir);
		
		System.out.println("Final results: " + this.accumulator.toString());
		sw.stop();
		System.out.println("Runtime: " + sw.toString());
		return this.accumulator;
	}
	
	public void runDirectory(File file) {
		
		for(File childFile: file.listFiles()) {
			if( childFile.isDirectory()) {
				runDirectory(childFile);				
			} else {
				if( FileUtil.getFileExt(childFile).equalsIgnoreCase("dt")) {
					runFile(childFile);
				}
			}
		}
		
	}
	
	public void runFile(File file) {
		ProBenData data = new ProBenData(file,this.mergeTest);
		data.load();

		if( this.def.shouldCenter() ) {
			data.center(def.getInputCenter(), def.getOutputCenter());
		}
		
		ProBenEvaluate eval = new ProBenEvaluate(data, this.def);
		ProBenResult result = eval.evaluate();
		System.out.println("Using result:" + result.toString());
		this.accumulator.accumulate(result);
	}
}
