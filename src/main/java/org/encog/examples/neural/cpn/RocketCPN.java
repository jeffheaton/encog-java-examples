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
package org.encog.examples.neural.cpn;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.cpn.CPN;
import org.encog.neural.cpn.training.TrainInstar;
import org.encog.neural.cpn.training.TrainOutstar;

/**
 * 
 * Use a counterpropagation network to determine the angle a rocket is pointed.
 * 
 * This is based on a an example by Karsten Kutza, 
 * written in C on 1996-01-24.
 * http://www.neural-networks-at-your-fingertips.com
 *
 */
public class RocketCPN {
	
	public static final int WIDTH = 11;
	public static final int HEIGHT = 11;

	public static final String[][] PATTERN1 =  { { 
		"           ",
        "           ",
        "     O     ",
        "     O     ",
        "    OOO    ",
        "    OOO    ",
        "    OOO    ",
        "   OOOOO   ",
        "   OOOOO   ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "        O  ",
        "       O   ",
        "     OOO   ",
        "    OOO    ",
        "   OOO     ",
        " OOOOO     ",
        "OOOOO      ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "           ",
        "  OO       ",
        "  OOOOO    ",
        "  OOOOOOO  ",
        "  OOOOO    ",
        "  OO       ",
        "           ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "OOOOO      ",
        " OOOOO     ",
        "   OOO     ",
        "    OOO    ",
        "     OOO   ",
        "       O   ",
        "        O  ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "   OOOOO   ",
        "   OOOOO   ",
        "    OOO    ",
        "    OOO    ",
        "    OOO    ",
        "     O     ",
        "     O     ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "      OOOOO",
        "     OOOOO ",
        "     OOO   ",
        "    OOO    ",
        "   OOO     ",
        "   O       ",
        "  O        ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "           ",
        "       OO  ",
        "    OOOOO  ",
        "  OOOOOOO  ",
        "    OOOOO  ",
        "       OO  ",
        "           ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "  O        ",
        "   O       ",
        "   OOO     ",
        "    OOO    ",
        "     OOO   ",
        "     OOOOO ",
        "      OOOOO",
        "           ",
        "           "  } };

	String[][] PATTERN2 = { { 
		"           ",
        "           ",
        "     O     ",
        "     O     ",
        "     O     ",
        "    OOO    ",
        "    OOO    ",
        "    OOO    ",
        "   OOOOO   ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "     O     ",
        "     O     ",
        "    O O    ",
        "    O O    ",
        "    O O    ",
        "   O   O   ",
        "   O   O   ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "           ",
        "     O     ",
        "    OOO    ",
        "    OOO    ",
        "    OOO    ",
        "   OOOOO   ",
        "           ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "           ",
        "           ",
        "     O     ",
        "     O     ",
        "     O     ",
        "    OOO    ",
        "           ",
        "           ",
        "           "  },

      { "           ",
        "  O        ",
        "     O     ",
        "     O     ",
        "    OOO    ",
        "    OO     ",
        "    OOO   O",
        "    OOOO   ",
        "   OOOOO   ",
        "           ",
        "       O   "  },

      { "           ",
        "           ",
        "     O     ",
        "     O     ",
        "    OOO    ",
        "    OOO    ",
        "    OOO    ",
        "   OOOOO   ",
        "   OOOOO   ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "       O   ",
        "      O    ",
        "    OOO    ",
        "    OOO    ",
        "   OOO     ",
        "  OOOOO    ",
        " OOOOO     ",
        "           ",
        "           "  },

      { "           ",
        "           ",
        "        O  ",
        "       O   ",
        "     OOO   ",
        "    OOO    ",
        "   OOO     ",
        " OOOOO     ",
        "OOOOO      ",
        "           ",
        "           "  } };
	public static final double HI = 1;
	public static final double LO = 0;
	
	
	private double[][] input1;
	private double[][] input2;
	private double[][] ideal1;
	
	private int inputNeurons;
	private int instarNeurons;
	private int outstarNeurons;

