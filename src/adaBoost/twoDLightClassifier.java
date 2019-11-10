package adaBoost;

import java.util.Random;
import twoD.Constants;



public class twoDLightClassifier extends LightClassifier {
	private int threshhold;
	private boolean vertical;
	private boolean isClassBigger;
	
	public void init() {
		if (vertical = rng.nextBoolean())
			threshhold = rng.nextInt(Constants.height);
		else
			threshhold = rng.nextInt(Constants.width);
		isClassBigger = rng.nextBoolean();
	}

	// crear un clasificador aleatorio de 2 dimensiones
	public twoDLightClassifier() {
		super();
	}

	// devuelve una lista de fiabilidad de pertenencia respecto de una lista de
	// datos
	@Override
	public double[] predict(DataInput X) {
		double[] predictions = new double[X.getM()];
		for (int i = 0; i < predictions.length; i++) {
			int x = X.getData()[i][0];
			int y = X.getData()[i][1];
			if (vertical) {
				if (isClassBigger)
					predictions[i] = x - threshhold;
				else
					predictions[i] = threshhold - x;
			}
			else {
				if (isClassBigger)
					predictions[i] = y - threshhold;
				else
					predictions[i] = threshhold - y;
			}
		}
		return predictions;
	}
	
	private void assignParametres(twoDLightClassifier lc) {
		this.threshhold = lc.threshhold;
		this.vertical = lc.vertical;
		this.isClassBigger = lc.isClassBigger;
	}

	@Override
	public void train(DataInput X, double[] D, int A) {
		double minEpsilon = Double.POSITIVE_INFINITY;
		twoDLightClassifier bestLightClassifier = new twoDLightClassifier();
		for (int i = 0; i < A; i++) {
			init();
			double epsilon = super.getError(X, D);
			if (epsilon <= minEpsilon) {
				minEpsilon = epsilon;
				bestLightClassifier.assignParametres(this);
			}
		}
		assignParametres(bestLightClassifier);
	}
	
	public twoDLightClassifier(twoDLightClassifier twoD) {
		super();
		assignParametres(twoD);
	}
}
