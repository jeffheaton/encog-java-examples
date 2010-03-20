package org.encog.examples.neural.gui.predict;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.temporal.TemporalDataDescription;
import org.encog.neural.data.temporal.TemporalNeuralDataSet;
import org.encog.neural.data.temporal.TemporalPoint;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.simple.EncogUtility;

public class PredictSIN extends JFrame implements ActionListener {

	public final static int INPUT_WINDOW = 5;
	public final static int PREDICT_WINDOW = 1;
	private BasicNetwork network;
	private GraphPanel graph;
	private NeuralDataSet trainingData;
	private Train train;
	private JButton btnTrain;
	
	public PredictSIN()
	{
		this.setTitle("SIN Wave Predict");
		this.setSize(640, 480);
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(graph = new GraphPanel(), BorderLayout.CENTER);
		
		network = EncogUtility.simpleFeedForward(INPUT_WINDOW, PREDICT_WINDOW*2, 0, 1, true);
		network.reset();
		graph.setNetwork(network);
		
		this.trainingData = generateTraining();
		this.train = new ResilientPropagation(this.network,this.trainingData);
		btnTrain = new JButton("Train");
		this.btnTrain.addActionListener(this);
		content.add(btnTrain,BorderLayout.SOUTH);
		graph.setError(network.calculateError(this.trainingData));
	}
	
	public void performTraining()
	{
		for(int i=0;i<10;i++) {
			this.train.iteration();
		}
		graph.setError(train.getError());
			
	}
	
	public NeuralDataSet generateTraining()
	{
		TemporalNeuralDataSet result = new TemporalNeuralDataSet(INPUT_WINDOW,PREDICT_WINDOW);
		
		TemporalDataDescription desc = new TemporalDataDescription(
				TemporalDataDescription.Type.RAW,true,true);
		result.addDescription(desc);
		
		for(int i = 0;i<360;i++)
		{
			TemporalPoint point = new TemporalPoint(1);
			point.setSequence(i);
			point.setData(0, GraphPanel.obtainActual(i));
			result.getPoints().add(point);
		}
		
		result.generate();
		return result;
	}
	

	
	public static void main(String[] args)
	{
		PredictSIN program = new PredictSIN();
		program.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		performTraining();
		this.graph.refresh();
	}
	
}
