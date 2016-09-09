package de.lennartmeinhardt.moiree;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Simple object that stores all information necessary for creating Moiré images.
 * 
 * @author Lennart Meinhardt
 */
public class ImageSettings {
	
	private final Property<MoireeImageMode> imageModeProperty = new SimpleObjectProperty<>();

	private final IntegerProperty imageWidthProperty = new SimpleIntegerProperty();
	private final IntegerProperty imageHeightProperty = new SimpleIntegerProperty();

	private final IntegerProperty pixelSizeProperty = new SimpleIntegerProperty();
	private final DoubleProperty pixelDensityProperty = new SimpleDoubleProperty();

	
	public Property<MoireeImageMode> imageModeProperty() {
		return imageModeProperty;
	}
	public MoireeImageMode getImageMode() {
		return imageModeProperty.getValue();
	}
	public void setImageMode(MoireeImageMode value) {
		imageModeProperty.setValue(value);
	}

	
	public IntegerProperty imageWidthProperty() {
		return imageWidthProperty;
	}
	public int getImageWidth() {
		return imageWidthProperty.get();
	}
	public void setImageWidth(int value) {
		imageWidthProperty.set(value);
	}
	
	public IntegerProperty imageHeightProperty() {
		return imageHeightProperty;
	}
	public int getImageHeight() {
		return imageHeightProperty.get();
	}
	public void setImageHeight(int value) {
		imageHeightProperty.set(value);
	}
	
	public IntegerProperty pixelSizeProperty() {
		return pixelSizeProperty;
	}
	public int getPixelSize() {
		return pixelSizeProperty.get();
	}
	public void setPixelSize(int value) {
		pixelSizeProperty.set(value);
	}
	
	public DoubleProperty pixelDensityProperty() {
		return pixelDensityProperty;
	}
	public double getPixelDensity() {
		return pixelDensityProperty.get();
	}
	public void setPixelDensity(double value) {
		pixelDensityProperty.set(value);
	}
}
