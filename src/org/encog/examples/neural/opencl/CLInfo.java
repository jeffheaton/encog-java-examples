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
package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.opencl.EncogCL;
import org.encog.engine.opencl.EncogCLDevice;
import org.encog.engine.opencl.EncogCLError;
import org.encog.engine.opencl.EncogCLPlatform;
import org.encog.util.logging.Logging;

public class CLInfo {
	public static void main(String[] args)
	{
		Logging.stopConsoleLogging();
		try {
			Encog.getInstance().initCL();
			
			EncogCL cl = Encog.getInstance().getCL();
			
			for(EncogCLPlatform platform : cl.getPlatforms() )
			{
				System.out.println("Found platform: " + platform.getName() );
				for(EncogCLDevice device: platform.getDevices() )
				{
					System.out.println("Found device: " + device.toString() );
				}
			}
			
			
		}
		catch(EncogCLError e) {
			System.out.println("OpenCL Error: " + e.getMessage());
		}
	}
}
