package de.lennartmeinhardt.moiree;

import java.util.ResourceBundle;

import javafx.scene.image.WritableImage;

/**
 * Object that combines image drawing with internal names (to be used to get localized strings from {@link ResourceBundle}).
 * This enum class contains the available moiree image modes.
 * 
 * @author Lennart Meinhardt
 */
public enum MoireeImageMode {

	RANDOM("modeRandom", Drawing::drawRandomPixelsToImage),
	SQUARES("modeSquares", Drawing::drawCheckerboardToImage),
	TRIANGLES("modeTriangles", Drawing::drawTrianglesToImage),
	HORIZONTAL_LINES("modeHorizontalLines", Drawing::drawHorizontalLinesToImage);
	
	// the internal name
	private final String nameKey;
	// the object that can draw images
	private final ImageDrawer imageDrawer;
	
	
	/**
	 * Create a new {@link MoireeImageMode} with given internal name and image drawer.
	 * @param nameKey
	 * @param imageDrawer
	 */
	private MoireeImageMode(String nameKey, ImageDrawer imageDrawer) {
		this.nameKey = nameKey;
		this.imageDrawer = imageDrawer;
	}
	
	
	/**
	 * Get the localized name of this object from a {@link ResourceBundle}.
	 * 
	 * @param resources the resource bundle
	 * @return localized name
	 */
	public String getName(ResourceBundle resources) {
		return resources.getString(nameKey);
	}
	
	/**
	 * Get the object that draws images.
	 * 
	 * @return the image drawer
	 */
	public ImageDrawer getImageDrawer() {
		return imageDrawer;
	}
	
	
	/**
	 * Get the enum object of given index.
	 * 
	 * @param index the index to look up
	 * @return object of given index
	 * @throws ArrayIndexOutOfBoundsException if the index exceeds bounds
	 */
	public static MoireeImageMode getInstance(int index) throws ArrayIndexOutOfBoundsException {
		return values()[index];
	}
	
	/**
	 * Get the enum object of given index, or the default object if the index is invalid.
	 * 
	 * @param index the index to look up
	 * @return object of given index, or default object
	 */
	public static MoireeImageMode getInstanceOrDefault(int index) {
		try {
			return getInstance(index);
		} catch(ArrayIndexOutOfBoundsException ex) {
			return RANDOM;
		}
	}
	
	
	/**
	 * An object that can draw a Moiré image.
	 * 
	 * @author Lennart Meinhardt
	 */
	@FunctionalInterface
	public static interface ImageDrawer {
		void drawToImage(WritableImage destination, ImageSettings settings);
	}
}
