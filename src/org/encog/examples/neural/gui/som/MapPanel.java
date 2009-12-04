package org.encog.examples.neural.gui.som;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.synapse.Synapse;

public class MapPanel extends JPanel {
	
	public static final int CELL_SIZE = 8;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;

	private SomColors som;
	private Synapse synapse;
	private double minimum;
	private double maximum;
	
	public MapPanel(SomColors som)
	{
		this.som = som;
		this.synapse = som.getNetwork().getLayer(BasicNetwork.TAG_INPUT).getNext().get(0);
	}
	
	private int convertColor(double d)
	{
		//System.out.println(d);
		double result = 128*d;
		result+=128;
		result = Math.min(result, 255);
		result = Math.max(result, 0);
		return (int)result;
	}
	
	public void paint(Graphics g)
	{
		for(int y = 0; y< HEIGHT; y++)
		{
			for(int x = 0; x< WIDTH; x++)
			{
				int index = (y*WIDTH)+x;
				int red = convertColor(this.synapse.getMatrix().get(0, index));
				int green = convertColor(this.synapse.getMatrix().get(1, index));
				int blue = convertColor(this.synapse.getMatrix().get(2, index));
				g.setColor(new Color(red,green,blue));
				g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
	}	
}
