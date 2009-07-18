package org.encog.examples.neural.predict.sunspot;

import java.text.NumberFormat;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.logging.Logging;

/**
 * Simple class to predict sun spots.  This is very loosely based on a 
 * an example by Karsten Kutza written in C on 1996-04-17.
 * http://www.neural-networks-at-your-fingertips.com/bpn.html
 * 
 * I translated it to Java and adapted it to use Encog.  This example also
 * trains with RPROP, whereas the original used backprop.
 * 
 * The acceptable error rate is set to 6%.  This is just for a quick example,
 * you should lower this, but it will take considerable time to train.  6%
 * will not be overly accurate.
 * 
 * This example creates two types of predictions:
 * 
 * Regular: All predictions are based on actual SunSpot data.
 * 
 * Closed Loop: Predictions are based on previously predicted data.
 * 
 * @author jeff
 *
 */
public class PredictSunspot {

	public final static double[] SUNSPOTS = {
            0.0262,  0.0575,  0.0837,  0.1203,  0.1883,  0.3033,  
            0.1517,  0.1046,  0.0523,  0.0418,  0.0157,  0.0000,  
            0.0000,  0.0105,  0.0575,  0.1412,  0.2458,  0.3295,  
            0.3138,  0.2040,  0.1464,  0.1360,  0.1151,  0.0575,  
            0.1098,  0.2092,  0.4079,  0.6381,  0.5387,  0.3818,  
            0.2458,  0.1831,  0.0575,  0.0262,  0.0837,  0.1778,  
            0.3661,  0.4236,  0.5805,  0.5282,  0.3818,  0.2092,  
            0.1046,  0.0837,  0.0262,  0.0575,  0.1151,  0.2092,  
            0.3138,  0.4231,  0.4362,  0.2495,  0.2500,  0.1606,  
            0.0638,  0.0502,  0.0534,  0.1700,  0.2489,  0.2824,  
            0.3290,  0.4493,  0.3201,  0.2359,  0.1904,  0.1093,  
            0.0596,  0.1977,  0.3651,  0.5549,  0.5272,  0.4268,  
            0.3478,  0.1820,  0.1600,  0.0366,  0.1036,  0.4838,  
            0.8075,  0.6585,  0.4435,  0.3562,  0.2014,  0.1192,  
            0.0534,  0.1260,  0.4336,  0.6904,  0.6846,  0.6177,  
            0.4702,  0.3483,  0.3138,  0.2453,  0.2144,  0.1114,  
            0.0837,  0.0335,  0.0214,  0.0356,  0.0758,  0.1778,  
            0.2354,  0.2254,  0.2484,  0.2207,  0.1470,  0.0528,  
            0.0424,  0.0131,  0.0000,  0.0073,  0.0262,  0.0638,  
            0.0727,  0.1851,  0.2395,  0.2150,  0.1574,  0.1250,  
            0.0816,  0.0345,  0.0209,  0.0094,  0.0445,  0.0868,  
            0.1898,  0.2594,  0.3358,  0.3504,  0.3708,  0.2500,  
            0.1438,  0.0445,  0.0690,  0.2976,  0.6354,  0.7233,  
            0.5397,  0.4482,  0.3379,  0.1919,  0.1266,  0.0560,  
            0.0785,  0.2097,  0.3216,  0.5152,  0.6522,  0.5036,  
            0.3483,  0.3373,  0.2829,  0.2040,  0.1077,  0.0350,  
            0.0225,  0.1187,  0.2866,  0.4906,  0.5010,  0.4038,  
            0.3091,  0.2301,  0.2458,  0.1595,  0.0853,  0.0382,  
            0.1966,  0.3870,  0.7270,  0.5816,  0.5314,  0.3462,  
            0.2338,  0.0889,  0.0591,  0.0649,  0.0178,  0.0314,  
            0.1689,  0.2840,  0.3122,  0.3332,  0.3321,  0.2730,  
            0.1328,  0.0685,  0.0356,  0.0330,  0.0371,  0.1862,  
            0.3818,  0.4451,  0.4079,  0.3347,  0.2186,  0.1370,  
            0.1396,  0.0633,  0.0497,  0.0141,  0.0262,  0.1276,  
            0.2197,  0.3321,  0.2814,  0.3243,  0.2537,  0.2296,  
            0.0973,  0.0298,  0.0188,  0.0073,  0.0502,  0.2479,  
            0.2986,  0.5434,  0.4215,  0.3326,  0.1966,  0.1365,  
            0.0743,  0.0303,  0.0873,  0.2317,  0.3342,  0.3609,  
            0.4069,  0.3394,  0.1867,  0.1109,  0.0581,  0.0298,  
            0.0455,  0.1888,  0.4168,  0.5983,  0.5732,  0.4644,  
            0.3546,  0.2484,  0.1600,  0.0853,  0.0502,  0.1736,  
            0.4843,  0.7929,  0.7128,  0.7045,  0.4388,  0.3630,  
            0.1647,  0.0727,  0.0230,  0.1987,  0.7411,  0.9947,  
            0.9665,  0.8316,  0.5873,  0.2819,  0.1961,  0.1459,  
            0.0534,  0.0790,  0.2458,  0.4906,  0.5539,  0.5518,  
            0.5465,  0.3483,  0.3603,  0.1987,  0.1804,  0.0811,  
            0.0659,  0.1428,  0.4838,  0.8127 
          };
	
