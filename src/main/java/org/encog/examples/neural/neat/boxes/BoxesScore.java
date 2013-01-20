package org.encog.examples.neural.neat.boxes;

import java.util.Random;

import org.encog.mathutil.IntPair;
import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;

public class BoxesScore implements CalculateScore  {

    public static final double EDGE_LEN = 2.0;
    public static final double SQR_LEN = 0.5772;
    public static final double DIMENSIONS = 3;
    private final int resolution;
    private final double pixelSize;
    private final double orig;
    
    public BoxesScore(int theResolution)
    {
    	resolution = theResolution;
        pixelSize = EDGE_LEN / theResolution;
        orig = -1.0 + (pixelSize/2.0);
    }
    
    public void queryNetwork(TrialEvaluation eval, int pos) {
    	// first, get 
    	IntPair targetPos = eval.getTest().initTestCase(pos);
        
    	
    	// first, create the input data
    	int inputIdx = 0;
        MLData inputData = new BasicMLData(resolution*resolution);
        
        double yReal = orig;
        for(int y=0; y<resolution; y++, yReal += pixelSize)
        {
            double xReal = orig;
            for(int x=0; x<resolution; x++, xReal += pixelSize, inputIdx++)
            {
            	inputData.setData(inputIdx, eval.getTest().getPixel(xReal, yReal));
            }
        }
        
        // second, query the network
        MLData output = ((NEATNetwork)eval.getPhenotype()).compute(inputData);
        
        // finally, process the output
        double minActivation = Double.POSITIVE_INFINITY;
        double maxActivation = Double.NEGATIVE_INFINITY;
        int idx = 0;

        for(int i=0; i<output.size(); i++)
        {
            double d = output.getData(i);

            if(d > maxActivation)
            {
                maxActivation = d;
                idx = i;
            } 
            else if(d < minActivation)
            {
                minActivation = d;
            }
        }

        int y = idx / resolution;
        int x = idx - (y * resolution);
        IntPair selectedPoint = new IntPair(x, y);
        
        eval.accumulate(CalcRealDistanceSquared(targetPos, selectedPoint),
        		Math.max(0.0, maxActivation - minActivation));
        
        
    }
	
	@Override
	public double calculateScore(MLMethod phenotype) {
        BoxTrial test = new BoxTrial(new Random());
        TrialEvaluation eval = new TrialEvaluation(phenotype, test);
        
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<25; j++) {
            	queryNetwork(eval,i);
            }
        }
        
        return eval.calculateFitness();
	}

    private double CalcRealDistanceSquared(IntPair a, IntPair b)
    {
        double xdelta = (a.getX() - b.getX()) * pixelSize;
        double ydelta = (a.getY() - b.getY()) * pixelSize;
        return xdelta*xdelta + ydelta*ydelta;
    }

	@Override
	public boolean shouldMinimize() {
		return false;
	}

	@Override
	public boolean requireSingleThreaded() {
		return false;
	}

}
