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
	
	@Override
	public double calculateScore(MLMethod phenotype) {
		BoxTrialCase test = new BoxTrialCase(new Random());
        TrialEvaluation eval = new TrialEvaluation(phenotype, test);
        
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<25; j++) {
            	IntPair targetPos = eval.getTest().initTestCase(i);
            	IntPair actualPos = eval.query(this.resolution);
            	
                eval.accumulate(CalcRealDistanceSquared(targetPos, actualPos),
                		Math.max(0.0, eval.getMaxActivation() - eval.getMinActivation()));
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
