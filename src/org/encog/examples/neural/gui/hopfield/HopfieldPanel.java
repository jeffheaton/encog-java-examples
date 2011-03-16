package org.encog.examples.neural.gui.hopfield;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import org.encog.engine.util.EngineArray;
import org.encog.neural.data.bipolar.BiPolarNeuralData;
import org.encog.neural.thermal.HopfieldNetwork;

public class HopfieldPanel extends JPanel implements MouseListener {

	public HopfieldNetwork hopfield;
	private boolean grid[];
	private int margin;
	
	private int gridX;
	private int gridY;
	private int cellWidth;
	private int cellHeight;
	
	public HopfieldPanel(int gridX,int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.grid = new boolean[this.gridX * this.gridY];
		this.addMouseListener(this);
		this.hopfield = new HopfieldNetwork(this.gridX * this.gridY);
	}
	
	/**
	 * Clear the grid.
	 */
	public void clear() {
		int index = 0;
		for (int y = 0; y < this.gridY; y++) {
			for (int x = 0; x < this.gridX; x++) {
				this.grid[index++] = false;
			}
		}

		repaint();
	}

	/**
	 * Clear the weight matrix.
	 */
	public void clearMatrix() {
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
		final int x = ((e.getX() - this.margin) / this.cellWidth);
		final int y = e.getY() / this.cellHeight;
		if (((x >= 0) && (x < this.gridY)) && ((y >= 0) && (y < this.gridY))) {
			final int index = (y * this.gridX) + x;
			this.grid[index] = !this.grid[index];
		}
		repaint();

	}

	@Override
	public void paint(final Graphics g) {
		int width = this.getWidth();
		int height = this.getHeight();
		this.cellHeight = height/this.gridY;
		this.cellWidth = width/this.gridX;
		
		g.setColor(Color.WHITE);
		g.fillRect(0,0, width, height);
		g.setColor(Color.BLACK);
		this.margin = (this.getWidth() - (this.cellWidth * this.gridX)) / 2;
		int index = 0;
		for (int y = 0; y < this.gridY; y++) {
			for (int x = 0; x < this.gridX; x++) {
				if (this.grid[index++]) {
					g.fillRect(this.margin + (x * this.cellWidth), y
							* this.cellHeight, this.cellWidth,
							this.cellHeight);
				} else {
					g.drawRect(this.margin + (x * this.cellWidth), y
							* this.cellHeight, this.cellWidth,
							this.cellHeight);
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


}
