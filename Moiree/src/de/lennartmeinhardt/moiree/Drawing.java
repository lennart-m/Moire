package de.lennartmeinhardt.moiree;

import java.util.Random;

import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Drawing {
	
	private static final int ARGB_BLACK = 0xff << 24;
	private static final int ARGB_TRANSPARENT = 0;

	
	/**
	 * Draw the checkerboard moiré image with given pixel size.
	 * 
	 * @param image the image to write to
	 * @param size the pixel size
	 */
	public static void drawCheckerboardToImage(WritableImage image, int size) {
		PixelWriter writer = image.getPixelWriter();
		double width = image.getWidth();
		double height = image.getHeight();
		
		int argb = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if((x / size + y / size) % 2 == 0)
					argb = ARGB_BLACK;
				else
					argb = ARGB_TRANSPARENT;
				
				writer.setArgb(x, y, argb);
			}
		}
	}
	/**
	 * Draw the checkerboard moiré image with given image settings.
	 * 
	 * @param image the image to write to
	 * @param settings the image settings
	 */
	public static void drawCheckerboardToImage(WritableImage image, ImageSettings settings) {
		drawCheckerboardToImage(image, settings.getPixelSize());
	}
	
	/**
	 * Draw the horizontal lines moiré image with given pixel size.
	 * 
	 * @param image the image to write to
	 * @param size the pixel size
	 */
	public static void drawHorizontalLinesToImage(WritableImage image, int size) {
		PixelWriter writer = image.getPixelWriter();
		double width = image.getWidth();
		double height = image.getHeight();
		
		int argb = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if((y / size) % 2 == 0)
					argb = ARGB_BLACK;
				else
					argb = ARGB_TRANSPARENT;
				
				writer.setArgb(x, y, argb);
			}
		}
	}
	/**
	 * Draw the horizontal lines moiré image with given image settings.
	 * 
	 * @param image the image to write to
	 * @param settings the image settings
	 */
	public static void drawHorizontalLinesToImage(WritableImage image, ImageSettings settings) {
		drawHorizontalLinesToImage(image, settings.getPixelSize());
	}

	/**
	 * Draw the triangles moiré image with given pixel size.
	 * 
	 * @param image the image to write to
	 * @param size the pixel size
	 */
	public static void drawTrianglesToImage(WritableImage image, double size) {
		double width = image.getWidth();
		double height = image.getHeight();
		
		size *= 2;
		
		Polygon topLeftTriangle = createTriangle(0, 0, size);
		Polygon topRightTriangle = createTriangle(1, 0, size);
		Polygon bottomLeftTriangle = createTriangle(-.5, 1, size);
		Polygon bottomCenterTriangle = createTriangle(.5, 1, size);
		Polygon bottomRightTriangle = createTriangle(1.5, 1, size);

		Group patternCell = new Group(topLeftTriangle, topRightTriangle, bottomLeftTriangle, bottomCenterTriangle, bottomRightTriangle);
		Rectangle bounds2 = new Rectangle(2 * size * Math.sqrt(1.25), 2 * size);
		patternCell.setClip(bounds2);
		
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		Image pattern = patternCell.snapshot(params, null);

		ImagePattern imagePattern = new ImagePattern(pattern, 0, 0, size * 2 * Math.sqrt(1.25), size * 2, false);

		Rectangle bounds = new Rectangle(width, height);
		bounds.setFill(imagePattern);
		bounds.snapshot(params, image);
	}
	/**
	 * Draw the triangles moiré image with given image settings.
	 * 
	 * @param image the image to write to
	 * @param settings the image settings
	 */
	public static void drawTrianglesToImage(WritableImage image, ImageSettings settings) {
		drawTrianglesToImage(image, settings.getPixelSize());
	}
	
	/**
	 * Create a isosceles triangle with given height and translations. Translations are given relatively.
	 * Translation x is relative to triangle width, y is relative to triangle height.
	 * 
	 * @param translateXrel the triangle's x translation, to be scaled by width
	 * @param translateYrel the triangle's y translation, to be scaled by height
	 * @param height the triangle height
	 * @return triangle as {@link Polygon} object
	 */
	private static Polygon createTriangle(double translateXrel, double translateYrel, double height) {
		double triangleSideLength = height * Math.sqrt(1.25);
		Polygon triangle = new Polygon(triangleSideLength / 2, 0, 0, height, triangleSideLength, height);
		triangle.setTranslateX(translateXrel * triangleSideLength);
		triangle.setTranslateY(translateYrel * height);
		triangle.setFill(Color.BLACK);
		return triangle;
	}
	
	/**
	 * Draw the random moiré image with given density and pixel size.
	 * 
	 * @param image the image to write to
	 * @param density the pixel density
	 * @param size the pixel size
	 */
	public static void drawRandomPixelsToImage(WritableImage image, double density, int size) {
		PixelWriter writer = image.getPixelWriter();
		PixelReader reader = image.getPixelReader();
		double width = image.getWidth();
		double height = image.getHeight();
		
		Random random = new Random();
		int argb = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int offsetX = x % size;
				int offsetY = y % size;
				if(offsetX == 0 && offsetY == 0) {
					if(random.nextDouble() < density)
						argb = ARGB_BLACK;
					else
						argb = ARGB_TRANSPARENT;
				} else {
					int tempX = x - offsetX;
					int tempY = y - offsetY;
					argb = reader.getArgb(tempX, tempY);
				}
				
				writer.setArgb(x, y, argb);
			}
		}
	}
	/**
	 * Draw the random moiré image with given image settings.
	 * 
	 * @param image the image to write to
	 * @param settings the image settings
	 */
	public static void drawRandomPixelsToImage(WritableImage image, ImageSettings settings) {
		drawRandomPixelsToImage(image, settings.getPixelDensity() / 100, settings.getPixelSize());
	}
	
	/**
	 * Draw the diagonal lines moiré image with given pixel size.
	 * 
	 * @param image the image to write to
	 * @param size the pixel size
	 */
	public static void drawDiagonalLinesToImage(WritableImage image, int size) {
		PixelWriter writer = image.getPixelWriter();
		double width = image.getWidth();
		double height = image.getHeight();
		
		int argb = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(((x + y) / size) % 2 == 0)
					argb = ARGB_BLACK;
				else
					argb = ARGB_TRANSPARENT;
				
				writer.setArgb(x, y, argb);
			}
		}
	}
	/**
	 * Draw the diagonal lines moiré image with given image settings.
	 * 
	 * @param image the image to write to
	 * @param settings the image settings
	 */
	public static void drawDiagonalLinesToImage(WritableImage image, ImageSettings settings) {
		drawDiagonalLinesToImage(image, settings.getPixelSize());
	}
	

	// no instances
	private Drawing() {
	}
}
