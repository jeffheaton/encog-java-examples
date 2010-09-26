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
