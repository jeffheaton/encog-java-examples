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

package org.encog.examples.neural.predict.market;

import org.encog.util.logging.Logging;


/**
 * Use the saved market neural network, and now attempt to predict for today, and the
 * last 60 days and see what the results are.
 */
public class MarketPredict {
		
	public static void main(String[] args)
	{
		Logging.stopConsoleLogging();
		
		if( args.length<1 ) {
			System.out.println("Specify one of the following arguments: generate, train, incremental, selective or evaluate.");
		}
		else
		{
			if( args[0].equalsIgnoreCase("generate") ) {
				MarketBuildTraining.generate();
			} 
			else if( args[0].equalsIgnoreCase("train") ) {
				MarketTrain.train();
			} 
			else if( args[0].equalsIgnoreCase("evaluate") ) {
				MarketEvaluate.evaluate();
			} else if( args[0].equalsIgnoreCase("prune") ) {
				MarketPrune.incremental();
			} 
		}
	}
	
}
