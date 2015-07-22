package org.encog.examples.proben;

import java.io.File;

import org.encog.ml.MLMethod;
import org.encog.ml.train.MLTrain;

public interface BenchmarkDefinition {
	public MLMethod createMethod(ProBenData data);
	public MLTrain createTrainer(MLMethod method, ProBenData data);
	public String getProBenFolder();
	public boolean shouldCenter();
	public double getInputCenter();
	public double getOutputCenter();
}
