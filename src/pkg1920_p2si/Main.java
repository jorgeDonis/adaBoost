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
	private static int T = 50;

	
	public static void main(String[] args) throws IOException {
		FileWriter fileWriter = new FileWriter("tests/tiempos_X.test");
	    PrintWriter printWriter = new PrintWriter(fileWriter);	
		MNISTPredictor predictor;
		HardClassifier.A = 50;
		DataInput.ml.loadDBFromPath("mnist_1000");
		for (int i = 1; i < 50; i++) {
			long start = System.nanoTime();
			predictor = new MNISTPredictor(trainingPercentage, 20);
			long end = System.nanoTime();
			long time = end - start;
			String elapsedtime = String.valueOf(time);
			printWriter.println(i + " " + elapsedtime);
		}
		printWriter.close();
	}
}
