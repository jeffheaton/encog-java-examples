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
import org.encog.engine.opencl.EncogCLDevice;
import org.encog.engine.opencl.kernels.KernelVectorAdd;

/**
 * VERY VERY simple OpenCL test.  I used this just to make sure Encog is talking to OpenCL.
 *
 */
public class SimpleCLTest {
	public static void main(String[] args)
	{
        Encog.getInstance().initCL();

        EncogCLDevice device = Encog.getInstance().getCL().getDevices().get(0);
        KernelVectorAdd k = new KernelVectorAdd(device,4);
        k.compile();

        double[] a = { 1, 2, 3, 4 };
        double[] b = { 5, 6, 7, 8 };
        double[] c = k.add(device, a, b);

        for (int i = 0; i < a.length; i++)
        {
            System.out.println(a[i] + " + " + b[i] + " = " + c[i]);
        }
	}
}
