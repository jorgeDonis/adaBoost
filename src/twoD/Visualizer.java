package twoD;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Visualizer {
	
	static final int scalingFactor = 10;
	
	public JFrame frame;

	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public  BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
	
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
	
		// Return the buffered image
		return bimage;
	}

	protected  class ColorPan extends JComponent {
			public void paint(Graphics g) {
				BufferedImage image = new BufferedImage(Constants.width, Constants.height, BufferedImage.TYPE_INT_RGB);
				image.setRGB(0, 0, Constants.width, Constants.height, data, 0, Constants.width);
				image = toBufferedImage(
						image.getScaledInstance(Constants.width * scalingFactor,
								Constants.height * scalingFactor, Image.SCALE_SMOOTH));
				g.drawImage(image, 0, 0, this);
			}
		}

	protected  int[] data;

	protected  void printImage() {
		frame.getContentPane().add(new ColorPan());
		frame.setSize(Constants.width * scalingFactor + 100,
				Constants.height * scalingFactor + 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public Visualizer() {
		super();
	}

}