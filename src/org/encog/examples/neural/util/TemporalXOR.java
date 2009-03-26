package org.encog.examples.neural.util;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;

public class TemporalXOR {
	
	/*
	 * 1 xor 0 = 1
	 * 0 xor 0 = 0
	 * 0 xor 1 = 1
	 * 1 xor 1 = 0
	 */
	public static final double[] SEQUENCE = { 1.0,0.0,1.0,
		0.0,0.0,0.0,
		0.0,1.0,1.0,
		1.0,1.0,0.0 };

	private double[][] input;
	private double[][] ideal;
		
	public NeuralDataSet generate(int count)
	{
		this.input = new double[count][1];
		this.ideal = new double[count][1];
		
		for(int i=0;i<this.input.length;i++)
		{
			this.input[i][0] = SEQUENCE[i%SEQUENCE.length];
			this.ideal[i][0] = SEQUENCE[(i+1)%SEQUENCE.length];
		}
		
		return new BasicNeuralDataSet(this.input,this.ideal);
	}
}
