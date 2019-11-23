package adaBoost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pkg1920_p2si.Imagen;
import pkg1920_p2si.MNISTLoader;
import twoD.Constants;



public class DataInput {
	
	public static int Byte2Unsigned(byte b) {
        return b & 0xFF;
    }
	
	// número de muestras
	private int m;
	// dimensión de cada muestra
	private int n;
	// datos almacenados (cada fila es un vector de datos de dimensión n)
	private int[][] data;
	// clasificación de los datos para aprender (1 = sí pertenece a la clase)
	private boolean[] classification;
	
	public static MNISTLoader ml = new MNISTLoader();

	public DataInput(String filename) throws IOException {
		m = Constants.sample_size;
		n = 2;
		data = new int[m][n];
		classification = new boolean[m];
		File file = new File(Constants.generatedDataOutputFile);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		int i = 0;
		int k = 0;
		while ((s = br.readLine()) != null && k < m) {
			for (int j = 0; j < s.length() && k < m; j++) {
				data[k][0] = j;
				data[k][1] = Constants.height - i - 1;
				switch (s.charAt(j)) {
				case Constants.isClass:
					classification[k] = true;
					k++;
					break;
				case Constants.notClass:
					classification[k] = false;
					k++;
					break;
				}
			}
			i++;
		}
	}

	// genera todas las posibles entradas de un espacio
	// bidimensional con el tamaño del canvas 2D
	public DataInput() {
		m = Constants.width * Constants.height;
		n = 2;
		data = new int[m][n];
		int k = 0;
		for (int i = 0; i < Constants.height; i++) {
			for (int j = 0; j < Constants.width; j++) {
				data[k][0] = j;
				data[k][1] = i;
				k++;
			}
		}
	}
	
	void allocateMem(int percentage) {
		for (int i = 0; i <= 9; i++) {
			double coefficient = percentage / (double) 100;
			double image_n = ml.getImageDatabaseForDigit(i).size();
			m += coefficient * image_n;
		}
		data = new int[m][n];
		classification = new boolean[m];
		
	}
	
	/**
	 * Lee de la base de datos el dígito y genera una entrada con sus valores
	 * ya clasificados
	 * @param digit del al 9
	 * @param percentage entre 0 y 100
	 */
	public DataInput(int digit, int percentage) {
		n = 784;
		ml.loadDBFromPath("./mnist_1000");
		allocateMem(percentage);
        int l = 0; //the index of the picture in the data[] array
        for (int i = 0; i <= 9; i++) {
        	ArrayList<Imagen> digitImages = ml.getImageDatabaseForDigit(i);
        	int exampleCap = (int) (digitImages.size() * (percentage / (double) 100));
        	for (int j = 0; j < exampleCap; j++) {
        		byte imageDataBytes[] = digitImages.get(j).getImageData();
        		for (int k = 0; k < imageDataBytes.length; k++)
        			data[l][k] = Byte2Unsigned(imageDataBytes[k]);
        		classification[l] = (i == digit);
        		l++;
        	}
        }
	}

	public int getM() {
		return m;
	}

	public int getN() {
		return n;
	}

	public int[][] getData() {
		return data;
	}

	public boolean[] getClassification() {
		return classification;
	}

}
