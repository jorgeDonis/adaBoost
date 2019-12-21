package adaBoost;

import java.io.Serializable;

/**
 * Implementación de LightClassifier para el caso de las imagenes MNIST.
 * Se fija en un píxel de la imagen y según su nivel de brillo, determina 
 * si pertenece a la clase o no.
 * @author jorge
 *
 */
@SuppressWarnings("serial")
public class PixelBrightnessThreshholdClassifier extends LightClassifier implements Serializable {
	/**
	 * Píxel de la imagen que cabe analizar
	 */
	int pixelPos;

	/**
	 * El brillo tiene que ser mayor o menor que este dato
	 */
	int threshold;
	/**
	 * Indica si el brillo del píxel tiene que ser mayor o menor
	 * para pertenecer a la clase
	 */
	boolean mustBeHigher;

	/**
	 * Inicializa los valores del clasificador de manera
	 * aleatoria
	 */
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

	/**
	 * Discretiza el valor dentro del conjunto {-1, 1} para que adaboost ajuste
	 * alpha acordemente
	 * 
	 * @param x valor entero
	 * @return -1 si x es menor estricto que 0, 1 de lo contrario
	 */
	private int discretizar(int x) {
		if (x < 0)
			return -1;
		else
			return 1;
	}

	@Override
	public int[] predict(DataInput X) {
		int[] predictions = new int[X.getM()];
		for (int i = 0; i < predictions.length; i++) {
			int absolutePrediction = threshold - X.getData()[i][pixelPos];
			if (mustBeHigher)
				absolutePrediction *= -1;
			predictions[i] = discretizar(absolutePrediction);
		}
		return predictions;
	}

	private void assignParametres(PixelBrightnessThreshholdClassifier pix) {
		this.threshold = pix.threshold;
		this.mustBeHigher = pix.mustBeHigher;
		this.pixelPos = pix.pixelPos;
	}

}
