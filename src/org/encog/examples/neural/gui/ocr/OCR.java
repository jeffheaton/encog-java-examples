/*
 * Encog Artificial Intelligence Framework v2.x
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
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

package org.encog.examples.neural.gui.ocr;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.encog.neural.activation.ActivationLinear;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataPair;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodGaussian;
import org.encog.util.logging.Logging;
import org.encog.util.math.rbf.GaussianFunction;

/**
 * OCR: Main form that allows the user to interact with the OCR application.
 */
public class OCR extends JFrame implements Runnable {

	class SymAction implements java.awt.event.ActionListener {
		public void actionPerformed(final java.awt.event.ActionEvent event) {
			final Object object = event.getSource();
			if (object == OCR.this.downSample) {
				downSample_actionPerformed(event);
			} else if (object == OCR.this.clear) {
				clear_actionPerformed(event);
			} else if (object == OCR.this.add) {
				add_actionPerformed(event);
			} else if (object == OCR.this.del) {
				del_actionPerformed(event);
			} else if (object == OCR.this.load) {
				load_actionPerformed(event);
			} else if (object == OCR.this.save) {
				save_actionPerformed(event);
			} else if (object == OCR.this.train) {
				train_actionPerformed(event);
			} else if (object == OCR.this.recognize) {
				recognize_actionPerformed(event);
			}
		}
	}

	class SymListSelection implements javax.swing.event.ListSelectionListener {
		public void valueChanged(
				final javax.swing.event.ListSelectionEvent event) {
			final Object object = event.getSource();
			if (object == OCR.this.letters) {
				letters_valueChanged(event);
			}
		}
	}

	public class UpdateStats implements Runnable {
		long tries;
		double error;

		public void run() {
			OCR.this.tries.setText("" + this.tries);
			OCR.this.txtError.setText(""
					+ OCR.this.numberFormat.format(this.error));
		}
	}

	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = -6779380961875907013L;

	/**
	 * The downsample width for the application.
	 */
	static final int DOWNSAMPLE_WIDTH = 5;

	/**
	 * The down sample height for the application.
	 */
	static final int DOWNSAMPLE_HEIGHT = 7;

	static final double MAX_ERROR = 0.01;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            Args not really used.
	 */
	public static void main(final String args[]) {
		Logging.stopConsoleLogging();
		(new OCR()).setVisible(true);
	}

	private final NumberFormat numberFormat;

	private boolean halt;

	/**
	 * The entry component for the user to draw into.
	 */
	private final Entry entry;

	/**
	 * The down sample component to display the drawing downsampled.
	 */
	private final Sample sample;

	/**
	 * The letters that have been defined.
	 */
	private final DefaultListModel letterListModel = new DefaultListModel();

	/**
	 * The neural network.
	 */
	private BasicNetwork net;

	/**
	 * The background thread used for training.
	 */
	private Thread trainThread = null;

	private final JLabel JLabel1 = new javax.swing.JLabel();

	private final JLabel JLabel2 = new javax.swing.JLabel();

	/**
	 * THe downsample button.
	 */
	private final JButton downSample = new JButton();

	/**
	 * The add button.
	 */
	private final JButton add = new JButton();
	/**
	 * The clear button
	 */
	private final JButton clear = new JButton();

	/**
	 * The recognize button
	 */
	private final JButton recognize = new JButton();

	private final JScrollPane JScrollPane1 = new JScrollPane();

	/**
	 * The letters list box
	 */
	private final JList letters = new JList();

	/**
	 * The delete button
	 */
	private final JButton del = new JButton();

	/**
	 * The load button
	 */
	private final JButton load = new JButton();

	/**
	 * The save button
	 */
	private final JButton save = new JButton();

	/**
	 * The train button
	 */
	JButton train = new JButton();

	JLabel JLabel3 = new JLabel();

	JLabel JLabel4 = new JLabel();

	/**
	 * How many tries
	 */
	JLabel tries = new JLabel();
	/**
	 * The last error
	 */
	JLabel txtError = new JLabel();

	JLabel JLabel8 = new JLabel();

	JLabel JLabel5 = new JLabel();

