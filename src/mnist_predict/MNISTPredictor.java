package mnist_predict;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import adaBoost.DataInput;
import adaBoost.HardClassifier;

/**
 * Almacena un clasificador fuerte para cada dígito. Permite clasificar una
 * imagen cualquiera, es decir, determinar el dígito que se muestra.
 * 
 * @author jorge
 */
@SuppressWarnings("serial")
public class MNISTPredictor implements Serializable {
	/**
	 * Número de clasificadores fuertes a generar. Elevar este parámetro es
	 * equivalente a elevar la A o la T de HardClassifer
	 */
	public static int ITERACIONES_REFUERZO = 1;
	public MNISTSingleDigitPredictor[] predictors;

	/**
	 * Método auxiliar del constructor. Construye un clasificador fuerte para el
	 * dígito indicado
	 * 
	 * @param digit De 0 a 9
	 */

	/**
	 * Crea 10 clasificadores fuertes entrenados
	 * 
	 * @param percentage Porcentaje de entrenamiento con el que generar los
	 *                   clasificadores fuertes
	 */
	public MNISTPredictor(int percentage, int T) {
		predictors = new MNISTSingleDigitPredictor[10];
		for (int i = 0; i <= 9; i++) {
			DataInput X = new DataInput(i, 0, percentage, true);
			HardClassifier bestHardClassifier = new HardClassifier();
			float bestAccuracy = Float.NEGATIVE_INFINITY;
			for (int j = 0; j < MNISTPredictor.ITERACIONES_REFUERZO; j++) {
				HardClassifier thisHardClassifier = new HardClassifier(X, T);
				float thisAccuracy = new MNISTSingleDigitPredictor(thisHardClassifier).classify(X);
				System.out.print("para el dígito " + i + "!\n");
				if (thisAccuracy > bestAccuracy) {
					bestAccuracy = thisAccuracy;
					bestHardClassifier = thisHardClassifier;
				}
			}
			predictors[i] = new MNISTSingleDigitPredictor(bestHardClassifier);
		}
		System.out.println(getConfidence(0, percentage) * 100 + "% de acierto en general sobre el conjunto de "
				+ "entrenamiento!");
		System.out.println(
				getConfidence(percentage, 100) * 100 + "% de acierto en general sobre " + "el conjunto de test!");
	}

	/**
	 * Lee del fichero binario todos los predictores (clasificadores fuertes)
	 * 
	 * @param filename Ruta al fichero binario
	 * @throws FileNotFoundException Si no existe el fichero
	 */
	public MNISTPredictor(String filename) throws FileNotFoundException {
		FileInputStream fis;
		predictors = new MNISTSingleDigitPredictor[10];
		try {
			fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			for (int i = 0; i <= 9; i++)
				predictors[i] = (MNISTSingleDigitPredictor) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Guarda en un fichero todos los clasificadores fuertes. Éstos se componen de
	 * clasificadores débiles y de las alphas. Los clasificadores débiles guardan el
	 * píxel que toman en cuenta, el umbral y si el brillo ha de estar por encima o
	 * por debajo del umbral. Se guarda como fichero binario.
	 * 
	 * @param filename Nombre del fichero
	 */
	public void saveToFile(String filename) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for (int i = 0; i <= 9; i++)
				oos.writeObject(predictors[i]);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Función sigmoidea
	 * 
	 * @param x
	 * @return valor entre 0 y 1
	 */
	public static float sigmoid(double x) {
		return (float) (1 / (1 + Math.pow(Math.E, (-1 * x))));
	}

	/**
	 * Lee la imagen específicada en la ruta y pasa todos los predictores para luego
	 * determinar el dígito.
	 * 
	 * @param filename Ruta a la imagen de prueba
	 * @return Mensaje en el que se indica de qué número el clasificador cree que se
	 *         trata la imagen y con qué grado de confianza (sigmoidea)
	 */
	public String testImage(String filename) {
		DataInput X = new DataInput(filename, true);
		float certainty = 0;
		int digit = 0;
		for (int i = 0; i <= 9; i++) {
			float thisCertainty = predictors[i].getConfidence(X);
			if (thisCertainty >= certainty) {
				certainty = thisCertainty;
				digit = i;
			}
		}
		return "Creo que es un " + digit + " con un " + sigmoid(certainty) * 100 + "% " + "de confianza";
	}

	/**
	 * Deduce de qué número se trata
	 * 
	 * @param X Dato de entrada m=1 n =784
	 * @return de 0 a 9
	 */
	private int guessDigit(DataInput X) {
		double certainty = Double.NEGATIVE_INFINITY;
		int digit = 0;
		for (int i = 0; i <= 9; i++) {
			double thisCertainty = predictors[i].getConfidence(X);
			if (thisCertainty >= certainty) {
				certainty = thisCertainty;
				digit = i;
			}
		}
		return digit;
	}

	/**
	 * Calcula el porcentaje (de 0 a 100) de aciertos
	 * 
	 * @param loPercentage límite bajo en porcentaje
	 * @param hiPercentage límite alto en porcentaje
	 * @return float entre 0 y 1.
	 */
	public float getConfidence(int loPercentage, int hiPercentage) {
		int total_images = 0;
		int right_guesses = 0;
		for (int i = 0; i <= 9; i++) {
			DataInput X = new DataInput(i, loPercentage, hiPercentage, false);
			total_images += X.getM();
			for (int j = 0; j < X.getM(); j++) {
				int guessedDigit = guessDigit(new DataInput(X.getData()[j]));
				if (i == guessedDigit)
					right_guesses++;
			}
		}
		return (float) ((float) right_guesses) / ((float) (total_images));
	}
}
