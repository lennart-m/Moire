package de.lennartmeinhardt.moiree;

import java.util.prefs.Preferences;

import javafx.scene.paint.Color;

/**
 * Helper class that encapsulates reading from and saving to {@link Preferences}.
 * 
 * @author Lennart Meinhardt
 */
public class PreferencesHelper {
	
	// transformation setup
	private static final String KEY_ROTATION = "transformRotation";
	private static final String KEY_TRANSLATION_X = "transformTranslationX";
	private static final String KEY_TRANSLATION_Y = "transformTranslationY";
	private static final String KEY_COMMON_SCALING = "transformCommonScaling";
	private static final String KEY_USE_COMMON_SCALING = "transformUseCommonScaling";
	private static final String KEY_SCALING_X = "transformScalingX";
	private static final String KEY_SCALING_Y = "transformScalingY";
	// image setup
	private static final String KEY_PIXEL_DENSITY = "pixelDensityPercents";
	private static final String KEY_PIXEL_SIZE = "pixelSize";
	private static final String KEY_IMAGE_MODE_INDEX = "imageModeIndex";
	private static final String KEY_IMAGE_WIDTH = "imageWidth";
	private static final String KEY_IMAGE_HEIGHT = "imageHeight";
	// others
	private static final String KEY_ENABLE_ANIMATIONS = "enableAnimations";
	private static final String KEY_TRANS_SETUP_EXPANDED = "transformationSetupExpanded";
	private static final String KEY_IMAGE_PANE_EXPANDED = "imagePaneExpanded";
	// colors
	private static final String KEY_BACKGROUND_COLOR_PREFIX = "backgroundColor";
	private static final String KEY_FOREGROUND_COLOR_PREFIX = "foregroundColor";
	private static final String ALPHA_SUFFIX = "_alphaChannel";
	private static final String RED_SUFFIX = "_redChannel";
	private static final String GREEN_SUFFIX = "_greenChannel";
	private static final String BLUE_SUFFIX = "_blueChannel";
	
	// the preferences object to use
	private final Preferences preferences;

	
	/**
	 * Create a new {@link PreferencesHelper} with default preferences.
	 */
	public PreferencesHelper() {
		this(Preferences.userNodeForPackage(PreferencesHelper.class));
	}

	/**
	 * Create a new {@link PreferencesHelper} with given preferences.
	 */
	public PreferencesHelper(Preferences preferences) {
		this.preferences = preferences;
	}
	
	
	/********************************************
	 ************** Transformation **************
	 ********************************************/
	

	public double getTranslationX(double defaultValue) {
		return this.preferences.getDouble(KEY_TRANSLATION_X, defaultValue);
	}
	public void setTranslationX(double value) {
		this.preferences.putDouble(KEY_TRANSLATION_X, value);
	}

	public double getTranslationY(double defaultValue) {
		return this.preferences.getDouble(KEY_TRANSLATION_Y, defaultValue);
	}
	public void setTranslationY(double value) {
		this.preferences.putDouble(KEY_TRANSLATION_Y, value);
	}

	public double getScalingX(double defaultValue) {
		return this.preferences.getDouble(KEY_SCALING_X, defaultValue);
	}
	public void setScalingX(double value) {
		this.preferences.putDouble(KEY_SCALING_X, value);
	}

	public double getScalingY(double defaultValue) {
		return this.preferences.getDouble(KEY_SCALING_Y, defaultValue);
	}
	public void setScalingY(double value) {
		this.preferences.putDouble(KEY_SCALING_Y, value);
	}

	public double getRotation(double defaultValue) {
		return this.preferences.getDouble(KEY_ROTATION, defaultValue);
	}
	public void setRotation(double value) {
		this.preferences.putDouble(KEY_ROTATION, value);
	}

	public double getCommonScaling(double defaultValue) {
		return this.preferences.getDouble(KEY_COMMON_SCALING, defaultValue);
	}
	public void setCommonScaling(double value) {
		this.preferences.putDouble(KEY_COMMON_SCALING, value);
	}

	public boolean isUseCommonScaling(boolean defaultValue) {
		return this.preferences.getBoolean(KEY_USE_COMMON_SCALING, defaultValue);
	}
	public void setUseCommonScaling(boolean value) {
		this.preferences.putBoolean(KEY_USE_COMMON_SCALING, value);
	}
	
