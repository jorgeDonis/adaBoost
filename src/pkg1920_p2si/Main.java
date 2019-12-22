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

	static int trainingPercentage = 30;
	private static boolean trainingMode;
	private static String brainLocation;
	private static String testImageLocation;
	private static int T = 100;

	public static void main(String[] args) throws IOException {
		FileWriter fileWriter = new FileWriter("tests/overfit2.test");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		MNISTPredictor predictor;
		HardClassifier.A = 1000;
		DataInput.ml.loadDBFromPath("mnist_1000");
		for (int i = 1; i < 1000; i+=10) {
			predictor = new MNISTPredictor(trainingPercentage, i);
			float aciertosEntrenamiento = predictor.getConfidence(0, trainingPercentage);
			float aciertosTest = predictor.getConfidence(trainingPercentage, 100);
			printWriter.println(i + " " + String.valueOf(aciertosEntrenamiento) + " " 
			+ String.valueOf(aciertosTest));
		}
		printWriter.close();
	}
}
