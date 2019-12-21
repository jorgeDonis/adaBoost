package adaBoost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	// clasificación de los datos para aprender (true = sí pertenece a la clase)
	private boolean[] classification;
	
	public static MNISTLoader ml = new MNISTLoader();

	/**
	 * Constructor para el clasificador de 2 dimensiones
	 * @param filename
	 * @throws IOException
	 */
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
		br.close();
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
	
	/**
	 * Reserva memoria para el clasificador de números
	 * @param lowPercentage
	 * @param highPercentage
	 * @param allDigits
	 * @param digit
	 */
	private void allocateMem(int lowPercentage, int highPercentage, boolean allDigits, int digit) {
		int equalizeSamples = 0;
		for (int i = 0; i <= 9;) {
			if (!allDigits)
				i = digit;
			double image_n = ml.getImageDatabaseForDigit(i).size();
			double lowIndex = lowPercentage * image_n / (double) 100; 
			double highIndex = highPercentage * image_n / (double) 100; 
			m += (highIndex - lowIndex);
			if (!allDigits)
				break;
			if (i == digit && equalizeSamples < 10)
				equalizeSamples++;
			else
				i++;
		}
		data = new int[m][n];
		classification = new boolean[m];
		
	}
	
	/**
	 * Lee de la base de datos el dígito y genera una entrada con sus valores
	 * ya clasificados. Toma datos de todos los números por defecto.
	 * @param digit del al 9
	 * @param lowPercentage entre 0 y 100
	 * @param highPercentage entre 0 y 100. Low estrictamente menor que high
	 * @param allDigits Si queremos coger datos de todos los dígitos o sólo del indicado
	 * En teoría es mejor entrenar con ejemplos de todos los dígitos. Si se desea, se normalizaran
	 * las probabilidades, repitiendo los ejemplos del número que sí que es de la clase 10 veces (
	 * ecualización).
	 */
	public DataInput(int digit, int lowPercentage, int highPercentage, boolean allDigits) {
		n = 784;
		allocateMem(lowPercentage, highPercentage, allDigits, digit);
        int l = 0; //the index of the picture in the data[] array
        int equalizeSamples = 0;
        for (int i = 0; i <= 9;) {
        	if (!allDigits)
        		i = digit;
        	@SuppressWarnings("unchecked")
			ArrayList<Imagen> digitImages = ml.getImageDatabaseForDigit(i);
        	double image_n = digitImages.size();
        	double lowIndex = lowPercentage * image_n / (double) 100; 
			double highIndex = highPercentage * image_n / (double) 100; 
        	for (int j = (int) lowIndex; j < (int) highIndex - 1; j++) {
        		byte imageDataBytes[] = digitImages.get(j).getImageData();
        		for (int k = 0; k < imageDataBytes.length; k++)
        			data[l][k] = Byte2Unsigned(imageDataBytes[k]);
        		classification[l] = (i == digit);
        		l++;
        	}
        	if (!allDigits)
        		break;
        	if (i == digit && equalizeSamples < 10)
				equalizeSamples++;
			else
				i++;
        }
	}
	
	/**
	 * Crea una entrada del dígito (0-9) digit con un ejemplo de índice index (0 - 100 approx)
	 * @param digit
	 * @param index
	 */
	@SuppressWarnings("unchecked")
	public DataInput(int digit, int index) {
		n = 784;
		m = 1;
		data = new int[m][n];
		ArrayList<Imagen> digitImages = ml.getImageDatabaseForDigit(digit);
		byte imageDataBytes[] = digitImages.get(index).getImageData();
		for (int k = 0; k < imageDataBytes.length; k++)
			data[0][k] = Byte2Unsigned(imageDataBytes[k]);
	}
	
	/**
	 * Crea una entrada con la información de píxeles proporcionada.
	 * Se establece la clasifiación a false, aunque no es necesario usarla
	 * @param data
	 */
	public DataInput(int[] data) {
		n = 784;
		m = 1;
		this.data = new int[m][n];
		this.data[0] = data;
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
