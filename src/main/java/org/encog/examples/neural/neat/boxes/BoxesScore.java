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
            	
                eval.accumulate(calcRealDistanceSquared(targetPos, actualPos),
                		Math.max(0.0, eval.getMaxActivation() - eval.getMinActivation()));
            }
        }
        
        return eval.calculateFitness();
	}

    private double calcRealDistanceSquared(IntPair a, IntPair b)
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
