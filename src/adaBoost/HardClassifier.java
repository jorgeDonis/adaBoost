package adaBoost;

import java.io.Serializable;

/**
 * Dada una imagen, puede predecir se pertenece a una clase o no. Según los
 * datos de entrenamiento clasificará un número u otro. Se puede entrenar para
 * clasificar 0's, 1's...
 * 
 * @author jorge
 */
@SuppressWarnings("serial")
public class HardClassifier implements Serializable {
	// número de clasificadores débiles
	public int T;

	// iteraciones de aprendizaje para cada clasificador débil
	public static int A = 800;

	public HardClassifier() {

	}

	// vector de pesos de cada dato
	private double[] D;

	private void initD(int m) {
		D = new double[m];
		double equiProb = (double) 1 / m;
		for (int i = 0; i < m; i++)
			D[i] = equiProb;
	}

	public static double log2(double x) {
		return (double) (Math.log(x) / Math.log(2));
	}

	private void updateD(double alpha, int[] predictions, DataInput X) {
		double sigma = 0;
		for (int i = 0; i < D.length; i++) {
			double coeff;
			double compareWith = 0;
			if (X.getClassification()[i])
				compareWith = 1;
			else
				compareWith = -1;
			if (compareWith * predictions[i] < 0)
				coeff = Math.exp(alpha);
			else
				coeff = Math.exp(-alpha);
			sigma += D[i] *= coeff;
		}
		double normalizeFactor = 1 / sigma;
		for (int i = 0; i < D.length; i++)
			D[i] *= normalizeFactor;
	}

	// lista de clasificadores débiles
	public LightClassifier[] lightClassifiers;

	// lista de 'alfas' asociadas a cada clasificador débil
	public double[] alphas;

	/**
	 * Crea el clasificador fuerte según el algoritmo adaboost para clasificar
	 * correctamente el conjunto de entrenamiento
	 * 
	 * @param X Conjunto de entrenamiento
	 */
	public HardClassifier(DataInput X, int T) {
		this.T = T;
		this.alphas = new double[T];
		initD(X.getM());
		lightClassifiers = new LightClassifier[T];
		for (int t = 0; t < T; t++) {
			LightClassifier lc = new PixelBrightnessThreshholdClassifier();
			lc.train(X, D, A);
			lightClassifiers[t] = lc;
			double epsilon = lc.getError(X, D);
			double alpha;
			alpha = (double) 0.5 * (double) Math.log((double) (double) (1 - epsilon) / (double) epsilon);
			alphas[t] = alpha;
			updateD(alpha, lc.predict(X), X);
		}
	}

	/**
	 * Dado la entrada, el clasificado calcula su hipótesis
	 * 
	 * @param X Conjunto de datos de prueba
	 * @return Vector de double con el sumatorio de las predicciones de los clasificadores
	 * débiles multiplicados por sus respectivas alphas (grados de confianza)
	 */
	public double[] predict(DataInput X) {
		double[] hardPredictions = new double[X.getM()];
		for (int t = 0; t < T; t++) {
			LightClassifier lc = lightClassifiers[t];
			int[] lightPredictions = lc.predict(X);
			for (int i = 0; i < X.getM(); i++)
				hardPredictions[i] = (hardPredictions[i] + (lightPredictions[i] * alphas[t]));
		}
		return hardPredictions;
	}
}
