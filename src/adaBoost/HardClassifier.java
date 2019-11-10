package adaBoost;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import twoD.Constants;

public class HardClassifier {

	// número de clasificadores débiles
	static final int T = 3;

	// iteraciones de aprendizaje para cada clasificador débil
	static final int A = 10000000;

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
	private LightClassifier[] lightClassifiers;

	// lista de 'alfas' asociadas a cada clasificador débil
	private double[] alphas = new double[T];
	
	private void debug() {
		twoDLightClassifier lc = new twoDLightClassifier();
		lc.vertical = true;
		lc.isClassBigger = false;
		lc.threshhold = 2;
		lightClassifiers[0] = lc;
		
		
		lc = new twoDLightClassifier();
		lc.vertical = true;
		lightClassifiers[1] = lc;
		lc.threshhold = 7;
		lc.isClassBigger = false;
		
		lc = new twoDLightClassifier();
		lc.vertical = false;
		lc.isClassBigger = true;
		lc.threshhold = 4;
		lightClassifiers[2] = lc;
		
	}

	public HardClassifier(DataInput X) {
		initD(X.getM());
		lightClassifiers = new LightClassifier[T];
		for (int t = 0; t < T; t++) {
			LightClassifier lc = new twoDLightClassifier3();
			lightClassifiers[t] = lc;
			lc.train(X, D, A);
			double epsilon = lc.getError(X, D);
			double alpha = 0.5 * Math.log((double)(1 - epsilon) / epsilon);
			alphas[t] = alpha;
			updateD(alpha, lc.predict(X), X);
		}
	}

	private void writeOutputFile(char[][] output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.classifiedDataFile));
		for (int i = 0; i < Constants.height; i++) {
			for (int j = 0; j < Constants.width; j++) {
				writer.append(output[i][j]);
			}
			writer.append('\n');
		}
		writer.close();
	}

	private void superposeOriginalData(char[][] output) throws IOException {
		DataInput trainingData = new DataInput(Constants.generatedDataOutputFile);
		for (int i = 0; i < trainingData.getM(); i++) {
			int x = trainingData.getData()[i][0];
			int y = trainingData.getData()[i][1];
			if (trainingData.getClassification()[i])
				output[Constants.height - 1 - y][x] = Constants.isClass;
			else
				output[Constants.height - 1 - y][x] = Constants.notClass;
		}
	}

	public void classify(DataInput X) throws IOException {
		double[] hardPredictions = new double[X.getM()];
		for (int t = 0; t < T; t++) {
			LightClassifier lc = lightClassifiers[t];
			double[] lightPredictions = lc.predict(X);
			for (int i = 0; i < X.getM(); i++)
				hardPredictions[i] = hardPredictions[i] + (lightPredictions[i] * alphas[t]);
		}
		char[][] output = new char[Constants.height][Constants.width];
		for (int m = 0; m < X.getM(); m++) {
			int[] coordinate = X.getData()[m];
			if (hardPredictions[m] >= 0)
				output[Constants.height - 1 - coordinate[1]][coordinate[0]] = Constants.predictIsClass;
			else
				output[Constants.height - 1 - coordinate[1]][coordinate[0]] = Constants.outOfDomain;
		}
		superposeOriginalData(output);
		writeOutputFile(output);
	}
}
