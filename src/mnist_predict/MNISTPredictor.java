package mnist_predict;

import adaBoost.DataInput;
import adaBoost.HardClassifier;
import pkg1920_p2si.Imagen;
import pkg1920_p2si.MostrarImagen;

public class MNISTPredictor {
	
	private HardClassifier hc;
	
	private void showImage(Imagen img) {
		 MostrarImagen imgShow = new MostrarImagen();
		 imgShow.setImage(img);
	     imgShow.mostrar();
	}
	
	public MNISTPredictor(HardClassifier hc) {
		this.hc = hc;
	}
	
	private void showImageFromData() {
		
	}
	
	public float classify(DataInput X) {
		int correctGuess = 0;
		boolean[] realClassifications = X.getClassification();
		double[] predictions = hc.predict(X);
		for (int i = 0; i < X.getM(); i++) {
			if ((predictions[i] > 0 && realClassifications[i])
				|| (predictions[i] <= 0 && !realClassifications[i])) {
				
				correctGuess++;
			}
				
		}
		float precision = (float) ((double)correctGuess / (double)X.getM()) * 100;
		System.out.println( precision + "% accuracy!");
		return precision;
	}
	
	/**
	 * Sólo predice una entrada
	 * @param X Entrada de n = 728 y m = 1
	 * @return Cierto si el clasificador fuerte opina que es de la clase
	 */
	public boolean isClass(DataInput X) {
		return (hc.predict(X)[0] > 0);
	}
}
