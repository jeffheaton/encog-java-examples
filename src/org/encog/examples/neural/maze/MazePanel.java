package org.encog.examples.neural.maze;

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
	protected int rows = 30;
	protected int columns = 30;
	protected int cellWidth;
	protected int cellHeight;
	protected boolean rowWalls[][];
	protected boolean columnWalls[][];

	public final static int NORTH = 1;
	public final static int SOUTH = 2;
	public final static int EAST = 3;
	public final static int WEST = 4;
	protected int mouseX = 0;
	protected int mouseY = 0;

	public MazePanel() {
		createBuffer();
		generateMaze();
	}

	public void createBuffer() {
		rowWalls = new boolean[rows][columns];
		columnWalls = new boolean[columns][rows];
		for (int x = 0; x < columns; x++)
			for (int y = 0; y < rows; y++) {
				rowWalls[y][x] = true;
				columnWalls[x][y] = true;
			}
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public boolean isBorder(int x, int y, int direction) {
		switch (direction) {
		case NORTH:
			if (y < 1)
				return true;
			break;
		case SOUTH:
			if (y > rows)
				return true;
			break;
		case EAST:
			if (x > columns)
				return true;
			break;
		case WEST:
			if (x < 1)
				return true;
			break;
		}
		return false;
	}

	public void setWall(int x, int y, int direction, boolean value) {
		// if(isBorder(x,y,direction))
		// return;
		switch (direction) {
		case NORTH:
			columnWalls[x][y - 1] = value;
			break;
		case SOUTH:
			columnWalls[x][y] = value;
			break;
		case EAST:
			rowWalls[y][x] = value;
			break;
		case WEST:
			rowWalls[y][x - 1] = value;
			break;
		}

	}

	public boolean isWall(int x, int y, int direction) {
		if (isBorder(x, y, direction))
			return true;
		switch (direction) {
		case NORTH:
			return (columnWalls[x][y - 1]);
		case SOUTH:
			return (columnWalls[x][y]);
		case EAST:
			return (rowWalls[y][x]);
		case WEST:
			return (rowWalls[y][x - 1]);
		}
		return false;
	}

	public boolean inTact(int x, int y) {
		if ((x < 0) || (y < 0) || (x >= columns) || (y >= rows))
			return false;
		if (!isWall(x, y, NORTH))
			return false;
		if (!isWall(x, y, SOUTH))
			return false;
		if (!isWall(x, y, EAST))
			return false;
		if (!isWall(x, y, WEST))
			return false;
		return true;
	}

	public void paint(Graphics g) {
		render(g);
	}

	public void render(Graphics output) {
		height = (int) this.getSize().getHeight();
		width = (int) this.getSize().getWidth();
		cellHeight = height / rows;
		cellWidth = width / columns;

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
				if (isWall(x, y, NORTH))
					g.drawLine(x * cellWidth, y * cellHeight, (x + 1)
							* cellWidth, y * cellHeight);
				if (isWall(x, y, SOUTH))
					g.drawLine(x * cellWidth, (y + 1) * cellHeight, (x + 1)
							* cellWidth, (y + 1) * cellHeight);
				if (isWall(x, y, WEST))
					g.drawLine(x * cellWidth, y * cellHeight, x * cellWidth,
							(y + 1) * cellHeight);
				if (isWall(x, y, EAST))
					g.drawLine((x + 1) * cellWidth, y * cellHeight, (x + 1)
							* cellWidth, (y + 1) * cellHeight);
			}
		output.drawImage(offscreen, 0, 0, this);
	}

	public static int getRandom(int range) {
		return ((int) (Math.random() * range));
	}

	public void generateMaze() {
		MazeCell currentCell = new MazeCell(getRandom(columns), getRandom(rows));
		int totalCells = rows * columns;
		int visitedCells = 1;
		Vector<MazeCell> cellStack = new Vector<MazeCell>();
		while (visitedCells < totalCells) {
			Vector<Integer> neighbors = new Vector<Integer>();

			if (inTact(currentCell.x - 1, currentCell.y))
				neighbors.add(WEST);
			if (inTact(currentCell.x + 1, currentCell.y))
				neighbors.add(EAST);
			if (inTact(currentCell.x, currentCell.y + 1))
				neighbors.add(SOUTH);
			if (inTact(currentCell.x, currentCell.y - 1))
				neighbors.add(NORTH);

			if (neighbors.size() > 0) {
				int n = getRandom(neighbors.size());
				Integer dir = (Integer) neighbors.elementAt(n);
				setWall(currentCell.x, currentCell.y, dir.intValue(), false);
				cellStack.addElement(currentCell);
				switch (dir.intValue()) {
				case NORTH:
					currentCell = new MazeCell(currentCell.x, currentCell.y - 1);
					break;
				case SOUTH:
					currentCell = new MazeCell(currentCell.x, currentCell.y + 1);
					break;
				case EAST:
					currentCell = new MazeCell(currentCell.x + 1, currentCell.y);
					break;
				case WEST:
					currentCell = new MazeCell(currentCell.x - 1, currentCell.y);
					break;
				}
				visitedCells++;
			} else {
				currentCell = (MazeCell) cellStack.elementAt(cellStack.size() - 1);
				cellStack.remove(cellStack.size() - 1);
			}
		}

	}

	public boolean moveMouse(int direction) {
		if (isWall(mouseX, mouseY, direction))
			return false;
		switch (direction) {
		case NORTH:
			mouseY--;
			break;
		case SOUTH:
			mouseY++;
			break;
		case WEST:
			mouseX--;
			break;
		case EAST:
			mouseX++;
			break;
		}
		paint(getGraphics());
		return true;
	}

	public int getMouseState(int direction) {
		int state = 0;
		int x = mouseX;
		int y = mouseY;

		while (!isWall(x, y, direction)) {
			switch (direction) {
			case NORTH:
				y--;
				break;
			case SOUTH:
				y++;
				break;
			case EAST:
				x++;
				break;
			case WEST:
				x--;
				break;
			}
			state++;
		}
		return state;
	}
}