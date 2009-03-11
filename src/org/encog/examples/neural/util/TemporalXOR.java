package org.encog.examples.neural.util;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;

public class TemporalXOR {
	
	private double[][] input;
	private double[][] ideal;

	public double xor(final double a,final double b)
	{
		boolean aa = a>0;
		boolean bb = b>0;
		
		boolean result = aa ^ bb;
		
		if( result )
			return 1.0;
		else
			return 0.0;
	}
	
	public int generateTrainingPart(final int index)
	{
		int currentIndex = index;
		
		for(int i=0;i<XOR.XOR_INPUT.length;i++)
		{
			double value;
				
			// part 1
			value = XOR.XOR_INPUT[i][0];
			if( currentIndex>0 )
			this.input[currentIndex][0] = value;
			this.ideal[currentIndex+1][0] = value;
			currentIndex++;
			
			// part 2
			value = XOR.XOR_INPUT[i][1];
			this.input[currentIndex][0] = XOR.XOR_INPUT[i][1];
			this.ideal[currentIndex+1][0] = value;
			currentIndex++;
			
			// part 3
			value = xor(XOR.XOR_INPUT[i][0],XOR.XOR_INPUT[i][1]);
			this.input[currentIndex][0] = value; 
			this.ideal[currentIndex+1][0] = value;
			currentIndex++;
			
		}
		
		return currentIndex;
	}
	
	public double[][] getInput() {
		return input;
	}

	public double[][] getIdeal() {
		return ideal;
	}
	
	public NeuralDataSet generate(int count)
	{
		this.input = new double[count*3][1];
		this.ideal = new double[count*3][1];
		int index = -1;
		for(int i=0;i<count;i++)
		{
			index = generateTrainingPart(index);
		}
		NeuralDataSet result = new BasicNeuralDataSet(this.input,this.ideal);
		return result;
	}
	
	public static void main(String args[])
	{
		TemporalXOR xor = new TemporalXOR();
		NeuralDataSet set = xor.generate(1000);
		System.out.println(set);
	}
	
}
