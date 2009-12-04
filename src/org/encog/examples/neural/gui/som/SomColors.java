package org.encog.examples.neural.gui.som;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodGaussianMulti;
import org.encog.neural.pattern.SOMPattern;
import org.encog.util.randomize.RangeRandomizer;

public class SomColors extends JFrame  implements Runnable {
	
	private MapPanel map;
	private BasicNetwork network;
	private Thread thread;
	private CompetitiveTraining train;
	private NeighborhoodGaussianMulti gaussian;
	
	public SomColors()
	{
		this.setSize(640,480);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.network = createNetwork();
		this.getContentPane().add(map = new MapPanel(this));
		this.gaussian = new NeighborhoodGaussianMulti(MapPanel.WIDTH,MapPanel.HEIGHT,1,5,0);
		this.train = new CompetitiveTraining(this.network,0.01,null,gaussian);
		train.setForceWinner(false);
		this.thread = new Thread(this);
		thread.start();
	}
	
	public BasicNetwork getNetwork()
	{
		return this.network;
	}
	
	private BasicNetwork createNetwork()
	{
		BasicNetwork result = new BasicNetwork();
		SOMPattern pattern = new SOMPattern();
		pattern.setInputNeurons(3);
		pattern.setOutputNeurons(MapPanel.WIDTH*MapPanel.HEIGHT);
		result = pattern.generate();
		result.reset();
		return result;
	}
	
	public static void main(String[] args)
	{
		SomColors frame = new SomColors();
		frame.setVisible(true);
	}

	public void run() {
		
		List<NeuralData> samples = new ArrayList<NeuralData>();
		for(int i=0;i<15;i++)
		{
			NeuralData data = new BasicNeuralData(3);
			data.setData(0, RangeRandomizer.randomize(-1,1));
			data.setData(1, RangeRandomizer.randomize(-1,1));
			data.setData(2, RangeRandomizer.randomize(-1,1));
			samples.add(data);
		}
		
		this.train.setAutoDecay(1000, 0.8, 0.003, 30, 5);
		
		for(int i=0;i<1000;i++)
		{
			int idx = (int)(Math.random()*samples.size());
			NeuralData c = samples.get(idx);
			
			this.train.trainPattern(c);
			this.train.autoDecay();
			this.map.repaint();
			System.out.println("Iteration " + i + ","+ this.train.toString());
		}
	}
}
