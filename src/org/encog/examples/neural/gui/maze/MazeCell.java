package org.encog.examples.neural.gui.maze;

/**
 * Used internally by the maze generator.
 */
public class MazeCell {
	public MazeCell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x, y;
}