/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
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
