package pkg1920_p2si;

import java.io.FileNotFoundException;
import java.io.IOException;
import adaBoost.DataInput;
import adaBoost.HardClassifier;
import mnist_predict.MNISTPredictor;

/**
 * @author Jorge Donis del Álamo
 */
public class Main {

	static int trainingPercentage = 80;

	/**
	 * @param args the command line arguments
	 * @throws IOException
	 */

	private static boolean trainingMode;
	private static String brainLocation;
	private static String testImageLocation;
	private static int T = 200;

	private static void parseArgs(String[] args) {
		if (args.length != 2) {
			System.err.println("Error de syntaxis. Ejemplos de uso:\n"
					+ "Adaboost –t <fichero_almacenamiento_clasificadores_fuertes>\n"
					+ "Adaboost <fichero_origen_clasificador_fuerte> <ruta_imagen_prueba_externa>\n");
			System.exit(1);
		}

		else {
			if (args[0].equals("-t")) {
				trainingMode = true;
				brainLocation = args[1];
			} else {
				trainingMode = false;
				brainLocation = args[0];
				testImageLocation = args[1];
			}
		}
	}

	public static void main(String[] args) throws IOException {
		MNISTPredictor predictor;
		HardClassifier.A = 500;
		parseArgs(args);
		if (trainingMode) {
			System.out.println("[MODO ENTRENAMIENTO]");
			System.out.println("Porcentaje de entrenamiento = " + trainingPercentage);
			System.out.println("Iteraciones de refuerzo = " + MNISTPredictor.ITERACIONES_REFUERZO);
			System.out.println("A = " + HardClassifier.A);
			System.out.println("T = " + T);
			DataInput.ml.loadDBFromPath("./mnist_1000");
			predictor = new MNISTPredictor(trainingPercentage, T);
			predictor.saveToFile(brainLocation);
		} else {
			try {
				predictor = new MNISTPredictor(brainLocation);
				System.out.println(predictor.testImage(testImageLocation));
			} catch (FileNotFoundException e) {
				System.out.println("No se pudo encontrar el fichero del clasificador!");
			}
		}
	}

}
