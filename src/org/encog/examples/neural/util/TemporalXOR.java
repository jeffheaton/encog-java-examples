package org.encog.examples.neural.util;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;

public class TemporalXOR {
	
	private double[][] input;
	private double[][] ideal;

	
	private void setInput(int index,double value)
	{
		if( index>=0 && index<this.input.length )
			this.input[index][0] = value;
	}
	
	private void setIdeal(int index,double value)
	{
		if( index>=0 && index<this.ideal.length )
			this.ideal[index][0] = value;
	}
	
	public int generateTrainingPart(final int index)
	{
		int currentIndex = index;
		
		for(int i=0;i<XOR.XOR_INPUT.length;i++)
		{
			double value;
				
			// part 1
			value = XOR.XOR_INPUT[i][0];
			setInput(currentIndex,value);
			setIdeal(currentIndex+1,value);
			currentIndex++;
			
			// part 2
			value = XOR.XOR_INPUT[i][1];
			setInput(currentIndex,value);
			setIdeal(currentIndex+1,value);
			currentIndex++;
			
			// part 3
			value = XOR.XOR_IDEAL[i][0];
			setInput(currentIndex,value); 
			setIdeal(currentIndex+1,value);
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
