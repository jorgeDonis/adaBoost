package adaBoost;

import twoD.Constants;

class Coordinate {

	int x, y;

	public Coordinate(int xmax, int ymax) {
		x = LightClassifier.rng.nextInt(xmax);
		y = LightClassifier.rng.nextInt(ymax);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}

public class twoDLightClassifier3 extends LightClassifier {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double a, b, c;
	private double sign;

	public void init() {
		Coordinate p1, p2;
		while (true) {
			p1 = new Coordinate(Constants.width, Constants.height);
			p2 = new Coordinate(Constants.width, Constants.height);
			if (!p1.equals(p2))
				break;
		}
		a = p1.y - p2.y;
		b = p2.x - p1.x;
		c = -p1.y * p2.x + p1.x * p2.y;
		if (rng.nextInt(2) == 1)
			sign = -1;
		else
			sign = 1;
	}

	// crear un clasificador aleatorio de 2 dimensiones
	public twoDLightClassifier3() {
		super();
	}

	// devuelve una lista de fiabilidad de pertenencia respecto de una lista de
	// datos
	@Override
	public int[] predict(DataInput X) {
		int[] predictions = new int[X.getM()];
		for (int i = 0; i < predictions.length; i++) {
			int x = X.getData()[i][0];
			int y = X.getData()[i][1];
			predictions[i] = (int) ((a * x + b * y + c) * sign);
		}
		return predictions;
	}

	private void assignParametres(twoDLightClassifier3 lc) {
		this.a = Double.valueOf(lc.a);
		this.b = Double.valueOf(lc.b);
		this.c = Double.valueOf(lc.c);
		this.sign = Double.valueOf(lc.sign);
	}

	@Override
	public void train(DataInput X, double[] D, int A) {
		double minEpsilon = Double.POSITIVE_INFINITY;
		twoDLightClassifier3 bestLightClassifier = new twoDLightClassifier3();
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

	public twoDLightClassifier3(twoDLightClassifier3 twoD) {
		super();
		assignParametres(twoD);
	}
}
