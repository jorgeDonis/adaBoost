package twoD;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import adaBoost.DataInput;
import adaBoost.HardClassifier;
import adaBoost.LightClassifier;

public class Predictor2D {
	
	private HardClassifier hc;
	
	public Predictor2D(HardClassifier hc) {
		this.hc = hc;
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

	public void classify2D(DataInput X) throws IOException {
		double[] hardPredictions = hc.predict(X);
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
	
	public void intersect(DataInput X) throws IOException {
		boolean[] agree = new boolean[X.getM()];
		for (int i = 0; i < X.getM(); i++)
			agree[i] = true;
		for (int t = 0; t < HardClassifier.T; t++) {
			LightClassifier lc = hc.lightClassifiers[t];
			double[] lightPredictions = lc.predict(X);
			for (int i = 0; i < X.getM(); i++)
				if (lightPredictions[i] < 0)
					agree[i] = false;
		}
		char[][] output = new char[Constants.height][Constants.width];
		for (int m = 0; m < X.getM(); m++) {
			int[] coordinate = X.getData()[m];
			if (agree[m])
				output[Constants.height - 1 - coordinate[1]][coordinate[0]] = Constants.predictIsClass;
			else
				output[Constants.height - 1 - coordinate[1]][coordinate[0]] = Constants.outOfDomain;
		}
		superposeOriginalData(output);
		writeOutputFile(output);
	}
}
