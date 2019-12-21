package pkg1920_p2si;

import java.io.IOException;
import adaBoost.DataInput;
import mnist_predict.MNISTPredictor;

/**
 * @author Jorge Donis del Álamo
 */
public class Main {
	
	static int TrainingPercentage = 1;

	/**
	 * @param args the command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		DataInput.ml.loadDBFromPath("./mnist_1000");
		MNISTPredictor predictor = new MNISTPredictor("adaboost_89.brain");
//		predictor.saveToFile("adaboost.brain");
		System.out.println("Confianza = " + predictor.getConfidence(TrainingPercentage) * 100 + "%");
//		System.out.println(predictor.testImage("/home/jorge/eclipse-workspace/adaBoostEntregar/mnist_1000/d9/12029.png"));
	}

}
