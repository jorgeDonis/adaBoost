package adaBoost;

public class PixelBrightnessThreshholdClassifier extends LightClassifier {
	
	/*
	 * From 0 to 783, pixel to focus on 
	 */
	int pixelPos;
	
	/**
	 * Brightness has to be higher or lower than this
	 */
	int threshold;
	
	boolean mustBeHigher;
	
	private void init() {
		pixelPos = rng.nextInt(784);
		threshold = rng.nextInt(256);
		mustBeHigher = rng.nextBoolean();
	}

	public PixelBrightnessThreshholdClassifier() {
		super();
	}
	
	public PixelBrightnessThreshholdClassifier(PixelBrightnessThreshholdClassifier pix) {
		super();
		assignParametres(pix);
	}
	
	@Override
	public void train(DataInput X, double[] D, int A) {
		double minEpsilon = Double.POSITIVE_INFINITY;
		PixelBrightnessThreshholdClassifier bestLightClassifier = new PixelBrightnessThreshholdClassifier();
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

	@Override
	public double[] predict(DataInput X) {
		double[] predictions = new double[X.getM()];
		for (int i = 0; i < predictions.length; i++) {
			predictions[i] = threshold - X.getData()[i][pixelPos];
			if (mustBeHigher)
				predictions[i] *= 1;
		}
		return predictions;
	}
	
	private void assignParametres(PixelBrightnessThreshholdClassifier pix) {
		this.threshold = pix.threshold;
		this.mustBeHigher = pix.mustBeHigher;
		this.pixelPos = pix.pixelPos;
	}

}
