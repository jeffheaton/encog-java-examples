package org.encog.examples.neural.normalize;

import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

public class SimpleNormalize {
	public static void main(String[] args) {
		
		// Normalize values with an actual range of (0 to 100) to (-1 to 1)
		NormalizedField norm = new NormalizedField(NormalizationAction.Normalize, 
				null,100,0,1,-1);
		
		double x = 5;
		double y = norm.normalize(x);
		
		System.out.println( x + " normalized is " + y);
		
		double z = norm.deNormalize(y);
		
		System.out.println( y + " denormalized is " + z);
	}
}
