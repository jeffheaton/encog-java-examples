package org.encog.examples.neural.pole;

import org.encog.neural.networks.BasicNetwork;
import org.encog.normalize.DataNormalization;

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
		DataNormalization norm = new DataNormalization();
	}
	
	public int scorePole(BasicNetwork network, PoleSimulator pole)
	{
		int result = 0;
		while(pole.balanced())
		{
			result++;
			pole.simulate();
		}
		return result;
	}
}
