package org.encog.examples.proben;

import java.io.File;

import org.encog.util.file.FileUtil;

public class ProBenRunner {
	private File dir;
	private String methodName;
	private String trainingName;
	private String methodArchitecture;
	private String trainingArgs;
	
	public ProBenRunner(File theDir, String theMethodName, String theTrainingName, String theMethodArchitecture, String theTrainingArgs) {
		this.dir = theDir;
		this.methodName = theMethodName;
		this.trainingName = theTrainingName;
		this.methodArchitecture = theMethodArchitecture;
		this.trainingArgs = theTrainingArgs;
	}
	
	public void run() {
		runDirectory(this.dir);
	}
	
	public void runDirectory(File file) {
		
		for(File childFile: file.listFiles()) {
			if( childFile.isDirectory()) {
				runDirectory(childFile);				
			} else {
				if( FileUtil.getFileExt(childFile).equalsIgnoreCase("dt")) {
					runFile(childFile);
				}
			}
		}
		
	}
	
	public void runFile(File file) {
		ProBenData data = new ProBenData(file);
		data.load();
		
		ProBenEvaluate eval = new ProBenEvaluate(data, methodName,trainingName,methodArchitecture,trainingArgs);
		eval.evaluate();
	}
}
