package org.encog.examples.neural.forest;

import java.io.File;

import org.encog.StatusReportable;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.normalize.Normalization;
import org.encog.normalize.input.InputField;
import org.encog.normalize.input.InputFieldCSV;
import org.encog.normalize.output.OutputFieldRangeMapped;
import org.encog.normalize.output.mapped.OutputFieldEncode;
import org.encog.normalize.output.nominal.OutputEquilateral;
import org.encog.normalize.output.nominal.OutputOneOf;
import org.encog.normalize.segregate.RangeSegregator;
import org.encog.normalize.segregate.index.IndexSampleSegregator;
import org.encog.normalize.target.NormalizationStorageNeuralDataSet;

public class GenerateData implements StatusReportable {
	
	public void buildOutputMapped(Normalization norm, InputField coverType)
	{
		OutputFieldEncode mapped1 = new OutputFieldEncode(coverType);
		mapped1.addRange(1, 1, 0.9);
		mapped1.setCatchAll(0.1);
		norm.addOutputField(mapped1);
		OutputFieldEncode mapped2 = new OutputFieldEncode(coverType);
		mapped2.addRange(2, 2, 0.9);
		mapped2.setCatchAll(0.1);
		norm.addOutputField(mapped2);
		OutputFieldEncode mapped3 = new OutputFieldEncode(coverType);
		mapped3.addRange(3, 3, 0.9);
		mapped3.setCatchAll(0.1);
		norm.addOutputField(mapped3);
		OutputFieldEncode mapped4 = new OutputFieldEncode(coverType);
		mapped4.addRange(4, 4, 0.9);
		mapped4.setCatchAll(0.1);
		norm.addOutputField(mapped4);
		OutputFieldEncode mapped5 = new OutputFieldEncode(coverType);
		mapped5.addRange(5, 5, 0.9);
		mapped5.setCatchAll(0.1);
		norm.addOutputField(mapped5);
		OutputFieldEncode mapped6 = new OutputFieldEncode(coverType);
		mapped6.addRange(6, 6, 0.9);
		mapped6.setCatchAll(0.1);
		norm.addOutputField(mapped6);
		OutputFieldEncode mapped7 = new OutputFieldEncode(coverType);
		mapped7.addRange(7, 7, 0.9);
		mapped7.setCatchAll(0.1);
		norm.addOutputField(mapped7);
	}
	
	public void buildOutputOneOf(Normalization norm, InputField coverType)
	{
		OutputOneOf outType = new OutputOneOf(0.9,0.1);
		outType.addItem(coverType, 1);
		outType.addItem(coverType, 2);
		outType.addItem(coverType, 3);
		outType.addItem(coverType, 4);
		outType.addItem(coverType, 5);
		outType.addItem(coverType, 6);
		outType.addItem(coverType, 7);
		norm.addOutputField(outType);
	}
	
	public void buildOutputEquilateral(Normalization norm, InputField coverType)
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
	
	public Normalization generate(File output, int area,int start,int stop, int sample)
	{
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
		
		BufferedNeuralDataSet buffer = new BufferedNeuralDataSet(output);		
		
		Normalization norm = new Normalization();
		norm.setReport(this);
		norm.setTarget(new NormalizationStorageNeuralDataSet(buffer));
		//norm.setTarget(new NormalizationTargetCSV(Constant.TRAINING_FILE));
		norm.addInputField(inputElevation = new InputFieldCSV(Constant.COVER_TYPE_FILE,0));
		norm.addInputField(inputAspect = new InputFieldCSV(Constant.COVER_TYPE_FILE,1));
		norm.addInputField(inputSlope = new InputFieldCSV(Constant.COVER_TYPE_FILE,2));
		norm.addInputField(hWater = new InputFieldCSV(Constant.COVER_TYPE_FILE,3));
		norm.addInputField(vWater = new InputFieldCSV(Constant.COVER_TYPE_FILE,4));
		norm.addInputField(roadway = new InputFieldCSV(Constant.COVER_TYPE_FILE,5));
		norm.addInputField(shade9 = new InputFieldCSV(Constant.COVER_TYPE_FILE,6));
		norm.addInputField(shade12 = new InputFieldCSV(Constant.COVER_TYPE_FILE,7));
		norm.addInputField(shade3 = new InputFieldCSV(Constant.COVER_TYPE_FILE,8));
		norm.addInputField(firepoint = new InputFieldCSV(Constant.COVER_TYPE_FILE,9));
		
		for(int i=0;i<4;i++)
		{
			norm.addInputField(wilderness[i]=new InputFieldCSV(Constant.COVER_TYPE_FILE,10+i));
		}
		
		for(int i=0;i<40;i++)
		{
			norm.addInputField(soilType[i]=new InputFieldCSV(Constant.COVER_TYPE_FILE,14+i));
		}
		
		norm.addInputField(coverType=new InputFieldCSV(Constant.COVER_TYPE_FILE,54));
		
		// select the wilderness area
		RangeSegregator segregator = new RangeSegregator(wilderness[area],false);
		segregator.addRange(1, 1, true);
		norm.addSegregator(segregator);
		
		// load only the part we actually want, i.e. training or eval
		IndexSampleSegregator segregator2 = new IndexSampleSegregator(start,stop,sample);
		norm.addSegregator(segregator2);
		
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
		
		//buildOutputMapped(norm,coverType);
		//buildOutputOneOf(norm,coverType);
		buildOutputEquilateral(norm,coverType);
		
		int inputLayerSize = norm.getNetworkInputLayerSize();
		int outputLayerSize = norm.getNetworkOutputLayerSize();
			
		buffer.beginLoad(inputLayerSize,outputLayerSize);
		
		norm.process();
		buffer.endLoad();
		return norm;
	}
	
	public void report(int total, int current, String message) {
		System.out.println( current + "/" + total + " " + message );
		
	}
}
