package adaBoost;

import java.util.Random;

public abstract class LightClassifier {
	
	public static Random rng = new Random();
	
	public LightClassifier() {
		super();
	}
	
	public abstract void train(DataInput X, double[] D, int A);
	
	public double getError(DataInput X, double[] D) {
		double epsilon = 0;
		double[] predictions = this.predict(X);
		for (int i = 0; i < predictions.length; i++) {
			double compareWith = 0;
			if (X.getClassification()[i])
				compareWith = 1;
			else
				compareWith = -1;
			if (predictions[i] * compareWith < 0)
				epsilon += D[i];
		}
		return epsilon;
	}
	public abstract double[] predict(DataInput X);
}
