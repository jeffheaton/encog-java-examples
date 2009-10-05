package org.encog.examples.neural.forest;

import java.io.File;

import org.encog.StatusReportable;
import org.encog.neural.data.buffer.BufferedNeuralDataSet;
import org.encog.normalize.Normalization;
import org.encog.normalize.input.InputField;
import org.encog.normalize.input.InputFieldCSV;
import org.encog.normalize.output.OutputFieldRangeMapped;
import org.encog.normalize.output.mapped.OutputFieldEncode;
import org.encog.normalize.segregate.RangeSegregator;
import org.encog.normalize.target.NormalizationTargetCSV;
import org.encog.normalize.target.NormalizationTargetNeuralDataSet;

import com.sun.tools.corba.se.idl.ConstEntry;

/**
 * Elevation (meters)                       Elevation in meters, field 0
 * Aspect (azimuth)                         Aspect in degrees azimuth, field 1
 * Slope (degrees)                          Slope in degrees, field 2
 * Horizontal_Distance_To_Hydrology(meters) Horz Dist to nearest surface water features, field 3
 * Vertical_Distance_To_Hydrology (meters)  Vert Dist to nearest surface water features, field 4
 * Horizontal_Distance_To_Roadways (meters) Horz Dist to nearest roadway, field 5
 * Hillshade_9am (0 to 255 index)			Hillshade index at 9am, summer solstice, field 6
 * Hillshade_Noon (0 to 255 index)			Hillshade index at noon, summer soltice, field 7
 * Hillshade_3pm (0 to 255 index)			Hillshade index at 3pm, summer solstice, field 8
 * Horizontal_Distance_To_Fire_Point(meters)Horz Dist to nearest wildfire ignition points, field 9
 * Wilderness_Area (4 binary columns)       0 (absence) or 1 (presence)	Wilderness area designation, field 10-13
 * Soil_Type (40 binary columns)            0 (absence) or 1 (presence)	Soil Type designation, field 14-54
 * Cover_Type (7 types) (integer)           1 to 7 Forest Cover Type designation, field 55-62
 *
 * Wilderness Areas:  	
 *   1 -- Rawah Wilderness Area
 *   2 -- Neota Wilderness Area
 *   3 -- Comanche Peak Wilderness Area
 *   4 -- Cache la Poudre Wilderness Area
 *
 * Soil Types:		
 *   1 to 40 : based on the USFS Ecological Landtype Units for this study area.
 *
 * Forest Cover Types:	
 *   1 -- Spruce/Fir
 *   2 -- Lodgepole Pine
 *   3 -- Ponderosa Pine
 *   4 -- Cottonwood/Willow
 *   5 -- Aspen
 *   6 -- Douglas-fir
 *   7 -- Krummholz
 *
 */
public class GenerateTraining implements StatusReportable {
	
	public void run()
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
		
		BufferedNeuralDataSet buffer = new BufferedNeuralDataSet(Constant.BUFFER_FILE);
		buffer.beginLoad(Constant.INPUT_NEURON_COUNT, Constant.OUTPUT_NEURON_COUNT);
		
		Normalization norm = new Normalization();
		norm.setReport(this);
		norm.setTarget(new NormalizationTargetNeuralDataSet(buffer));
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
		
		RangeSegregator segregator = new RangeSegregator(wilderness[0],false);
		segregator.addRange(1, 1, true);
		norm.addSegregator(segregator);
		
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
		
		norm.process();
		buffer.endLoad();

	}
	
	public static void main(String[] args)
	{
		GenerateTraining program = new GenerateTraining();
		program.run();
	}

	public void report(int total, int current, String message) {
		System.out.println( current + "/" + total + " " + message );
		
	}
}
