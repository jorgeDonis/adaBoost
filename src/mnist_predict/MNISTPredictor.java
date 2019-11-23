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
	
	/**
	 * 
	 * @param digit from 0 to 9
	 * @param imgNumber image number (position in folder)
	 */
	public void testImage(int digit, int imgNumber) {
		
	}	
}
