package org.encog.examples.neural.gui.mpg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.encog.ConsoleStatusReportable;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.normalize.DataNormalization;
import org.encog.normalize.input.BasicInputField;
import org.encog.normalize.input.InputField;
import org.encog.normalize.input.InputFieldCSV;
import org.encog.normalize.output.OutputField;
import org.encog.normalize.output.OutputFieldRangeMapped;
import org.encog.normalize.target.NormalizationStorageNeuralDataSet;
import org.encog.persist.EncogPersistedCollection;
import org.encog.persist.EncogPersistedObject;
import org.encog.util.simple.EncogUtility;

public class MilesPerGallon extends JFrame implements ActionListener, Runnable {
	
	private JButton buttonGenerate;
	private JButton buttonTrain;
	private JButton buttonEvaluate;
	private JTextField textPath;
	private JTextField textHP;
	private JTextField textDisp;
	private JTextField textWeight;
	private JTextField textCyl;
	private JTextField textAccel;
	private File fileCSV;
	private File directory;
	private File encogFile;
	private boolean training;
	private CalculateMPG calc;
	JLabel labelMPG;
	
	public MilesPerGallon()
	{
		setTitle("Neural Network Miles Per Gallon");
		this.setSize(320,200);
		Container content = this.getContentPane();
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(buttonGenerate = new JButton("Generate"));
		buttonPanel.add(buttonTrain = new JButton("Train"));
		buttonPanel.add(buttonEvaluate = new JButton("Evaluate"));
		content.setLayout(new BorderLayout());
		content.add(buttonPanel,BorderLayout.SOUTH);
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(7,2));
		content.add(gridPanel,BorderLayout.CENTER);
		gridPanel.add(new JLabel("Cylinders"));
		gridPanel.add(this.textCyl = new JTextField("6"));
		gridPanel.add(new JLabel("Displacement (cu.inch)"));		
		gridPanel.add(this.textDisp = new JTextField("183"));
		gridPanel.add(new JLabel("Horse Power"));
		gridPanel.add(this.textHP = new JTextField("230"));
		gridPanel.add(new JLabel("Weight (lbs)"));
		gridPanel.add(this.textWeight = new JTextField("2300"));
		gridPanel.add(new JLabel("Acceleration (0-60 mph)"));
		gridPanel.add(this.textAccel = new JTextField("6"));
		gridPanel.add(new JLabel("Data file"));
		gridPanel.add(this.textPath=new JTextField("c:\\mpg\\mpg.csv"));
		gridPanel.add(new JLabel("Vehicle MPG"));
		gridPanel.add(labelMPG = new JLabel("Click to Calc"));
		buttonTrain.addActionListener(this);
		buttonGenerate.addActionListener(this);
		buttonEvaluate.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
		
	public static void main(String[] args)
	{
		MilesPerGallon frame = new MilesPerGallon();
		frame.setVisible(true);
	}
	
	private double parseField(JTextField field, String name) throws Exception
	{
		try
		{
			double result = Double.parseDouble(field.getText());
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("Please enter valid value: "+name);
		}
	}

	public void actionPerformed(ActionEvent event) {
		if( event.getSource()==this.buttonEvaluate ) {
			performEvaluate();
		}
		else if( event.getSource()==this.buttonTrain ) {
			performTrain();
		}
		else if( event.getSource()==this.buttonGenerate ) {
			performBuild();
		}
		
	}
	
	private void message(String text)
	{
		JOptionPane.showMessageDialog(null, text);

	}
	
	private void obtainPaths()
	{
		String filenameCSV = this.textPath.getText();
		this.fileCSV = new File(filenameCSV);
		
		if( !fileCSV.exists() )
		{
			message("Can't load: " + fileCSV);
		}
		
		this.directory = fileCSV.getParentFile();
		
		if( !directory.exists() )
		{
			message("Can't load: " + directory);
		}
		
		this.encogFile = new File(this.directory,"mpg.eg");
	}

	private void performBuild() {
		InputField inputMPG;
		InputField inputCylinders;
		InputField inputDisplacement;
		InputField inputHorsePower;
		InputField inputWeight;
		InputField inputAcceleration;
		
		final double lo = -0.5;
		final double hi = 0.5;
		
		obtainPaths();
				
		DataNormalization norm = new DataNormalization();
		norm.addInputField(inputMPG = new InputFieldCSV(false,fileCSV,0));
		norm.addInputField(inputCylinders = new InputFieldCSV(true,fileCSV,1));
		norm.addInputField(inputDisplacement = new InputFieldCSV(true,fileCSV,2));
		norm.addInputField(inputHorsePower = new InputFieldCSV(true,fileCSV,3));
		norm.addInputField(inputWeight = new InputFieldCSV(true,fileCSV,4));
		norm.addInputField(inputAcceleration = new InputFieldCSV(true,fileCSV,5));
		
		OutputField mpg;
		norm.addOutputField(new OutputFieldRangeMapped(inputCylinders,lo,hi));
		norm.addOutputField(new OutputFieldRangeMapped(inputDisplacement,lo,hi));
		norm.addOutputField(new OutputFieldRangeMapped(inputHorsePower,lo,hi));
		norm.addOutputField(new OutputFieldRangeMapped(inputWeight,lo,hi));
		norm.addOutputField(new OutputFieldRangeMapped(inputAcceleration,lo,hi));
		norm.addOutputField(mpg = new OutputFieldRangeMapped(inputMPG,lo,hi));
		mpg.setIdeal(true);
		
		NormalizationStorageNeuralDataSet target = new NormalizationStorageNeuralDataSet(5,1);
		
		norm.setReport(new ConsoleStatusReportable());
		norm.setTarget(target);
				
		norm.process();
		
		EncogPersistedCollection encog = new EncogPersistedCollection(this.encogFile);
		encog.add("data", (EncogPersistedObject)target.getDataset());
		BasicNetwork network = EncogUtility.simpleFeedForward(5, 7, 0, 1, true);
		encog.add("network", network);
		encog.add("norm", norm);
		message("Success. Done processing CSV file.");
		
	}

	private void performTrain() {
		if( !training )
		{
			training = true;
			Thread t = new Thread(this);
			t.start();
		}
		else
		{
			message("Already training");
			
		}
	}

	private void performEvaluate() {
		try {
			obtainPaths();
			if (calc == null) {
				calc = new CalculateMPG(encogFile);
			}
			
			double cylinders = this.parseField(this.textCyl, "cylinders");
			double displacement = this.parseField(this.textDisp, "displacement");
			double horsePower = this.parseField(this.textHP, "horsePower");
			double weight = this.parseField(this.textWeight, "weight");
			double acceleration = this.parseField(this.textAccel, "acceleration");
	
			
			double mpg = calc.calulate(
					cylinders,
					displacement,
					horsePower, 
					weight,
					acceleration);
			this.labelMPG.setText(""+mpg);
		} catch (Exception e) {
			message(e.getMessage());
		}
	}

	public void run() {
		obtainPaths();
		calc = null;
		EncogPersistedCollection encog = new EncogPersistedCollection(this.encogFile);
		BasicNetwork network = (BasicNetwork)encog.find("network");
		NeuralDataSet trainingSet = (NeuralDataSet)encog.find("data");
		EncogUtility.trainDialog(network, trainingSet);		
		encog.add("network", network);	
		training = false;
	}
}
