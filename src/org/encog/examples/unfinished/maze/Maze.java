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

import java.util.Vector;

public class Maze {
	
	public final static int NORTH = 1;
	public final static int SOUTH = 2;
	public final static int EAST = 3;
	public final static int WEST = 4;

	
	protected final int rows;
	protected final int columns;
	protected final boolean rowWalls[][];
	protected final boolean columnWalls[][];

	public Maze(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		rowWalls = new boolean[rows][columns];
		columnWalls = new boolean[columns][rows];
		for (int x = 0; x < columns; x++)
			for (int y = 0; y < rows; y++) {
				rowWalls[y][x] = true;
				columnWalls[x][y] = true;
			}
	}
	
	public boolean isBorder(int x, int y, int direction) {
		
		if( x<0 || y<0 )
			return true;
		
		if( x>=this.columns || y>=this.rows )
			return true;
		
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
			if( y<=0 )
				return true;
			else
				return (columnWalls[x][y - 1]);
		case SOUTH:
			if( y>=(this.rows-1))
				return true;
			else
				return (columnWalls[x][y]);
		case EAST:
			if(x>=(this.columns-1))
				return true;
			else
				return (rowWalls[y][x]);
		case WEST:
			if(x<=0)
				return true;
			else
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

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}



}
