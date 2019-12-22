package mnist_predict;

import java.io.Serializable;

import adaBoost.DataInput;
import adaBoost.HardClassifier;

/**
 * Sirve de clasificador para un único dígito. Determina si una imagen
 * es 0 si se entrenó con un conjunto de datos en el que las clases ciertas
 * eran 0's...
 * @author jorge
 *
 */
@SuppressWarnings("serial")
public class MNISTSingleDigitPredictor implements Serializable {

	private HardClassifier hc;

	public MNISTSingleDigitPredictor(HardClassifier hc) {
		this.hc = hc;
	}
	
	public void setClassifier(HardClassifier hc) {
		this.hc = hc;
	}
	
	public MNISTSingleDigitPredictor() {
		
	}

	public float classify(DataInput X) {
		int correctGuess = 0;
		boolean[] realClassifications = X.getClassification();
		double[] predictions = hc.predict(X);
		for (int i = 0; i < X.getM(); i++) {
			if ((predictions[i] > 0 && realClassifications[i]) || (predictions[i] <= 0 && !realClassifications[i])) {
				correctGuess++;
			}
		}
		float precision = (float) ((double) correctGuess / (double) X.getM()) * 100;
		System.out.print(precision + "% precisión sobre el conjunto de entrenamiento ");
		return precision;
	}

	/**
	 * Sólo predice una entrada
	 * 
	 * @param X Entrada de n = 728 y m = 1
	 * @return Cierto si el clasificador fuerte opina que es de la clase
	 */
	public boolean isClass(DataInput X) {
		return (hc.predict(X)[0] > 0);
	}

	/**
	 * Sólo predice una entrada
	 * 
	 * @param X entrada n = 728 m = 1
	 * @return Float con la confianza, según la fórmula de adaboost (alphas y
	 *         clasificadores débiles)
	 */
	public float getConfidence(DataInput X) {
		return (float) hc.predict(X)[0];
	}
}
