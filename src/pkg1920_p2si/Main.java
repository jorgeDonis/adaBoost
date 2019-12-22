package pkg1920_p2si;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import adaBoost.DataInput;
import adaBoost.HardClassifier;
import mnist_predict.MNISTPredictor;

/**
 * @author Jorge Donis del Álamo
 */
public class Main {

	static int trainingPercentage = 10;
	private static boolean trainingMode;
	private static String brainLocation;
	private static String testImageLocation;
	private static int T = 3;

	
	public static void main(String[] args) throws IOException {
		FileWriter fileWriter = new FileWriter("tests/A.test");
	    PrintWriter printWriter = new PrintWriter(fileWriter);	
		MNISTPredictor predictor;
		DataInput.ml.loadDBFromPath("mnist_1000");
		for (int i = 1; i < 1000; i++) {
			HardClassifier.A = i;
			predictor = new MNISTPredictor(trainingPercentage, T);
			float precision = (predictor.getConfidence(trainingPercentage, 100));
			String precisionstr = String.valueOf(precision);
			printWriter.println(i + " " + precisionstr);
			System.out.println(precisionstr);
			if (precision > 80)
				break;
		}
		printWriter.close();
	}
}
