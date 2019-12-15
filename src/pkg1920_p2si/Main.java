package pkg1920_p2si;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import adaBoost.DataInput;
import adaBoost.HardClassifier;
import adaBoost.LightClassifier;
import adaBoost.PixelBrightnessThreshholdClassifier;
import mnist_predict.MNISTPredictor;
import twoD.Constants;
import twoD.Generator2D;
import twoD.InputVisualizer;
import twoD.OutputVisualizer;

/**
 * @author Jorge Donis del Álamo
 */
public class Main {

	/**
	 * Muestra la imágen de la base de datos (como imagen) e indica por salida
	 * estándar la clasificación del clasificador fuerte
	 * 
	 * @param digit          (0-9)
	 * @param index          (0-100) approx
	 * @param bestClassifier mejor clasificador
	 */
	private static void testNumber(int digit, int index, HardClassifier bestClassifier) {
		System.out.println("Muestro un " + digit);
		MostrarImagen mostrar = new MostrarImagen();
		mostrar.setImage((Imagen) DataInput.ml.getImageDatabaseForDigit(digit).get(index));
		mostrar.mostrar();
		if (new MNISTPredictor(bestClassifier).isClass(new DataInput(digit, index)))
			System.out.println("El clasificador piensa que es un " + Traindigit);
		else
			System.out.println("El clasificador piensa que no es un " + Traindigit);
	}

	static int Traindigit = 0;
	static int percentage = 80;

	/**
	 * @param args the command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * documentación para un futuro //Cargador MNIST de SI MNISTLoader ml = new
		 * MNISTLoader(); ml.loadDBFromPath("./mnist_1000");
		 * 
		 * //Accedo a las imagenes de dígito 1 ArrayList d0imgs =
		 * ml.getImageDatabaseForDigit(1);
		 * 
		 * //Y cojo la tercera imagen del dígito 1 Imagen img = (Imagen) d0imgs.get(10);
		 * 
		 * //La invierto para ilustrar como acceder a los pixels byte imageData[] =
		 * img.getImageData(); for (int i = 0; i < imageData.length; i++){
		 * 
		 * imageData[i] = (byte) (255 - imageData[i]);
		 * System.out.print(Byte2Unsigned(imageData[i])+ ","); }
		 * 
		 * //Muestro la imagen invertida MostrarImagen imgShow = new MostrarImagen();
		 * imgShow.setImage(img); imgShow.mostrar();
		 */

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("T = ");
		HardClassifier.T = Integer.parseInt(reader.readLine());
		System.out.print("A = ");
		HardClassifier.A = Integer.parseInt(reader.readLine());
		System.out.print("Dígito con el que entrenar (0 - 9): ");
		Traindigit = Integer.parseInt(reader.readLine());
		System.out.print("Porcentaje de entrenamiento (de 0 al porcentaje \n se entrena y "
				+ "con el resto de ejemplos se comprueba): ");
		percentage = Integer.parseInt(reader.readLine());
		System.out.println("\n");

		DataInput.ml.loadDBFromPath("./mnist_1000");
		DataInput X = new DataInput(Traindigit, 0, percentage);
		LightClassifier lc = new PixelBrightnessThreshholdClassifier(646, 10, true);
		
		double bestPrecision = Double.NEGATIVE_INFINITY;
		HardClassifier bestClassifier = null;
		for (int i = 0; i < 100; i++) {
			HardClassifier classifier = new HardClassifier(X);
			MNISTPredictor predictor = new MNISTPredictor(classifier);
			float thisPrecision = predictor.classify(new DataInput(Traindigit, percentage, 100));
			if (thisPrecision > bestPrecision) {
				bestPrecision = thisPrecision;
				bestClassifier = classifier;
			}
		}
		System.out.println("Mejor precisión : " + bestPrecision);
		while (true) {
			System.out.print("Introduce dígito con el que probar el clasificador (0-9): ");
			int digit = Integer.parseInt(reader.readLine());
			System.out.print("Introduce el ejemplo (entre 0 y 100 approx) con el que probar: ");
			int index = Integer.parseInt(reader.readLine());
			testNumber(digit, index, bestClassifier);
		}
	}

}
