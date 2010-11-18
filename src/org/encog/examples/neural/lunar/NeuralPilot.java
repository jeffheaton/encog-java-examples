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
package org.encog.examples.neural.lunar;

import org.encog.neural.data.NeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.normalize.DataNormalization;
import org.encog.normalize.input.BasicInputField;
import org.encog.normalize.input.InputField;
import org.encog.normalize.output.OutputFieldRangeMapped;

public class NeuralPilot {
	
	private BasicNetwork network;
	private DataNormalization norm;
	private boolean track;
	
	public NeuralPilot(BasicNetwork network, boolean track)
	{
		InputField fuelIN;
		InputField altitudeIN;
		InputField velocityIN;
	
		this.track = track;
		this.network = network;
		
		norm = new DataNormalization();
		norm.addInputField(fuelIN = new BasicInputField());
		norm.addInputField(altitudeIN = new BasicInputField());
		norm.addInputField(velocityIN = new BasicInputField());
		norm.addOutputField(new OutputFieldRangeMapped(fuelIN,-0.9,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(altitudeIN,-0.9,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(velocityIN,-0.9,0.9));
		fuelIN.setMax(200);
		fuelIN.setMin(0);
		altitudeIN.setMax(10000);
		altitudeIN.setMin(0);
		velocityIN.setMin(-LanderSimulator.TERMINAL_VELOCITY);
		velocityIN.setMax(LanderSimulator.TERMINAL_VELOCITY);

	}
	
	public int scorePilot()
	{
		LanderSimulator sim = new LanderSimulator();
		while(sim.flying())
		{
			double[] data = new double[3];
			data[0] = sim.getFuel();
			data[1] = sim.getAltitude();
			data[2] = sim.getVelocity();
			
			NeuralData input = this.norm.buildForNetworkInput(data);
			NeuralData output = this.network.compute(input);
			double value = output.getData(0);
			
			boolean thrust;
			
			if( value > 0 )
			{
				thrust = true;
				if( track )
					System.out.println("THRUST");
			}
			else
				thrust = false;
			
			sim.turn(thrust);
			if( track )
				System.out.println(sim.telemetry());
		}
		return(sim.score());
	}
}
