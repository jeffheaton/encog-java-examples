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

package org.encog.examples.nonlinear.tsp;

/**
 * City: Holds the location of a city for the traveling salesman problem.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class City {

	/**
	 * The city's x position.
	 */
	int xpos;

	/**
	 * The city's y position.
	 */
	int ypos;

	/**
	 * Constructor.
	 * 
	 * @param x
	 *            The city's x position
	 * @param y
	 *            The city's y position.
	 */
	public City(final int x, final int y) {
		this.xpos = x;
		this.ypos = y;
	}

	/**
	 * Return's the city's x position.
	 * 
	 * @return The city's x position.
	 */
	int getx() {
		return this.xpos;
	}

	/**
	 * Returns the city's y position.
	 * 
	 * @return The city's y position.
	 */
	int gety() {
		return this.ypos;
	}

	/**
	 * Returns how close the city is to another city.
	 * 
	 * @param cother
	 *            The other city.
	 * @return A distance.
	 */
	public int proximity(final City cother) {
		return proximity(cother.getx(), cother.gety());
	}

	/**
	 * Returns how far this city is from a a specific point. This method uses
	 * the pythagorean theorem to calculate the distance.
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @return The distance.
	 */
	int proximity(final int x, final int y) {
		final int xdiff = this.xpos - x;
		final int ydiff = this.ypos - y;
		return (int) Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}
}
