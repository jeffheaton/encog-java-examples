/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
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
 */
package org.encog.examples.neural.gui.som;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.synapse.Synapse;

public class MapPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7528474872067939033L;
	public static final int CELL_SIZE = 8;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;

	private Synapse synapse;
	
	public MapPanel(SomColors som)
	{
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
	
	@Override
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