	public void prepareInput()
	{
		int n,i,j;
		
		this.inputNeurons = WIDTH * HEIGHT;
		this.instarNeurons = PATTERN1.length;
		this.outstarNeurons = 2;
		
		this.input1 = new double[PATTERN1.length][this.inputNeurons];
		this.input2 = new double[PATTERN2.length][this.inputNeurons];
		this.ideal1 = new double[PATTERN1.length][this.outstarNeurons];

		  for (n=0; n<PATTERN1.length; n++) {
		    for (i=0; i<HEIGHT; i++) {
		      for (j=0; j<WIDTH; j++) {
		    	input1[n][i*WIDTH+j] = (PATTERN1[n][i].charAt(j) == 'O') ? HI : LO;
		    	input2[n][i*WIDTH+j] = (PATTERN2[n][i].charAt(j) == 'O') ? HI : LO;
		      }
		    }
		  }
		  normalizeInput();
		  for (n=0; n<PATTERN1.length; n++) {
		    this.ideal1[n][0] = Math.sin(n * 0.25 * Math.PI);
		    this.ideal1[n][1] = Math.cos(n * 0.25 * Math.PI);
		  }

	}
	
	public double sqr(double x)
	{
		return x*x;
	}
	
	
	void normalizeInput()
	{
	  int  n,i;
	  double length1, length2;

	  for (n=0; n<PATTERN1.length; n++) {
	    length1 = 0;
	    length2 = 0;
	    for (i=0; i<this.inputNeurons; i++) {
	      length1  += sqr(this.input1[n][i]);
	      length2 += sqr(this.input2[n][i]);
	    }
	    length1  = Math.sqrt(length1);
	    length2 = Math.sqrt(length2);
	    
	    for (i=0; i<this.inputNeurons; i++) {
	      input1[n][i] /= length1;
	      input2[n][i] /= length2;
	    }
	  }
	}

	public CPN createNetwork()
	{		
		CPN result = new CPN(this.inputNeurons, this.instarNeurons, this.outstarNeurons,1);
        return result;
	}
	
	public void trainInstar(CPN network,MLDataSet training)
	{
		int epoch = 1;

		MLTrain train = new TrainInstar(network,training,0.1,true);
		do {
			train.iteration();
			System.out
					.println("Training instar, Epoch #" + epoch + ", Error: " + train.getError() );
			epoch++;
		} while(train.getError()>0.01);
	}
	
	public void trainOutstar(CPN network,MLDataSet training)
	{
		int epoch = 1;

		MLTrain train = new TrainOutstar(network,training,0.1);
		do {
			train.iteration();
			System.out
					.println("Training outstar, Epoch #" + epoch + ", error=" + train.getError() );
			epoch++;
		} while(train.getError()>0.01); 
	}
	
	public MLDataSet generateTraining(double[][] input,double[][] ideal)
	{
		MLDataSet result = new BasicMLDataSet(input,ideal);
		return result;
	}
	
	public double determineAngle(MLData angle)
	{
		double result;

		  result = ( Math.atan2(angle.getData(0), angle.getData(1)) / Math.PI) * 180;
		  if (result < 0)
		    result += 360;

		  return result;
	}
	
	public void test(CPN network,String[][] pattern,double[][] input)
	{
		for(int i=0;i<pattern.length;i++)
		{
			MLData inputData = new BasicMLData(input[i]);
			MLData outputData = network.compute(inputData);
			double angle = determineAngle(outputData);
			
			// display image
			for(int j=0;j<HEIGHT;j++)
			{
				if( j==HEIGHT-1 )
					System.out.println("["+pattern[i][j]+"] -> " + ((int)angle) + " deg");
				else
					System.out.println("["+pattern[i][j]+"]");
			}
			
			System.out.println();
		}
	}
	
	public void run()
	{
		prepareInput();
		normalizeInput();
		CPN network = createNetwork();
		MLDataSet training = generateTraining(this.input1,this.ideal1);
		trainInstar(network,training);
		trainOutstar(network,training);
		test(network,PATTERN1,this.input1);
	}


	
	public static void main(String[] args)
	{
		RocketCPN cpn = new RocketCPN();
		cpn.run();
		Encog.getInstance().shutdown();
	}
	
}
