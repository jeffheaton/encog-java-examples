package org.encog.examples.neural.gui.hopfield;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.encog.engine.util.EngineArray;
import org.encog.examples.neural.gui.predict.GraphPanel;
import org.encog.examples.neural.gui.predict.PredictSIN;
import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.thermal.HopfieldNetwork;
import org.encog.util.simple.EncogUtility;

public class HopfieldPattern extends JFrame  implements ActionListener, MouseListener {

	public HopfieldPattern()
	{
		this.setTitle("Hopfield Pattern");
		this.setSize(640, 480);
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		
		this.grid = new boolean[this.GRID_X * this.GRID_Y];
		this.addMouseListener(this);
		this.buttonTrain = new JButton("Train");
		this.buttonGo = new JButton("Go");
		this.buttonClear = new JButton("Clear");
		this.buttonClearMatrix = new JButton("Clear Matrix");
		this.setLayout(new BorderLayout());
		this.buttonPanel = new JPanel();
		this.buttonPanel.add(this.buttonTrain);
		this.buttonPanel.add(this.buttonGo);
		this.buttonPanel.add(this.buttonClear);
		this.buttonPanel.add(this.buttonClearMatrix);
		this.add(this.buttonPanel, BorderLayout.SOUTH);

		this.buttonTrain.addActionListener(this);
		this.buttonGo.addActionListener(this);
		this.buttonClear.addActionListener(this);
		this.buttonClearMatrix.addActionListener(this);

		this.hopfield = new HopfieldNetwork(this.GRID_X * this.GRID_Y);

		
	}
	
	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = 1882626251007826502L;
	public final int GRID_X = 8;
	public final int GRID_Y = 8;
	public final int CELL_WIDTH = 20;
	public final int CELL_HEIGHT = 20;
	public HopfieldNetwork hopfield;
	private boolean grid[];
	private int margin;
	private JPanel buttonPanel;
	private JButton buttonTrain;
	private JButton buttonGo;
	private JButton buttonClear;
	private JButton buttonClearMatrix;

	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == this.buttonClear) {
			clear();
		} else if (e.getSource() == this.buttonClearMatrix) {
			clearMatrix();
		} else if (e.getSource() == this.buttonGo) {
			go();
		} else if (e.getSource() == this.buttonTrain) {
			train();
		}
	}

	/**
	 * Clear the grid.
	 */
	public void clear() {
		int index = 0;
		for (int y = 0; y < this.GRID_Y; y++) {
			for (int x = 0; x < this.GRID_X; x++) {
				this.grid[index++] = false;
			}
		}

		repaint();
	}

	/**
	 * Clear the weight matrix.
	 */
	private void clearMatrix() {
		EngineArray.fill(this.hopfield.getWeights(),0);
	}

	/**
	 * Run the neural network.
	 */
	public void go() {
		for(int i=0;i<this.grid.length;i++) {
			this.hopfield.getCurrentState().setData(i, grid[i]);	
		}
		
		this.hopfield.run();
		
		for(int i=0;i<this.grid.length;i++) {
			grid[i] = this.hopfield.getCurrentState().getBoolean(i);	
		}
		repaint();

	}

	public void mouseReleased(final MouseEvent e) {
		final int x = ((e.getX() - this.margin) / this.CELL_WIDTH);
		final int y = e.getY() / this.CELL_HEIGHT;
		if (((x >= 0) && (x < this.GRID_X)) && ((y >= 0) && (y < this.GRID_Y))) {
			final int index = (y * this.GRID_X) + x;
			this.grid[index] = !this.grid[index];
		}
		repaint();

	}

	@Override
	public void paint(final Graphics g) {
		this.margin = (this.getWidth() - (this.CELL_WIDTH * this.GRID_X)) / 2;
		int index = 0;
		for (int y = 0; y < this.GRID_Y; y++) {
			for (int x = 0; x < this.GRID_X; x++) {
				if (this.grid[index++]) {
					g.fillRect(this.margin + (x * this.CELL_WIDTH), y
							* this.CELL_HEIGHT, this.CELL_WIDTH,
							this.CELL_HEIGHT);
				} else {
					g.drawRect(this.margin + (x * this.CELL_WIDTH), y
							* this.CELL_HEIGHT, this.CELL_WIDTH,
							this.CELL_HEIGHT);
				}
			}
		}
	}

	/**
	 * Train the neural network.
	 */
	public void train() {
		BiPolarNeuralData pattern = new BiPolarNeuralData(this.grid.length);
		
		for(int i=0;i<this.grid.length;i++) {
			pattern.setData(i, grid[i]);	
		}

		this.hopfield.addPattern(pattern);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public static void main(String[] args)
	{
		HopfieldPattern program = new HopfieldPattern();
		program.setVisible(true);
	}
}
