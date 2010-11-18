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
package org.encog.examples.unfinished.pole;

import org.encog.neural.data.NeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.normalize.DataNormalization;
import org.encog.normalize.input.BasicInputField;
import org.encog.normalize.input.InputField;
import org.encog.normalize.output.OutputField;
import org.encog.normalize.output.OutputFieldRangeMapped;

public class ScorePole {
	
	public int score(BasicNetwork network)
	{
		int result = 0;
		
		for(int i=0;i<10;i++)
		{
			PoleSimulator pole = new PoleSimulator();
			result+=scorePole(network, pole);
		}
		
		return result;
	}
	
	public void setForce(BasicNetwork network, PoleSimulator pole)
	{
		InputField inputAngle,inputAV;
		OutputField outputAngle,outputAV;
		
		DataNormalization norm = new DataNormalization();
		norm.addInputField(inputAngle = new BasicInputField());
		norm.addInputField(inputAV = new BasicInputField());
		norm.addOutputField(outputAngle = new OutputFieldRangeMapped(inputAngle,-0.9,0.9));
		norm.addOutputField(outputAV = new OutputFieldRangeMapped(inputAV,-0.9,0.9));
		inputAngle.setMax(360);
		inputAngle.setMin(-360);
		inputAV.setMin(-1000);
		inputAV.setMax(1000);
		
		double[] input = new double[2];
		input[0] = pole.getPoleAngle();
		input[1] = pole.getPoleAngleVelocity();
		
		NeuralData input2 = norm.buildForNetworkInput(input);
		NeuralData output = network.compute(input2);
		double force = output.getData(0)*10;
		pole.setForce(force);
	}
	
	public int scorePole(BasicNetwork network, PoleSimulator pole)
	{
		System.out.println("------");
		int result = 0;
		while(pole.balanced())
		{
			result++;
			setForce(network,pole);
			pole.simulate();
			System.out.println(pole.getPoleAngle()+","+pole.getForce());
		}
		return result;
	}
}
