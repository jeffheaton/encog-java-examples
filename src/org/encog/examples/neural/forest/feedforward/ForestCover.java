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

package org.encog.examples.neural.forest.feedforward;

import org.encog.normalize.DataNormalization;
import org.encog.persist.EncogPersistedCollection;
import org.encog.util.logging.Logging;

/**
 * 
 * To make use of this example program you must download the file
 * "covtype.data". This file contains forest cover type for 30 x 30 meter cells
 * obtained from US Forest Service (USFS) Region 2 Resource Information System
 * (RIS) data.
 * 
 * This data file can be found here:
 * 
 * http://kdd.ics.uci.edu/databases/covertype/covertype.html
 * 
 * This example makes use of information such as elevation and soil type to
 * predict what type of tree cover the land has on it. This example is a good
 * introduction to Encog normalization, as it uses the DataNormalization class
 * to properly normalize the raw data provided by the USFS.
 * 
 * Running this example occurs in three steps. First you must generate the
 * training data.
 * 
 * Step 1: Generate Data
 * 
 * ForestCover generate [e/o]
 * 
 * There are a number of files needed to train and evaluate the neural network.
 * First the data must be split into a training and evaluation set. 75% of the
 * data is used for training. 25% is used to evaluate.
 * 
 * It is important not to use all of the data for training so that you can
 * evaluate using data that the neural network has never seen before.
 * 
 * The [e/o] option allows you to specify if the cover types should be
 * normalized equilaterally or using "one of". You will have better results with
 * equilateral training.
 * 
 * Step 2: Train the Neural Network
 * 
 * ForestCover traingui
 * 
 * Once the files have been generated the neural network should be trained. The
 * "traingui" command displays a dialog box that allows you to stop the training
 * manually once you feel the error is acceptable. To train in console mode only
 * just use the "train" command.
 * 
 * Step 3: Evaluate the Neural Network
 * 
 * ForestCover evaluate
 * 
 * The evaluation step sees how well the neural network does at actually
 * predicting forest cover with new data that it was not previously trained on.
 * You are given an overall percentage, as well as percent error broken down by
 * tree type.
 * 
 * ----------------------------------------------------------------------------
 * A brief description of the format for this file is given here:
 * 
 * Elevation (meters) Elevation in meters, field 0 Aspect (azimuth) Aspect in
 * degrees azimuth, field 1 Slope (degrees) Slope in degrees, field 2
 * Horizontal_Distance_To_Hydrology(meters) Horz Dist to nearest surface water
 * features, field 3 Vertical_Distance_To_Hydrology (meters) Vert Dist to
 * nearest surface water features, field 4 Horizontal_Distance_To_Roadways
 * (meters) Horz Dist to nearest roadway, field 5 Hillshade_9am (0 to 255 index)
 * Hillshade index at 9am, summer solstice, field 6 Hillshade_Noon (0 to 255
 * index) Hillshade index at noon, summer soltice, field 7 Hillshade_3pm (0 to
 * 255 index) Hillshade index at 3pm, summer solstice, field 8
 * Horizontal_Distance_To_Fire_Point(meters)Horz Dist to nearest wildfire
 * ignition points, field 9 Wilderness_Area (4 binary columns) 0 (absence) or 1
 * (presence) Wilderness area designation, field 10-13 Soil_Type (40 binary
 * columns) 0 (absence) or 1 (presence) Soil Type designation, field 14-54
 * Cover_Type (7 types) (integer) 1 to 7 Forest Cover Type designation, field
 * 55-62
 * 
 * Wilderness Areas: 1 -- Rawah Wilderness Area 2 -- Neota Wilderness Area 3 --
 * Comanche Peak Wilderness Area 4 -- Cache la Poudre Wilderness Area
 * 
 * Soil Types: 1 to 40 : based on the USFS Ecological Landtype Units for this
 * study area.
 * 
 * Forest Cover Types: 1 -- Spruce/Fir 2 -- Lodgepole Pine 3 -- Ponderosa Pine 4 --
 * Cottonwood/Willow 5 -- Aspen 6 -- Douglas-fir 7 -- Krummholz
 * 
 */

public class ForestCover {

	public static void generate(boolean useOneOf) {
		GenerateData generate = new GenerateData();
		generate.step1();
		generate.step2();
		DataNormalization norm = generate.step3(useOneOf);

		EncogPersistedCollection encog = new EncogPersistedCollection(
				Constant.TRAINED_NETWORK_FILE);
		encog.add(Constant.NORMALIZATION_NAME, norm);
	}

	public static void train(boolean useGUI) {
		TrainNetwork program = new TrainNetwork();
		program.train(useGUI);
	}

	public static void evaluate() {
		Evaluate evaluate = new Evaluate();
		evaluate.evaluate();
	}

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out
					.println("Usage: ForestCover [generate [e/o]/train/traingui/evaluate] ");
		} else {
			Logging.stopConsoleLogging();
			if (args[0].equalsIgnoreCase("generate")) {
				if (args.length < 2) {
					System.out.println("When using generate, you must specify an 'e' or an 'o' as the second parameter.");
				} else {
					boolean useOneOf;

					if (args[1].toLowerCase().equals("e"))
						useOneOf = false;
					else
						useOneOf = true;

					generate(useOneOf);
				}
			} else if (args[0].equalsIgnoreCase("train"))
				train(false);
			else if (args[0].equalsIgnoreCase("traingui"))
				train(true);
			else if (args[0].equalsIgnoreCase("evaluate"))
				evaluate();
		}
	}
}
