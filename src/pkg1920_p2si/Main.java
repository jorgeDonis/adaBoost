package pkg1920_p2si;

import java.io.IOException;
import adaBoost.DataInput;
import mnist_predict.MNISTPredictor;

/**
 * @author Jorge Donis del Álamo
 */
public class Main {
	
	static int TrainingPercentage = 30;

	/**
	 * @param args the command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		DataInput.ml.loadDBFromPath("./mnist_1000");
		MNISTPredictor predictor = new MNISTPredictor(TrainingPercentage);
		predictor.saveToFile("adaboost.brain");
		System.out.println("Confianza = " + predictor.getConfidence(TrainingPercentage));
	}

}
