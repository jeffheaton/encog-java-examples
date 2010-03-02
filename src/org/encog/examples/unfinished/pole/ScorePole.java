/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
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
