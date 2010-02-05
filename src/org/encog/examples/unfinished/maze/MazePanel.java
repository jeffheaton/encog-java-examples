/*
 * Encog(tm) Artificial Intelligence Framework v2.3
 * Java Examples
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
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
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.unfinished.maze;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class MazePanel extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8695035618197614437L;
	protected int width;
	protected int height;
	protected int cellWidth;
	protected int cellHeight;
	protected int mouseX = 0;
	protected int mouseY = 0;
	private Maze maze;

	public MazePanel(Maze maze) {
		this.maze = maze;
	}



	public void paint(Graphics g) {
		render(g);
	}

	public void render(Graphics output) {
		int rows = maze.getRows();
		int columns = maze.getColumns();
		
		height = (int) this.getSize().getHeight();
		width = (int) this.getSize().getWidth();
		cellHeight = height / rows;
		cellWidth = width / maze.getColumns();

		Image offscreen = createImage(height, width);
		Graphics g = offscreen.getGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.red);
		g.fillRect(mouseX * cellWidth, mouseY * cellHeight, cellWidth,
				cellHeight);
		g.setColor(Color.black);

		for (int y = 0; y < rows; y++)
			for (int x = 0; x < columns; x++) {
				if (maze.isWall(x, y, Maze.NORTH))
					g.drawLine(x * cellWidth, y * cellHeight, (x + 1)
							* cellWidth, y * cellHeight);
				if (maze.isWall(x, y, Maze.SOUTH))
					g.drawLine(x * cellWidth, (y + 1) * cellHeight, (x + 1)
							* cellWidth, (y + 1) * cellHeight);
				if (maze.isWall(x, y, Maze.WEST))
					g.drawLine(x * cellWidth, y * cellHeight, x * cellWidth,
							(y + 1) * cellHeight);
				if (maze.isWall(x, y, Maze.EAST))
					g.drawLine((x + 1) * cellWidth, y * cellHeight, (x + 1)
							* cellWidth, (y + 1) * cellHeight);
			}
		output.drawImage(offscreen, 0, 0, this);
	}



	public int getMouseX() {
		return mouseX;
	}



	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}



	public int getMouseY() {
		return mouseY;
	}



	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}



}
