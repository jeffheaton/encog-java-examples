/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
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
