package twoD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

public class OutputVisualizer extends Visualizer {

	private void readData(String filename) throws IOException {
		data = new int[Constants.height * Constants.width];
		File file = new File(Constants.classifiedDataFile);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		int i = 0;
		while ((s = br.readLine()) != null) {
			for (int j = 0; j < s.length(); j++) {
				switch (s.charAt(j)) {
				case Constants.predictIsClass:
					data[i * Constants.width + j] = (51 << 16) | (255 << 8) | 51;
					break;
				case Constants.outOfDomain:
					data[i * Constants.width + j] = (255 << 16) | (255 << 8) | 255;
					break;
				case Constants.isClass:
					data[i * Constants.width + j] = (0 << 16) | (0 << 8) | 204;
					break;
				case Constants.notClass:
					data[i * Constants.width + j] = (255 << 16) | (153 << 8) | 153;
					break;
				}
			}
			i++;
		}
	}

	public void view(String filename) throws IOException {
		readData(filename);
		printImage();
	}
	
	public OutputVisualizer() {
		super();
		this.frame = new JFrame("Clasificación");
		frame.setLocation(200 + Constants.width * scalingFactor + 100, 200);
	}
}