	/**
	 * The constructor.
	 */
	OCR() {
		getContentPane().setLayout(null);
		this.entry = new Entry();
		this.entry.setLocation(168, 25);
		this.entry.setSize(200, 128);
		getContentPane().add(this.entry);

		this.sample = new Sample(OCR.DOWNSAMPLE_WIDTH, OCR.DOWNSAMPLE_HEIGHT);
		this.sample.setLocation(307, 210);
		this.sample.setSize(65, 70);

		this.entry.setSample(this.sample);
		getContentPane().add(this.sample);

		setTitle("Java Neural Network");
		getContentPane().setLayout(null);
		setSize(405, 382);
		setVisible(false);
		this.JLabel1.setText("Letters Known");
		getContentPane().add(this.JLabel1);
		this.JLabel1.setBounds(12, 12, 100, 12);
		this.JLabel2.setText("Tries:");
		getContentPane().add(this.JLabel2);
		this.JLabel2.setBounds(12, 264, 72, 24);
		this.downSample.setText("D Sample");
		this.downSample.setActionCommand("Down Sample");
		getContentPane().add(this.downSample);
		this.downSample.setBounds(252, 180, 120, 24);
		this.add.setText("Add");
		this.add.setActionCommand("Add");
		getContentPane().add(this.add);
		this.add.setBounds(168, 156, 84, 24);
		this.clear.setText("Clear");
		this.clear.setActionCommand("Clear");
		getContentPane().add(this.clear);
		this.clear.setBounds(168, 180, 84, 24);
		this.recognize.setText("Recognize");
		this.recognize.setActionCommand("Recognize");
		getContentPane().add(this.recognize);
		this.recognize.setBounds(252, 156, 120, 24);
		this.JScrollPane1
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.JScrollPane1.setOpaque(true);
		getContentPane().add(this.JScrollPane1);
		this.JScrollPane1.setBounds(12, 24, 144, 132);
		this.JScrollPane1.getViewport().add(this.letters);
		this.letters.setBounds(0, 0, 126, 129);
		this.del.setText("Delete");
		this.del.setActionCommand("Delete");
		getContentPane().add(this.del);
		this.del.setBounds(12, 156, 144, 24);
		this.load.setText("Load");
		this.load.setActionCommand("Load");
		getContentPane().add(this.load);
		this.load.setBounds(12, 180, 75, 24);
		this.save.setText("Save");
		this.save.setActionCommand("Save");
		getContentPane().add(this.save);
		this.save.setBounds(84, 180, 72, 24);
		this.train.setText("Begin Training");
		this.train.setActionCommand("Begin Training");
		getContentPane().add(this.train);
		this.train.setBounds(12, 204, 144, 24);
		this.JLabel3.setText("Error:");
		getContentPane().add(this.JLabel3);
		this.JLabel3.setBounds(12, 288, 72, 24);
		this.tries.setText("0");
		getContentPane().add(this.tries);
		this.tries.setBounds(96, 264, 72, 24);
		this.txtError.setText("0");
		getContentPane().add(this.txtError);
		this.txtError.setBounds(96, 288, 72, 24);
		this.JLabel8.setHorizontalTextPosition(SwingConstants.CENTER);
		this.JLabel8.setHorizontalAlignment(SwingConstants.CENTER);
		this.JLabel8.setText("Training Results");
		getContentPane().add(this.JLabel8);
		this.JLabel8.setFont(new Font("Dialog", Font.BOLD, 14));
		this.JLabel8.setBounds(12, 240, 120, 24);
		this.JLabel5.setText("Draw Letters Here");
		getContentPane().add(this.JLabel5);
		this.JLabel5.setBounds(204, 12, 144, 12);

		final SymAction lSymAction = new SymAction();
		this.downSample.addActionListener(lSymAction);
		this.clear.addActionListener(lSymAction);
		this.add.addActionListener(lSymAction);
		this.del.addActionListener(lSymAction);
		final SymListSelection lSymListSelection = new SymListSelection();
		this.letters.addListSelectionListener(lSymListSelection);
		this.load.addActionListener(lSymAction);
		this.save.addActionListener(lSymAction);
		this.train.addActionListener(lSymAction);
		this.recognize.addActionListener(lSymAction);
		this.letters.setModel(this.letterListModel);

		this.numberFormat = NumberFormat.getNumberInstance();
	}

