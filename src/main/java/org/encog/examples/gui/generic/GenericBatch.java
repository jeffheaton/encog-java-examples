/*
 * Encog(tm) Java Examples v3.2
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2013 Heaton Research, Inc.
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
package org.encog.examples.gui.generic;

import java.io.File;

import org.encog.ca.program.CAProgram;
import org.encog.ca.program.generic.GenericCA;
import org.encog.ca.program.generic.GenericIO;
import org.encog.ca.runner.BasicCARunner;
import org.encog.ca.runner.CARunner;
import org.encog.ca.universe.Universe;
import org.encog.ca.universe.basic.BasicCellFactory;
import org.encog.ca.universe.basic.BasicUniverse;

public class GenericBatch {
	
	public static void main(String[] args) {
		Universe universe = new BasicUniverse(500,500,new BasicCellFactory(3,-1,1)); 
		universe.randomize();
		CAProgram physics = new GenericCA(universe,5);
		physics.randomize();
		
		CARunner runner = new BasicCARunner(
				universe,
				physics);
		
		//this.visualizer = new BasicCAVisualizer(theUniverse);
		//this.worldArea.setCurrentImage(this.visualizer.visualize());
		
		int t = 0;
		
		for (;;) {
			universe.randomize();
			physics.randomize();		
			String status = "fail";
			int iterations = runner.runToConverge(100,0.1);
			if( iterations>=100 && runner.getScore()>0.0 ) {
				save(runner,t);
				status = "save";
			}
			System.out.println("Try: " + t  + " , Iterations: " + iterations + ", Score: " + runner.getScore() + ", Status:" + status);
			t++;
		}
	}

	private static void save(CARunner runner, int t) {
		File f = new File("d:\\test\\world-"+t+".bin");
		GenericIO.save(runner, f);
	}
}
