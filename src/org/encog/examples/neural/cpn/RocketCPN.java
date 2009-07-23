package org.encog.examples.neural.cpn;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.Layer;

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
	
	private Layer inputLayer;
	private Layer instarLayer;
	private Layer outstarLayer;

	public void prepareInput()
	{
		int n,i,j;
		
		this.inputNeurons = WIDTH * HEIGHT;
		this.instarNeurons = 2;
		this.outstarNeurons = PATTERN1.length;
		
		this.input1 = new double[PATTERN1.length][this.inputNeurons];
		this.input2 = new double[PATTERN2.length][this.inputNeurons];
		this.ideal1 = new double[PATTERN1.length][this.instarNeurons];

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

	public BasicNetwork createNetwork()
	{
		BasicNetwork network = new BasicNetwork();
		network.addLayer(this.inputLayer = new BasicLayer(this.inputNeurons));
		network.addLayer(this.instarLayer = new BasicLayer(this.instarNeurons));
		network.addLayer(this.outstarLayer = new BasicLayer(this.outstarNeurons));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}


	
	public static void main(String[] args)
	{
		
	}
	
}
