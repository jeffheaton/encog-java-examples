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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.arrayutil.TemporalWindowArray;
import org.encog.util.simple.EncogUtility;

/**
 * Really simple GUI application that is used to predict the SIN wave.
 */
public class PredictSIN extends JFrame implements ActionListener {

	public final static int INPUT_WINDOW = 5;
	public final static int PREDICT_WINDOW = 1;
	private BasicNetwork network;
	private GraphPanel graph;
	private MLDataSet trainingData;
	private MLTrain train;
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
	
	public MLDataSet generateTraining()
	{
		TemporalWindowArray temp = new TemporalWindowArray(INPUT_WINDOW,PREDICT_WINDOW);
		
		double[] a = new double[360];
		
		for(int i = 0;i<360;i++)
		{
			a[i] = GraphPanel.obtainActual(i);
		}
		
		temp.analyze(a);
		return temp.process(a);
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
