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

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Sample: GUI element that displays sampled data.
 */
public class Sample extends JPanel {

	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = 2250441617163548592L;
	/**
	 * The image data.
	 */
	SampleData data;

	/**
	 * The constructor.
	 * 
	 * @param width
	 *            The width of the downsampled image
	 * @param height
	 *            The height of the downsampled image
	 */
	Sample(final int width, final int height) {
		this.data = new SampleData(' ', width, height);
	}

	/**
	 * The image data object.
	 * 
	 * @return The image data object.
	 */
	SampleData getData() {
		return this.data;
	}

	/**
	 * @param g
	 *            Display the downsampled image.
	 */
	@Override
	public void paint(final Graphics g) {
		if (this.data == null) {
			return;
		}

		int x, y;
		final int vcell = getHeight() / this.data.getHeight();
		final int hcell = getWidth() / this.data.getWidth();

		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.black);
		for (y = 0; y < this.data.getHeight(); y++) {
			g.drawLine(0, y * vcell, getWidth(), y * vcell);
		}
		for (x = 0; x < this.data.getWidth(); x++) {
			g.drawLine(x * hcell, 0, x * hcell, getHeight());
		}

		for (y = 0; y < this.data.getHeight(); y++) {
			for (x = 0; x < this.data.getWidth(); x++) {
				if (this.data.getData(x, y)) {
					g.fillRect(x * hcell, y * vcell, hcell, vcell);
				}
			}
		}

		g.setColor(Color.black);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

	}

	/**
	 * Assign a new image data object.
	 * 
	 * @param data
	 *            The image data object.
	 */

	void setData(final SampleData data) {
		this.data = data;
	}

}