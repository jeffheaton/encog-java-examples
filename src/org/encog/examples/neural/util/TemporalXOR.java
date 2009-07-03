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

package org.encog.examples.neural.util;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;

/**
 * Utility class that presents the XOR operator as a serial stream of values.
 * This is used to predict the next value in the XOR sequence. This provides a
 * simple stream of numbers that can be predicted.
 * 
 * @author jeff
 * 
 */
public class TemporalXOR {

	/**
	 * 1 xor 0 = 1, 0 xor 0 = 0, 0 xor 1 = 1, 1 xor 1 = 0
	 */
	public static final double[] SEQUENCE = { 1.0, 0.0, 1.0, 0.0, 0.0, 0.0,
			0.0, 1.0, 1.0, 1.0, 1.0, 0.0 };

	private double[][] input;
	private double[][] ideal;

	public NeuralDataSet generate(final int count) {
		this.input = new double[count][1];
		this.ideal = new double[count][1];

		for (int i = 0; i < this.input.length; i++) {
			this.input[i][0] = TemporalXOR.SEQUENCE[i
					% TemporalXOR.SEQUENCE.length];
			this.ideal[i][0] = TemporalXOR.SEQUENCE[(i + 1)
					% TemporalXOR.SEQUENCE.length];
		}

		return new BasicNeuralDataSet(this.input, this.ideal);
	}
}
