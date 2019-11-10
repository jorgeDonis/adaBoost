package twoD;

import java.io.BufferedWriter;
import static twoD.Constants.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator2D {
	// -1 means not known, 0 is not of the class, 1 if it belongs to the class
	private static int[][] data;

	private static boolean isInsideBig(int x, int y) {
		if (x < Constants.height) {
			return y <= x;
		} else {
			return (y <= (Constants.width - x));
		}
	}

	private static boolean isInsideSmall(int x, int y) {
		if (x >= (0.25 * Constants.width) && x <= (0.75 * Constants.width) && y <= (0.5 * Constants.height)) {
			if (x < Constants.height) {
				return (y >= (Constants.height - x));
			} else {
				return (y >= (x - Constants.height));
			}
		}
		return false;
	}

	private static boolean f(int x, int y) {
		if (y > Constants.height)
			return false;
		return (isInsideBig(x, y) && !isInsideSmall(x, y));
	}

	static public void generateData() throws IOException {
		Random rng = new Random();
		data = new int[Constants.width][Constants.height];
		for (int i = 0; i < Constants.width; i++)
			data[i] = new int[Constants.height];
		for (int i = 0; i < Constants.width; i++)
			for (int j = 0; j < Constants.height; j++)
				data[i][j] = -1;
		for (int i = 0; i < sample_size; i++) {
			int x = rng.nextInt(Constants.width);
			int y = rng.nextInt(Constants.height);
			data[x][y] = f(x, y) ? 1 : 0;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.generatedDataOutputFile));
		for (int j = Constants.height - 1; j >= 0; j--) {
			for (int i = 0; i < Constants.width; i++) {
				if (data[i][j] == 1)
					writer.write(Constants.isClass);
				else if (data[i][j] == 0)
					writer.write(Constants.notClass);
				else
					writer.write(Constants.outOfDomain);
			}
			writer.write("\n");
		}
		writer.close();
	}
}
