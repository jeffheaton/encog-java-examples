package org.encog.examples.proben;

import org.encog.ml.MLError;
import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.util.Format;

public class ProBenEvaluate {
	
	private ProBenData data;
	private int iterations;
	private double trainError;
	private double testError;
	private double validationError;
	private String methodName;
	private String trainingName;
	private String methodArchitecture;
	private String trainingArgs;
	
	public ProBenEvaluate(ProBenData theData, String theMethodName, String theTrainingName, String theMethodArchitecture, String theTrainingArgs) {
		this.data = theData;
		this.methodName = theMethodName;
		this.trainingName = theTrainingName;
		this.methodArchitecture = theMethodArchitecture;
		this.trainingArgs = theTrainingArgs;
	}
	
	public void evaluate() {
		
			MLMethodFactory methodFactory = new MLMethodFactory();		
			MLMethod method = methodFactory.create(methodName, methodArchitecture, 
					data.getInputCount(), data.getIdealCount());
			
			MLTrainFactory trainFactory = new MLTrainFactory();	
			MLTrain train = trainFactory.create(method,data.getTrainingDataSet(),trainingName,trainingArgs);
			
			// reset if improve is less than 1% over 5 cycles
			if( method instanceof MLResettable && !(train instanceof ManhattanPropagation) ) {
				train.addStrategy(new RequiredImprovementStrategy(500));
			}

			
		this.iterations = 0;
		do {
			//System.out.println(this.iterations + " - " + train.getError());
			train.iteration();
			this.iterations++;
		} while (train.getError() > 0.01);
		
		MLError calc = (MLError)train.getMethod();
		this.trainError = calc.calculateError(data.getTrainingDataSet());
		this.testError = calc.calculateError(data.getTestDataSet());
		this.validationError = calc.calculateError(data.getValidationDataSet());
		
		System.out.println(data.getName() + "; Iterations=" + iterations + "; Data Size=" + Format.formatInteger((int)data.getTrainingDataSet().getRecordCount()) 
				+ "; Training Error=" + Format.formatPercent(this.trainError) + "; Validation Error=" + Format.formatPercent(this.validationError));
	}
}
