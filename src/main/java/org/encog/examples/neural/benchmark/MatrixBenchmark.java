/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
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
package org.encog.examples.neural.benchmark;

import org.encog.mathutil.matrices.Matrix;
import org.encog.mathutil.matrices.MatrixMath;

public class MatrixBenchmark {
	
	public static Matrix generateRandomMatrix(int size)
	{
		Matrix result = new Matrix(size,size);
		for(int row=0;row<size;row++)
		{
			for(int col=0;col<size;col++)
			{
				result.set(row, col, Math.random()*100);
			}
		}
		return result;
	}
	
	public static void main(String args[])
	{
		long start,stop;
		
		start = System.currentTimeMillis();
		Matrix a = generateRandomMatrix(500);
		Matrix b = generateRandomMatrix(500);
		stop = System.currentTimeMillis();
		System.out.println("Setup matrix: " + ((double)(stop-start))/1000.0 );
		
		start = System.currentTimeMillis();
		MatrixMath.multiply(a, b);
		stop = System.currentTimeMillis();
		System.out.println("Multiply matrix: " + ((double)(stop-start))/1000.0 );
	}
}
