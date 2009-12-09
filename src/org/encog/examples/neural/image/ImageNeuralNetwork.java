package org.encog.examples.neural.image;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.encog.EncogError;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.image.ImageNeuralData;
import org.encog.neural.data.image.ImageNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.multi.MultiPropagation;
import org.encog.neural.networks.training.strategy.ResetStrategy;
import org.encog.util.downsample.Downsample;
import org.encog.util.downsample.RGBDownsample;
import org.encog.util.downsample.SimpleIntensityDownsample;
import org.encog.util.logging.Logging;
import org.encog.util.simple.EncogUtility;

/**
 * Should have an input file similar to:
 * 
 * CreateTraining: width:16,height:16,type:RGB
 * Input: image:./coins/dime.png, identity:dime
 * Input: image:./coins/dollar.png, identity:dollar
 * Input: image:./coins/half.png, identity:half dollar
 * Input: image:./coins/nickle.png, identity:nickle
 * Input: image:./coins/penny.png, identity:penny
 * Input: image:./coins/quarter.png, identity:quarter
 * Network: hidden1:100, hidden2:0
 * Train: Mode:console, Minutes:1, StrategyError:0.25, StrategyCycles:50
 * Whatis: image:./coins/dime.png
 * Whatis: image:./coins/half.png
 * Whatis: image:./coins/testcoin.png
 *
 */
public class ImageNeuralNetwork {

	class ImagePair {
		private final File file;
		private final int identity;
		
		public ImagePair(File file, int identity) {
			super();
			this.file = file;
			this.identity = identity;
		}
		public File getFile() {
			return file;
		}
		public int getIdentity() {
			return identity;
		}
	}
	
	private List<ImagePair> imageList = new ArrayList<ImagePair>();
	private Map<String,String> args = new HashMap<String,String>();
	private Map<String,Integer> identity2neuron = new HashMap<String,Integer>();
	private Map<Integer,String> neuron2identity = new HashMap<Integer,String>();
	private ImageNeuralDataSet training;
	private String line;
	private int outputCount;
	private int downsampleWidth;
	private int downsampleHeight;
	private BasicNetwork network;
	private Downsample downsample;
	
	public void executeLine() throws IOException
	{
		int index = line.indexOf(':');
		if( index==-1 ) {
			throw new EncogError("Invalid command: " + line);
		}
		
		String command = line.substring(0,index).toLowerCase().trim();
		String argsStr = line.substring(index+1).trim();
		StringTokenizer tok = new StringTokenizer(argsStr,",");
		this.args.clear();
		while(tok.hasMoreTokens()) {
			String arg = tok.nextToken();
			int index2 = arg.indexOf(':');
			if( index2==-1 ) {
				throw new EncogError("Invalid command: " + line);
			}	
			String key = arg.substring(0,index2).toLowerCase().trim();
			String value = arg.substring(index2+1).trim();
			args.put(key, value);
		}
		
		executeCommand(command,args);
	}
	
	private void executeCommand(String command, Map<String, String> args) throws IOException {
		if( command.equals("input")) {
			processInput();
		}		
		else if( command.equals("createtraining")) {
			processCreateTraining();
		}
		else if( command.equals("train")) {
			processTrain();
		} else if( command.equals("network")) {
			processNetwork();
		} else if( command.equals("whatis")) {
			processWhatIs();
		}
		
	}
	
	private String getArg(String name)
	{
		String result = this.args.get(name);
		if( result==null ) {
			throw new EncogError("Missing argument " + name + " on line: " + this.line);
		}
		return result;
	}
	
	private int assignIdentity(String identity) {
		
		if( this.identity2neuron.containsKey(identity.toLowerCase())) {
			return this.identity2neuron.get(identity.toLowerCase());
		}
		
		int result = this.outputCount;
		this.identity2neuron.put(identity.toLowerCase(), result);
		this.neuron2identity.put(result, identity.toLowerCase());
		this.outputCount++;
		return result;
	}
	
	private void processCreateTraining()
	{
		String strWidth = getArg("width");
		String strHeight = getArg("height");
		String strType = getArg("type");
		
		this.downsampleHeight = Integer.parseInt(strWidth);
		this.downsampleWidth = Integer.parseInt(strHeight);
		
		if( strType.equals("RGB") )
			this.downsample = new RGBDownsample();
		else
			this.downsample = new SimpleIntensityDownsample();
		
		this.training = new ImageNeuralDataSet(
				downsample,false,1,-1);
		System.out.println("Training set created");
	}
	
	private void processTrain() throws IOException {
		String strMode = getArg("mode");
		String strMinutes = getArg("minutes");
		String strStrategyError = getArg("strategyerror");
		String strStrategyCycles = getArg("strategycycles");
		
		System.out.println("Training Beginning... Output patterns=" + this.outputCount);
		
		double strategyError = Double.parseDouble(strStrategyError);
		int strategyCycles = Integer.parseInt(strStrategyCycles);
		
		Train train = new MultiPropagation(network,training);
		train.addStrategy(new ResetStrategy(strategyError, strategyCycles));
		
		if( strMode.equalsIgnoreCase("gui")) {
			EncogUtility.trainDialog(train, network, training);
		}
		else {
			int minutes = Integer.parseInt(strMinutes);
			EncogUtility.trainConsole(train, network, training,minutes);
		}
		System.out.println("Training Stopped...");
	}
	
	private void processNetwork() throws IOException {
		System.out.println("Downsampling images...");
		
		for(ImagePair pair: this.imageList) {
			NeuralData ideal = new BasicNeuralData(this.outputCount);
			int idx = pair.getIdentity();
			for(int i=0;i<this.outputCount;i++) {
				if( i==idx )
					ideal.setData(i, 1);
				else
					ideal.setData(i, -1);
			}		
			
			Image img = ImageIO.read(pair.getFile());
			ImageNeuralData data = new ImageNeuralData(img);
			this.training.add(data,ideal);
		}
		
		String strHidden1 = getArg("hidden1");
		String strHidden2 = getArg("hidden2");
		
		this.training.downsample(this.downsampleHeight, this.downsampleWidth);

		int hidden1 = Integer.parseInt(strHidden1);
		int hidden2 = Integer.parseInt(strHidden2);
		
		this.network = EncogUtility.simpleFeedForward(
				this.training.getInputSize(), hidden1, hidden2, 
				this.training.getIdealSize(), true);
		System.out.println("Created network: " + this.network.toString());
	}
	
	private void processInput() throws IOException
	{
		String image = getArg("image");
		String identity = getArg("identity");
		
		int idx = assignIdentity(identity);
		File file = new File(image);
		
		this.imageList.add( new ImagePair(file,idx));
		
		System.out.println("Added input image:" + image);
	}
	
	public void processWhatIs() throws IOException {
		String filename = getArg("image");
		File file = new File(filename);
		Image img = ImageIO.read(file);
		ImageNeuralData input = new ImageNeuralData(img);
		input.downsample(this.downsample, false, 
				this.downsampleHeight, this.downsampleWidth, 1, -1);
		int winner = this.network.winner(input);
		System.out.println("What is: " + filename + ", it seems to be: " 
				+ this.neuron2identity.get(winner));
	}

	public void execute(String file) throws IOException {
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		while ((this.line = br.readLine()) != null) {
			executeLine();
		}
		in.close();
	}

	public static void main(String[] args) {
		Logging.stopConsoleLogging();
		if (args.length < 1) {
			System.out
					.println("Must specify command file.  See source for format.");
		} else {
			try {
				ImageNeuralNetwork program = new ImageNeuralNetwork();
				program.execute(args[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
