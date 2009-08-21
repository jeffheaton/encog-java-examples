package org.encog.examples.neural.benchmark;

import org.encog.matrix.Matrix;
import org.encog.matrix.MatrixMath;

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
		Matrix c = MatrixMath.multiply(a, b);
		stop = System.currentTimeMillis();
		System.out.println("Multiply matrix: " + ((double)(stop-start))/1000.0 );
	}
}
