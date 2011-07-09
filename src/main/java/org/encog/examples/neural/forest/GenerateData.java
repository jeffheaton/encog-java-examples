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

package org.encog.examples.neural.forest;

import java.io.File;

import org.encog.StatusReportable;
import org.encog.util.normalize.DataNormalization;
import org.encog.util.normalize.input.InputField;
import org.encog.util.normalize.input.InputFieldCSV;
import org.encog.util.normalize.output.OutputField;
import org.encog.util.normalize.output.OutputFieldDirect;
import org.encog.util.normalize.output.OutputFieldRangeMapped;
import org.encog.util.normalize.output.nominal.OutputEquilateral;
import org.encog.util.normalize.output.nominal.OutputOneOf;
import org.encog.util.normalize.segregate.IntegerBalanceSegregator;
import org.encog.util.normalize.segregate.index.IndexSampleSegregator;
import org.encog.util.normalize.target.NormalizationStorageCSV;

public class GenerateData implements StatusReportable {
	private ForestConfig config;
	
	public GenerateData(ForestConfig config) {
		this.config = config;
	}
	
	public void buildOutputOneOf(DataNormalization norm, InputField coverType)
	{
		OutputOneOf outType = new OutputOneOf(0.9,0.1);
		outType.addItem(coverType, 1);
		outType.addItem(coverType, 2);
		outType.addItem(coverType, 3);
		outType.addItem(coverType, 4);
		outType.addItem(coverType, 5);
		outType.addItem(coverType, 6);
		outType.addItem(coverType, 7);
		norm.addOutputField(outType, true);
	}
	
	public void buildOutputEquilateral(DataNormalization norm, InputField coverType)
	{
		OutputEquilateral outType = new OutputEquilateral(0.9,0.1);
		outType.addItem(coverType, 1);
		outType.addItem(coverType, 2);
		outType.addItem(coverType, 3);
		outType.addItem(coverType, 4);
		outType.addItem(coverType, 5);
		outType.addItem(coverType, 6);
		outType.addItem(coverType, 7);
		norm.addOutputField(outType, true);
	}
	
	public void copy(File source,File target,int start,int stop,int size)
	{
		InputField inputField[] = new InputField[55];
		
		DataNormalization norm = new DataNormalization();
		norm.setReport(this);
		norm.setTarget(new NormalizationStorageCSV(target));
		for(int i=0;i<55;i++)
		{
			inputField[i] = new InputFieldCSV(true,source,i);
			norm.addInputField(inputField[i]);
			OutputField outputField = new OutputFieldDirect(inputField[i]);
			norm.addOutputField(outputField);
		}
				
		// load only the part we actually want, i.e. training or eval
		IndexSampleSegregator segregator2 = new IndexSampleSegregator(start,stop,size);
		norm.addSegregator(segregator2);
						
		norm.process();
	}
	
	public void narrow(File source,File target,int field, int count)
	{
		InputField inputField[] = new InputField[55];
		
		DataNormalization norm = new DataNormalization();
		norm.setReport(this);
		norm.setTarget(new NormalizationStorageCSV(target));
		for(int i=0;i<55;i++)
		{
			inputField[i] = new InputFieldCSV(true,source,i);
			norm.addInputField(inputField[i]);
			OutputField outputField = new OutputFieldDirect(inputField[i]);
			norm.addOutputField(outputField);
		}
				
		IntegerBalanceSegregator segregator = new IntegerBalanceSegregator(inputField[field],count);
		norm.addSegregator(segregator);
						
		norm.process();
		System.out.println("Samples per tree type:");
		System.out.println(segregator.dumpCounts());
	}
	
	public void step1()
	{
		System.out.println("Step 1: Generate training and evaluation files");
		System.out.println("Generate training file");
		copy(config.getCoverTypeFile(),config.getTrainingFile(),0,2,4); // take 3/4
		System.out.println("Generate evaluation file");
		copy(config.getCoverTypeFile(),config.getEvaluateFile(),3,3,4); // take 1/4
	}
	
	public void step2()
	{
		System.out.println("Step 2: Balance training to have the same number of each tree");
		narrow(config.getTrainingFile(),config.getBalanceFile(),54,3000);
	}
	
	public DataNormalization step3(boolean useOneOf)
	{
		System.out.println("Step 3: Normalize training data");
		InputField inputElevation;
		InputField inputAspect;
		InputField inputSlope;
		InputField hWater;
		InputField vWater;
		InputField roadway;
		InputField shade9;
		InputField shade12;
		InputField shade3;
		InputField firepoint;
		InputField[] wilderness = new InputField[4];
		InputField[] soilType = new InputField[40];
		InputField coverType;	
		
		DataNormalization norm = new DataNormalization();
		norm.setReport(this);
		norm.setTarget(new NormalizationStorageCSV(config.getNormalizedDataFile()));
		norm.addInputField(inputElevation = new InputFieldCSV(true,config.getBalanceFile(),0));
		norm.addInputField(inputAspect = new InputFieldCSV(true,config.getBalanceFile(),1));
		norm.addInputField(inputSlope = new InputFieldCSV(true,config.getBalanceFile(),2));
		norm.addInputField(hWater = new InputFieldCSV(true,config.getBalanceFile(),3));
		norm.addInputField(vWater = new InputFieldCSV(true,config.getBalanceFile(),4));
		norm.addInputField(roadway = new InputFieldCSV(true,config.getBalanceFile(),5));
		norm.addInputField(shade9 = new InputFieldCSV(true,config.getBalanceFile(),6));
		norm.addInputField(shade12 = new InputFieldCSV(true,config.getBalanceFile(),7));
		norm.addInputField(shade3 = new InputFieldCSV(true,config.getBalanceFile(),8));
		norm.addInputField(firepoint = new InputFieldCSV(true,config.getBalanceFile(),9));
		
		for(int i=0;i<4;i++)
		{
			norm.addInputField(wilderness[i]=new InputFieldCSV(true,config.getBalanceFile(),10+i));
		}
		
		for(int i=0;i<40;i++)
		{
			norm.addInputField(soilType[i]=new InputFieldCSV(true,config.getBalanceFile(),14+i));
		}
		
		norm.addInputField(coverType=new InputFieldCSV(false,config.getBalanceFile(),54));
		
		norm.addOutputField(new OutputFieldRangeMapped(inputElevation,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(inputAspect,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(inputSlope,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(hWater,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(vWater,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(roadway,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(shade9,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(shade12,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(shade3,0.1,0.9));
		norm.addOutputField(new OutputFieldRangeMapped(firepoint,0.1,0.9));
		
		for(int i=0;i<40;i++)
		{
			norm.addOutputField(new OutputFieldDirect(soilType[i]));
		}
		
		if( useOneOf )
			buildOutputOneOf(norm,coverType);
		else
			buildOutputEquilateral(norm,coverType);
				
		norm.process();
		return norm;
	}
	
	public void report(int total, int current, String message) {
		System.out.println( current + "/" + total + " " + message );
		
	}
}