	public final static int STARTING_YEAR = 1700;
	public final static int WINDOW_SIZE = 30;
	public final static int TRAIN_START = WINDOW_SIZE;
	public final static int TRAIN_END = 259;
	public final static int EVALUATE_START = 260;
	public final static int EVALUATE_END = SUNSPOTS.length-1;
	
	/**
	 * This really should be lowered, I am setting it to a level here that will
	 * train in under a minute.
	 */
	public final static double MAX_ERROR = 0.06;

	private double[] normalizedSunspots;
	private double[] closedLoopSunspots;
	private double mean;
	
	public void normalizeSunspots(double lo,double hi)
	{			
		  double min = Double.MAX_VALUE;
		  double max = Double.MIN_VALUE;
		  for (int year=0; year<SUNSPOTS.length; year++) {
		    min = Math.min(min, SUNSPOTS[year]);
		    max = Math.max(max, SUNSPOTS[year]);
		  }
		  
		  normalizedSunspots = new double[SUNSPOTS.length];
		  closedLoopSunspots = new double[SUNSPOTS.length];
		  
		  mean = 0;
		  for (int year=0; year<SUNSPOTS.length; year++) {
			  normalizedSunspots[year] = 
			  closedLoopSunspots [year] = ((SUNSPOTS[year]-min) / (max-min)) * (hi-lo) + lo;
		    mean += normalizedSunspots[year] / SUNSPOTS.length;
		  }
	}
	
	public NeuralDataSet generateTraining()
	{
		NeuralDataSet result = new BasicNeuralDataSet();
		for(int year = TRAIN_START;year<TRAIN_END;year++)
		{
			BasicNeuralData input = new BasicNeuralData(WINDOW_SIZE);
			BasicNeuralData ideal = new BasicNeuralData(1);
			int index = 0;
			for(int i=year-WINDOW_SIZE;i<year;i++)
			{
				input.setData(index++,this.normalizedSunspots[i]);
			}
			ideal.setData(0, this.normalizedSunspots[year]);
			
			result.add(input,ideal);
		}
		return result;
	}
	
	public BasicNetwork createNetwork()
	{
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(WINDOW_SIZE));
		network.addLayer(new BasicLayer(10));
		network.addLayer(new BasicLayer(1));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	public void train(BasicNetwork network,NeuralDataSet training)
	{
		final Train train = new ResilientPropagation(network, training);

		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > MAX_ERROR);
	}
	
	public void predict(BasicNetwork network)
	{
		NumberFormat f = NumberFormat.getNumberInstance();
		f.setMaximumFractionDigits(4);
		f.setMinimumFractionDigits(4);
		
		System.out.println("Year\tActual\tPredict\tClosed Loop Predict");
		
		for(int year=EVALUATE_START;year<EVALUATE_END;year++)
		{
			// calculate based on actual data
			NeuralData input = new BasicNeuralData(WINDOW_SIZE);
			for(int i=0;i<input.size();i++)
			{
				input.setData(i,this.normalizedSunspots[(year-WINDOW_SIZE)+i]);
			}
			NeuralData output = network.compute(input);
			double prediction = output.getData(0);
			this.closedLoopSunspots[year] = prediction;
			
			// calculate "closed loop", based on predicted data
			for(int i=0;i<input.size();i++)
			{
				input.setData(i,this.closedLoopSunspots[(year-WINDOW_SIZE)+i]);
			}
			output = network.compute(input);
			double closedLoopPrediction = output.getData(0);
			
			// display
			System.out.println((STARTING_YEAR+year)
					+"\t"+f.format(this.normalizedSunspots[year])
					+"\t"+f.format(prediction)
					+"\t"+f.format(closedLoopPrediction)
			);
			
		}
	}
	
	public void run()
	{
		normalizeSunspots(0.1,0.9);
		BasicNetwork network = createNetwork();
		NeuralDataSet training = generateTraining();
		train(network,training);
		predict(network);
		
	}
	
	public static void main(String args[])
	{
		Logging.stopConsoleLogging();
		PredictSunspot sunspot = new PredictSunspot();
		sunspot.run();
	}

}
