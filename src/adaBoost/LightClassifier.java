package adaBoost;

import java.io.Serializable;
import java.util.Random;

/**
 * Clasificador débil. Tiene una tasa de aciertos superior al 50%
 * @author jorge
 */
@SuppressWarnings("serial")
public abstract class LightClassifier implements Serializable {
	public static Random rng = new Random();
	
	public LightClassifier() {
		super();
	}
	
	/**
	 * Entrena el clasificador débil
	 * @param X conjunto de entrenamiento
	 * @param D vector de pesos (distribución probabilística)
	 * @param A Número de iteraciones (generará A clasificadores débiles aleatorios)
	 */
	public abstract void train(DataInput X, double[] D, int A);
	
	/**
	 * Calcula el error según la D
	 * @param X conjunto de entrenamiento
	 * @param D vector de pesos
	 * @return epsilon = sumatorio de D[i] para todas las X[i] donde la clasificación real
	 * difiere respecto de la predicha por el clasificador débil
	 */
	public double getError(DataInput X, double[] D) {
		double epsilon = 0;
		int[] predictions = this.predict(X);
		for (int i = 0; i < predictions.length; i++) {
			int compareWith = 0;
			if (X.getClassification()[i])
				compareWith = 1;
			else
				compareWith = -1;
			if (predictions[i] * compareWith < 0)
				epsilon += D[i];
		}
		return epsilon;
	}
	public abstract int[] predict(DataInput X);
}
