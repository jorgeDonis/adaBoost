package adaBoost;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import twoD.Constants;

public class HardClassifier {

	// número de clasificadores débiles
	public static final int T = 10;

	// iteraciones de aprendizaje para cada clasificador débil
	public static final int A = 1000;

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

	private void updateD(double alpha, double[] predictions, DataInput X) {
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
	public double[] alphas = new double[T];

	public HardClassifier(DataInput X) {
		initD(X.getM());
		lightClassifiers = new LightClassifier[T];
		for (int t = 0; t < T; t++) {
			LightClassifier lc = new PixelBrightnessThreshholdClassifier();
			lc.train(X, D, A);
			lightClassifiers[t] = lc;
			double epsilon = lc.getError(X, D);
			double alpha = 0.5 * Math.log((double)(1 - epsilon) / epsilon);
			alphas[t] = alpha;
			updateD(alpha, lc.predict(X), X);
		}
	}
	
	public double[] predict(DataInput X) {
		double[] hardPredictions = new double[X.getM()];
		for (int t = 0; t < HardClassifier.T; t++) {
			LightClassifier lc = lightClassifiers[t];
			double[] lightPredictions = lc.predict(X);
			for (int i = 0; i < X.getM(); i++)
				hardPredictions[i] = hardPredictions[i] + (lightPredictions[i] * alphas[t]);
		}
		return hardPredictions;
	}

}
