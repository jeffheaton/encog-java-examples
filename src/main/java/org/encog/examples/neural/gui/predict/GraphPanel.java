/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
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
package org.encog.examples.neural.gui.predict;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import org.encog.mathutil.EncogMath;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.Format;

public class GraphPanel extends JPanel implements ComponentListener {

	private BasicNetwork network;
	private double[] actual;
	private double[] predict;
	private int width;
	private int height;
	private int center;
	private double error;

	public GraphPanel() {
		this.addComponentListener(this);
	}

	public static double obtainActual(int angle) {
		angle %= 360;
		double rad = EncogMath.deg2rad(angle * 2);
		return Math.sin(rad);
	}
	
	public double obtainPrediction(int angle)
	{
		MLData input = new BasicMLData(PredictSIN.INPUT_WINDOW);
		if( angle< PredictSIN.INPUT_WINDOW )
			return this.predict[angle];
		
		int index = angle - PredictSIN.INPUT_WINDOW;
		for(int i=0;i<PredictSIN.INPUT_WINDOW;i++)
		{
			input.setData(i,this.actual[index++]);
		}
		
		MLData output = this.network.compute(input);
		return output.getData(0);
		
		
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		// actual
		g.setColor(Color.black);
		graphArray(g,this.actual);
		
		// predict
		g.setColor(Color.blue);
		graphArray(g,this.predict);
		
		g.setColor(Color.black);
		g.drawString("Current error:" + Format.formatPercent(this.error) + ",black = actual, blue= predict", 10, 10);
	}
	
	private void graphArray(Graphics g, double[] array)
	{
		int x = 0;
		int y = center;
		
		for (int i = 0; i < width; i++) {
			int x2 = i;
			int y2 = center + (int) (center * array[i]);

			g.drawLine(x, y, x2, y2);
			x = x2;
			y = y2;
		}
	}

	public BasicNetwork getNetwork() {
		return network;
	}

	public void setNetwork(BasicNetwork network) {
		this.network = network;
	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void refresh()
	{
		this.height = getHeight();
		this.width = getWidth();
		this.center = this.height / 2;

		this.actual = new double[width];
		this.predict = new double[width];

		for (int i = 0; i < width; i++) {
			this.actual[i] = this.obtainActual(i);
			if( i<PredictSIN.INPUT_WINDOW)
				this.predict[i] = this.obtainActual(i);
			else
			{
				double predict = this.obtainPrediction(i);
				this.predict[i] = predict;
			}
		}
		repaint();
	}

	public void componentResized(ComponentEvent e) {
		refresh();

	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}
	
	

}
