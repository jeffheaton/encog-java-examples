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
			int iterations = runner.runToConverge(100,0.8);
			if( iterations>=100 && runner.getScore()>0.01 ) {
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
