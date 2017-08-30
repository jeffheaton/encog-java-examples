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
