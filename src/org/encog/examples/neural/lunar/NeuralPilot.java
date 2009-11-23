package org.encog.examples.neural.lunar;

import org.encog.neural.activation.ActivationTANH;
import org.encog.neural.data.NeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
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
		OutputFieldRangeMapped fuelOUT;
		OutputFieldRangeMapped altitudeOUT;
		OutputFieldRangeMapped velocityOUT;
	
		this.track = track;
		this.network = network;
		
		norm = new DataNormalization();
		norm.addInputField(fuelIN = new BasicInputField());
		norm.addInputField(altitudeIN = new BasicInputField());
		norm.addInputField(velocityIN = new BasicInputField());
		norm.addOutputField(fuelOUT = new OutputFieldRangeMapped(fuelIN,-0.9,0.9));
		norm.addOutputField(altitudeOUT = new OutputFieldRangeMapped(altitudeIN,-0.9,0.9));
		norm.addOutputField(velocityOUT = new OutputFieldRangeMapped(velocityIN,-0.9,0.9));
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
		return(sim.cost());
	}
	
	public static void main(String[] args)
	{
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(3);
		pattern.addHiddenLayer(10);
		pattern.setOutputNeurons(1);
		pattern.setActivationFunction(new ActivationTANH());
		BasicNetwork network = pattern.generate();
		network.reset();
		
		NeuralPilot pilot = new NeuralPilot(network, true);
		int score = pilot.scorePilot();
		System.out.println(pilot.scorePilot());
	}
}
