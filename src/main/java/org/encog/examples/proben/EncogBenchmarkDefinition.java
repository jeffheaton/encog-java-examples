package org.encog.examples.proben;

import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
import org.encog.ml.train.strategy.end.SimpleEarlyStoppingStrategy;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;

public class EncogBenchmarkDefinition implements BenchmarkDefinition {

	private String methodName;
	private String trainingName;
	private String methodArchitecture;
	private String trainingArgs;
	private String probenPath;
	private boolean shouldCenter;
	private double inputCenter;
	private double outputCenter;

	public EncogBenchmarkDefinition(String theProbenPath, String theMethodName,
			String theTrainingName, String theMethodArchitecture,
			String theTrainingArgs) {
		this(theProbenPath, theMethodName, theTrainingName,
				theMethodArchitecture, theTrainingArgs, false, 0, 0);
	}

	public EncogBenchmarkDefinition(String theProbenPath, String theMethodName,
			String theTrainingName, String theMethodArchitecture,
			String theTrainingArgs, boolean theShouldCenter,
			double theInputCenter, double theOutputCenter) {
		this.methodName = theMethodName;
		this.trainingName = theTrainingName;
		this.methodArchitecture = theMethodArchitecture;
		this.trainingArgs = theTrainingArgs;
		this.probenPath = theProbenPath;
		this.inputCenter = theInputCenter;
		this.outputCenter = theOutputCenter;
	}

	@Override
	public MLMethod createMethod(ProBenData data) {
		MLMethodFactory methodFactory = new MLMethodFactory();
		MLMethod method = methodFactory.create(methodName, methodArchitecture,
				data.getInputCount(), data.getIdealCount());
		return method;
	}

	@Override
	public MLTrain createTrainer(MLMethod method, ProBenData data) {
		MLTrainFactory trainFactory = new MLTrainFactory();
		MLTrain train = trainFactory.create(method, data.getTrainingDataSet(),
				trainingName, trainingArgs);

		train.addStrategy(new SimpleEarlyStoppingStrategy(data
				.getValidationDataSet(), 50));
		// reset if improve is less than 1% over 5 cycles
		if (method instanceof MLResettable
				&& !(train instanceof ManhattanPropagation)) {
			train.addStrategy(new RequiredImprovementStrategy(100));
		}
		return train;
	}

	@Override
	public String getProBenFolder() {
		return this.probenPath;
	}

	@Override
	public boolean shouldCenter() {
		return this.shouldCenter;
	}

	@Override
	public double getInputCenter() {
		return this.inputCenter;
	}

	@Override
	public double getOutputCenter() {
		return this.outputCenter;
	}

}
