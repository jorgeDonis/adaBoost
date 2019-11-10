package adaBoost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import twoD.Constants;

public class DataInput {
	// número de muestras
	private int m;
	// dimensión de cada muestra
	private int n;
	// datos almacenados (cada fila es un datos de dimensión n)
	private int[][] data;
	// clasificación de los datos para aprender (1 = sí pertenece a la clase)
	private boolean[] classification;

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
