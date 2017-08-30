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
package org.encog.examples.neural.gui.som;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;

/**
 * A classic SOM example that shows how the SOM groups similar color shades.
 *
 */
public class SomColors extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6762179069967224817L;
	private MapPanel map;
	private SOM network;
	private Thread thread;
	private BasicTrainSOM train;
	private NeighborhoodRBF gaussian;

	public SomColors() {
		this.setSize(640, 480);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.network = createNetwork();
		this.getContentPane().add(map = new MapPanel(this));
		this.gaussian = new NeighborhoodRBF(RBFEnum.Gaussian,MapPanel.WIDTH,
				MapPanel.HEIGHT);
		this.train = new BasicTrainSOM(this.network, 0.01, null, gaussian);
		train.setForceWinner(false);
		this.thread = new Thread(this);
		thread.start();
	}

	public SOM getNetwork() {
		return this.network;
	}

	private SOM createNetwork() {
		SOM result = new SOM(3,MapPanel.WIDTH * MapPanel.HEIGHT);
		result.reset();
		return result;
	}

	public static void main(String[] args) {
		SomColors frame = new SomColors();
		frame.setVisible(true);
	}

	public void run() {

		List<MLData> samples = new ArrayList<MLData>();
		for (int i = 0; i < 15; i++) {
			MLData data = new BasicMLData(3);
			data.setData(0, RangeRandomizer.randomize(-1, 1));
			data.setData(1, RangeRandomizer.randomize(-1, 1));
			data.setData(2, RangeRandomizer.randomize(-1, 1));
			samples.add(data);
		}

		this.train.setAutoDecay(1000, 0.8, 0.003, 30, 5);

		for (int i = 0; i < 1000; i++) {
			int idx = (int) (Math.random() * samples.size());
			MLData c = samples.get(idx);

			this.train.trainPattern(c);
			this.train.autoDecay();
			this.map.repaint();
			System.out.println("Iteration " + i + "," + this.train.toString());
		}
	}
}
