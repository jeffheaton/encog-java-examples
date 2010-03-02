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
