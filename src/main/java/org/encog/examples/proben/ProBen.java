package org.encog.examples.proben;

import java.io.File;

import org.encog.Encog;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;

public class ProBen {
	
	public final static String METHOD_NAME = MLMethodFactory.TYPE_FEEDFORWARD;
	public final static String TRAINING_TYPE = MLTrainFactory.TYPE_RPROP;
	public final static String METHOD_ARCHITECTURE = "?:B->SIGMOID->40:B->SIGMOID->?";
	public final static String TRAINING_ARGS = "";
	
	
	public static void main(String[] args) {
		
		try {
			ProBenRunner runner = new ProBenRunner(new File("C:\\test\\proben1\\"),
					METHOD_NAME,
					TRAINING_TYPE,
					METHOD_ARCHITECTURE,
					TRAINING_ARGS);
			
			runner.run();

			Encog.getInstance().shutdown();
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
