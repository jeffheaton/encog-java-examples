package org.encog.examples.neural.forest.som;

import java.io.File;

import org.encog.StatusReportable;
import org.encog.normalize.DataNormalization;
import org.encog.normalize.input.InputField;
import org.encog.normalize.input.InputFieldCSV;
import org.encog.normalize.output.OutputField;
import org.encog.normalize.output.OutputFieldDirect;
import org.encog.normalize.output.OutputFieldRangeMapped;
import org.encog.normalize.output.nominal.OutputEquilateral;
import org.encog.normalize.output.nominal.OutputOneOf;
import org.encog.normalize.segregate.IntegerBalanceSegregator;
import org.encog.normalize.segregate.index.IndexSampleSegregator;
import org.encog.normalize.target.NormalizationStorageCSV;

public class GenerateData implements StatusReportable {
	
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
	
	
	public void step1()
	{
		System.out.println("Step 1: Generate training and evaluation files");
		System.out.println("Generate training file");
		copy(Constant.COVER_TYPE_FILE,Constant.TRAINING_FILE,0,2,4); // take 3/4
		System.out.println("Generate evaluation file");
		copy(Constant.COVER_TYPE_FILE,Constant.EVALUATE_FILE,3,3,4); // take 1/4
	}
	
	public DataNormalization step2()
	{
		System.out.println("Step 2: Normalize training data");
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
		norm.setTarget(new NormalizationStorageCSV(Constant.NORMALIZED_FILE));
		norm.addInputField(inputElevation = new InputFieldCSV(true,Constant.TRAINING_FILE,0));
		norm.addInputField(inputAspect = new InputFieldCSV(true,Constant.TRAINING_FILE,1));
		norm.addInputField(inputSlope = new InputFieldCSV(true,Constant.TRAINING_FILE,2));
		norm.addInputField(hWater = new InputFieldCSV(true,Constant.TRAINING_FILE,3));
		norm.addInputField(vWater = new InputFieldCSV(true,Constant.TRAINING_FILE,4));
		norm.addInputField(roadway = new InputFieldCSV(true,Constant.TRAINING_FILE,5));
		norm.addInputField(shade9 = new InputFieldCSV(true,Constant.TRAINING_FILE,6));
		norm.addInputField(shade12 = new InputFieldCSV(true,Constant.TRAINING_FILE,7));
		norm.addInputField(shade3 = new InputFieldCSV(true,Constant.TRAINING_FILE,8));
		norm.addInputField(firepoint = new InputFieldCSV(true,Constant.TRAINING_FILE,9));
		
		for(int i=0;i<4;i++)
		{
			norm.addInputField(wilderness[i]=new InputFieldCSV(true,Constant.TRAINING_FILE,10+i));
		}
		
		for(int i=0;i<40;i++)
		{
			norm.addInputField(soilType[i]=new InputFieldCSV(true,Constant.TRAINING_FILE,14+i));
		}
		
		norm.addInputField(coverType=new InputFieldCSV(false,Constant.TRAINING_FILE,54));
		
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
				
		norm.process();
		return norm;
	}
	
	public void report(int total, int current, String message) {
		System.out.println( current + "/" + total + " " + message );
		
	}
}
