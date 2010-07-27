package org.encog.examples.neural.gui.predict;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JPanel;

import org.encog.engine.util.Format;
import org.encog.mathutil.EncogMath;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;

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
		NeuralData input = new BasicNeuralData(PredictSIN.INPUT_WINDOW);
		if( angle< PredictSIN.INPUT_WINDOW )
			return this.predict[angle];
		
		int index = angle - PredictSIN.INPUT_WINDOW;
		for(int i=0;i<PredictSIN.INPUT_WINDOW;i++)
		{
			input.setData(i,this.actual[index++]);
		}
		
		NeuralData output = this.network.compute(input);
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
