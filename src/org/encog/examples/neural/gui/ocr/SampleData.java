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

/**
 * SampleData: Holds sampled data that will be used to train the neural network.
 */
public class SampleData implements Comparable<SampleData>, Cloneable {

	/**
	 * The downsampled data as a grid of booleans.
	 */
	protected boolean grid[][];

	/**
	 * The letter.
	 */
	protected char letter;

	/**
	 * The constructor
	 * 
	 * @param letter
	 *            What letter this is
	 * @param width
	 *            The width
	 * @param height
	 *            The height
	 */
	public SampleData(final char letter, final int width, final int height) {
		this.grid = new boolean[width][height];
		this.letter = letter;
	}

	/**
	 * Clear the downsampled image
	 */
	public void clear() {
		for (int x = 0; x < this.grid.length; x++) {
			for (int y = 0; y < this.grid[0].length; y++) {
				this.grid[x][y] = false;
			}
		}
	}

	/**
	 * Create a copy of this sample
	 * 
	 * @return A copy of this sample
	 */
	@Override
	public Object clone()

	{

		final SampleData obj = new SampleData(this.letter, getWidth(),
				getHeight());
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				obj.setData(x, y, getData(x, y));
			}
		}
		return obj;
	}

	/**
	 * Compare this sample to another, used for sorting.
	 * 
	 * @param o
	 *            The object being compared against.
	 * @return Same as String.compareTo
	 */

	public int compareTo(final SampleData o) {
		final SampleData obj = o;
		if (getLetter() > obj.getLetter()) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Get a pixel from the sample.
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @return The requested pixel
	 */
	public boolean getData(final int x, final int y) {
		return this.grid[x][y];
	}

	/**
	 * Get the height of the down sampled image.
	 * 
	 * @return The height of the downsampled image.
	 */
	public int getHeight() {
		return this.grid[0].length;
	}

	/**
	 * Get the letter that this sample represents.
	 * 
	 * @return The letter that this sample represents.
	 */
	public char getLetter() {
		return this.letter;
	}

	/**
	 * Get the width of the downsampled image.
	 * 
	 * @return The width of the downsampled image
	 */
	public int getWidth() {
		return this.grid.length;
	}

	/**
	 * Set one pixel of sample data.
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param v
	 *            The value to set
	 */
	public void setData(final int x, final int y, final boolean v) {
		this.grid[x][y] = v;
	}

	/**
	 * Set the letter that this sample represents.
	 * 
	 * @param letter
	 *            The letter that this sample represents.
	 */
	public void setLetter(final char letter) {
		this.letter = letter;
	}

	/**
	 * Convert this sample to a string.
	 * 
	 * @return Just returns the letter that this sample is assigned to.
	 */
	@Override
	public String toString() {
		return "" + this.letter;
	}

}