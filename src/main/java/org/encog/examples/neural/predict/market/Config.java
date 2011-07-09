/*
 * Encog(tm) Examples v2.4
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

package org.encog.examples.neural.predict.market;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.encog.ml.data.market.TickerSymbol;

/**
 * Basic config info for the market prediction example.
 * 
 * @author jeff
 * 
 */
public class Config {
	public static final String TRAINING_FILE = "marketData.egb";
	public static final String NETWORK_FILE = "marketNetwork.eg";
	public static final int TRAINING_MINUTES = 1;
	public static final int HIDDEN1_COUNT = 20;
	public static final int HIDDEN2_COUNT = 0;
	public static final int INPUT_WINDOW = 10;
	public static final int PREDICT_WINDOW = 1;
	public static final TickerSymbol TICKER = new TickerSymbol("AAPL");
}