	/********************************************
	 ************** Image settings **************
	 ********************************************/
	
	public double getPixelDensity(double defaultValue) {
		return this.preferences.getDouble(KEY_PIXEL_DENSITY, defaultValue);
	}
	public void setPixelDensity(double value) {
		this.preferences.putDouble(KEY_PIXEL_DENSITY, value);
	}

	public int getPixelSize(int defaultValue) {
		return this.preferences.getInt(KEY_PIXEL_SIZE, defaultValue);
	}
	public void setPixelSize(int value) {
		this.preferences.putInt(KEY_PIXEL_SIZE, value);
	}
	
	public int getImageModeIndex(int defaultValue) {
		return this.preferences.getInt(KEY_IMAGE_MODE_INDEX, defaultValue);
	}
	public void setImageModeIndex(int value) {
		this.preferences.putInt(KEY_IMAGE_MODE_INDEX, value);
	}
	
	public void setImageWidth(int value) {
		this.preferences.putInt(KEY_IMAGE_WIDTH, value);
	}
	public int getImageWidth(int defaultValue) {
		return preferences.getInt(KEY_IMAGE_WIDTH, defaultValue);
	}
	
	public void setImageHeight(int value) {
		this.preferences.putInt(KEY_IMAGE_HEIGHT, value);
	}
	public int getImageHeight(int defaultValue) {
		return preferences.getInt(KEY_IMAGE_HEIGHT, defaultValue);
	}
	
	/********************************************
	 ************** Other settings **************
	 ********************************************/

	public boolean isEnableAnimations(boolean defaultValue) {
		return preferences.getBoolean(KEY_ENABLE_ANIMATIONS, defaultValue);
	}
	public void setEnableAnimations(boolean value) {
		preferences.putBoolean(KEY_ENABLE_ANIMATIONS, value);
	}

	public boolean isTransformationSetupExpanded(boolean defaultValue) {
		return preferences.getBoolean(KEY_TRANS_SETUP_EXPANDED, defaultValue);
	}
	public void setTransformationSetupExpanded(boolean value) {
		preferences.putBoolean(KEY_TRANS_SETUP_EXPANDED, value);
	}

	public boolean isImagePaneExpanded(boolean defaultValue) {
		return preferences.getBoolean(KEY_IMAGE_PANE_EXPANDED, defaultValue);
	}
	public void setImagePaneExpanded(boolean value) {
		preferences.putBoolean(KEY_IMAGE_PANE_EXPANDED, value);
	}
	
	/********************************************
	 ****************** Colors ******************
	 ********************************************/
	private Color getColor(String keyPrefix, Color defaultValue) {
		double opacity = preferences.getDouble(keyPrefix + ALPHA_SUFFIX, defaultValue.getOpacity());
		double red = preferences.getDouble(keyPrefix + RED_SUFFIX, defaultValue.getRed());
		double green = preferences.getDouble(keyPrefix + GREEN_SUFFIX, defaultValue.getGreen());
		double blue = preferences.getDouble(keyPrefix + BLUE_SUFFIX, defaultValue.getBlue());
		return new Color(red, green, blue, opacity);
	}
	private void setColor(String keyPrefix, Color color) {
		preferences.putDouble(keyPrefix + ALPHA_SUFFIX, color.getOpacity());
		preferences.putDouble(keyPrefix + RED_SUFFIX, color.getRed());
		preferences.putDouble(keyPrefix + GREEN_SUFFIX, color.getGreen());
		preferences.putDouble(keyPrefix + BLUE_SUFFIX, color.getBlue());
	}
	
	public Color getBackgroundColor(Color defaultValue) {
		return getColor(KEY_BACKGROUND_COLOR_PREFIX, defaultValue);
	}
	public void setBackgroundColor(Color value) {
		setColor(KEY_BACKGROUND_COLOR_PREFIX, value);
	}
	
	public Color getForegroundColor(Color defaultValue) {
		return getColor(KEY_FOREGROUND_COLOR_PREFIX, defaultValue);
	}
	public void setForegroundColor(Color value) {
		setColor(KEY_FOREGROUND_COLOR_PREFIX, value);
	}
}