	/**
	 * Called to add the current image to the training set
	 * 
	 * @param event
	 *            The event
	 */
	@SuppressWarnings("unchecked")
	void add_actionPerformed(final java.awt.event.ActionEvent event) {
		int i;

		final String letter = JOptionPane
				.showInputDialog("Please enter a letter you would like to assign this sample to.");
		if (letter == null) {
			return;
		}

		if (letter.length() > 1) {
			JOptionPane.showMessageDialog(this,
					"Please enter only a single letter.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.entry.downSample();
		final SampleData sampleData = (SampleData) this.sample.getData()
				.clone();
		sampleData.setLetter(letter.charAt(0));

		for (i = 0; i < this.letterListModel.size(); i++) {
			final Comparable str = (Comparable) this.letterListModel
					.getElementAt(i);
			if (str.equals(letter)) {
				JOptionPane.showMessageDialog(this,
						"That letter is already defined, delete it first!",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (str.compareTo(sampleData) > 0) {
				this.letterListModel.add(i, sampleData);
				return;
			}
		}
		this.letterListModel.add(this.letterListModel.size(), sampleData);
		this.letters.setSelectedIndex(i);
		this.entry.clear();
		this.sample.repaint();

	}

	/**
	 * Called to clear the image.
	 * 
	 * @param event
	 *            The event
	 */
	void clear_actionPerformed(final java.awt.event.ActionEvent event) {
		this.entry.clear();
		this.sample.getData().clear();
		this.sample.repaint();

	}

	/**
	 * Called when the del button is pressed.
	 * 
	 * @param event
	 *            The event.
	 */
	void del_actionPerformed(final java.awt.event.ActionEvent event) {
		final int i = this.letters.getSelectedIndex();

		if (i == -1) {
			JOptionPane.showMessageDialog(this,
					"Please select a letter to delete.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.letterListModel.remove(i);
	}

	/**
	 * Called to downsample the image.
	 * 
	 * @param event
	 *            The event
	 */
	void downSample_actionPerformed(final java.awt.event.ActionEvent event) {
		this.entry.downSample();

	}

	/**
	 * Called when a letter is selected from the list box.
	 * 
	 * @param event
	 *            The event
	 */
	void letters_valueChanged(final ListSelectionEvent event) {
		if (this.letters.getSelectedIndex() == -1) {
			return;
		}
		final SampleData selected = (SampleData) this.letterListModel
				.getElementAt(this.letters.getSelectedIndex());
		this.sample.setData((SampleData) selected.clone());
		this.sample.repaint();
		this.entry.clear();

	}

	/**
	 * Called when the load button is pressed.
	 * 
	 * @param event
	 *            The event
	 */
	void load_actionPerformed(final java.awt.event.ActionEvent event) {
		try {
			FileReader f;// the actual file stream
			BufferedReader r;// used to read the file line by line

			f = new FileReader(new File("./sample.dat"));
			r = new BufferedReader(f);
			String line;
			int i = 0;

			this.letterListModel.clear();

			while ((line = r.readLine()) != null) {
				final SampleData ds = new SampleData(line.charAt(0),
						OCR.DOWNSAMPLE_WIDTH, OCR.DOWNSAMPLE_HEIGHT);
				this.letterListModel.add(i++, ds);
				int idx = 2;
				for (int y = 0; y < ds.getHeight(); y++) {
					for (int x = 0; x < ds.getWidth(); x++) {
						ds.setData(x, y, line.charAt(idx++) == '1');
					}
				}
			}

			r.close();
			f.close();
			clear_actionPerformed(null);
			JOptionPane.showMessageDialog(this, "Loaded from 'sample.dat'.",
					"Training", JOptionPane.PLAIN_MESSAGE);

		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: " + e, "Training",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Used to map neurons to actual letters.
	 * 
	 * @return The current mapping between neurons and letters as an array.
	 */
	char[] mapNeurons() {
		final char map[] = new char[this.letterListModel.size()];

		for (int i = 0; i < map.length; i++) {
			map[i] = '?';
		}
		for (int i = 0; i < this.letterListModel.size(); i++) {
			final NeuralData input = new BasicNeuralData(5 * 7);
			int idx = 0;
			final SampleData ds = (SampleData) this.letterListModel
					.getElementAt(i);
			for (int y = 0; y < ds.getHeight(); y++) {
				for (int x = 0; x < ds.getWidth(); x++) {
					input.setData(idx++, ds.getData(x, y) ? .5 : -.5);
				}
			}

			final int best = this.net.winner(input);
			map[best] = ds.getLetter();
		}
		return map;
	}

	public void markStopped() {
		this.trainThread = null;
		this.train.setText("Begin Training");
		JOptionPane.showMessageDialog(this, "Training has completed.",
				"Training", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Called when the recognize button is pressed.
	 * 
	 * @param event
	 *            The event.
	 */
	void recognize_actionPerformed(final java.awt.event.ActionEvent event) {
		if (this.net == null) {
			JOptionPane.showMessageDialog(this, "I need to be trained first!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.entry.downSample();

		final NeuralData input = new BasicNeuralData(5 * 7);
		int idx = 0;
		final SampleData ds = this.sample.getData();
		for (int y = 0; y < ds.getHeight(); y++) {
			for (int x = 0; x < ds.getWidth(); x++) {
				input.setData(idx++, ds.getData(x, y) ? .5 : -.5);
			}
		}

		final int best = this.net.winner(input);
		final char map[] = mapNeurons();
		JOptionPane
				.showMessageDialog(this, "  " + map[best] + "   (Neuron #"
						+ best + " fired)", "That Letter Is",
						JOptionPane.PLAIN_MESSAGE);
		clear_actionPerformed(null);

	}

	/**
	 * Run method for the background training thread.
	 */
	public void run() {
		try {
			final int inputNeuron = OCR.DOWNSAMPLE_HEIGHT
					* OCR.DOWNSAMPLE_WIDTH;
			final int outputNeuron = this.letterListModel.size();

			final NeuralDataSet trainingSet = new BasicNeuralDataSet();
			for (int t = 0; t < this.letterListModel.size(); t++) {
				final NeuralData item = new BasicNeuralData(inputNeuron);
				int idx = 0;
				final SampleData ds = (SampleData) this.letterListModel
						.getElementAt(t);
				for (int y = 0; y < ds.getHeight(); y++) {
					for (int x = 0; x < ds.getWidth(); x++) {
						item.setData(idx++, ds.getData(x, y) ? .5 : -.5);
					}
				}

				trainingSet.add(new BasicNeuralDataPair(item, null));
			}

			this.net = new BasicNetwork();
			this.net.addLayer(new BasicLayer(new ActivationLinear(), false,
					inputNeuron));
			this.net.addLayer(new BasicLayer(new ActivationLinear(), false,
					outputNeuron));
			this.net.getStructure().finalizeStructure();
			this.net.reset();

			final CompetitiveTraining train = new CompetitiveTraining(this.net,
					0.25, trainingSet, new NeighborhoodGaussian(
							new GaussianFunction(0, 1, 2)));

			int tries = 1;
			while (!this.halt) {
				train.iteration();
				update(tries++, train.getError());
			}

			markStopped();
			this.halt = false;

		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: " + e, "Training",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Called when the save button is clicked.
	 * 
	 * @param event
	 *            The event
	 */
	void save_actionPerformed(final java.awt.event.ActionEvent event) {
		try {
			OutputStream os;// the actual file stream
			PrintStream ps;// used to read the file line by line

			os = new FileOutputStream("./sample.dat", false);
			ps = new PrintStream(os);

			for (int i = 0; i < this.letterListModel.size(); i++) {
				final SampleData ds = (SampleData) this.letterListModel
						.elementAt(i);
				ps.print(ds.getLetter() + ":");
				for (int y = 0; y < ds.getHeight(); y++) {
					for (int x = 0; x < ds.getWidth(); x++) {
						ps.print(ds.getData(x, y) ? "1" : "0");
					}
				}
				ps.println("");
			}

			ps.close();
			os.close();
			clear_actionPerformed(null);
			JOptionPane.showMessageDialog(this, "Saved to 'sample.dat'.",
					"Training", JOptionPane.PLAIN_MESSAGE);

		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: " + e, "Training",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Called when the train button is pressed.
	 * 
	 * @param event
	 *            The event.
	 */
	void train_actionPerformed(final java.awt.event.ActionEvent event) {
		if (this.trainThread == null) {
			this.train.setText("Stop Training");
			this.train.repaint();
			this.trainThread = new Thread(this);
			this.trainThread.start();
		} else {
			this.halt = true;
		}
	}

	/**
	 * Called to update the stats, from the neural network.
	 * 
	 * @param trial
	 *            How many tries.
	 * @param error
	 *            The current error.
	 */
	public void update(final int retry, final double error) {

		final UpdateStats stats = new UpdateStats();
		stats.tries = retry;
		stats.error = error;

		try {
			SwingUtilities.invokeAndWait(stats);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e, "Training",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}