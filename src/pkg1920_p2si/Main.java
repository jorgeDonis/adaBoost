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

	static int trainingPercentage = 90;
	private static boolean trainingMode;
	private static String brainLocation;
	private static String testImageLocation;
	private static int T = 800;

	public static void main(String[] args) throws IOException {
		HardClassifier.A = 1000;
		DataInput.ml.loadDBFromPath("mnist_1000");
		MNISTPredictor predictor = new MNISTPredictor(trainingPercentage, T);
		System.out.println("Anton was born with " + predictor.getConfidence(trainingPercentage, 100)
		+ "% success rate!");
		predictor.saveToFile("sonofanton.brain");
	}
}
