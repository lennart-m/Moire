package de.lennartmeinhardt.moiree;

import java.util.prefs.Preferences;

public class MoireePreferences {
	private static final double DEF_PIXEL_DENSITY = 0.2;
	private static final int DEF_PIXEL_SIZE = 1;
	private static final double DEF_ROTATION = 0.0;
	private static final double DEF_TRANSLATION_X = 0.0;
	private static final double DEF_TRANSLATION_Y = 0.0;
	private static final double DEF_COMMON_SCALING = 1.0;
	private static final boolean DEF_USE_COMMON_SCALING = true;
	private static final double DEF_SCALING_X = 1.0;
	private static final double DEF_SCALING_Y = 1.0;
	private static final int DEF_IMAGE_MODE_INDEX = 0;

	private static final String KEY_PIXEL_DENSITY = "pixelDensity";
	private static final String KEY_PIXEL_SIZE = "pixelSize";
	private static final String KEY_ROTATION = "transformRotation";
	private static final String KEY_TRANSLATION_X = "transformTranslationX";
	private static final String KEY_TRANSLATION_Y = "transformTranslationY";
	private static final String KEY_COMMON_SCALING = "transformCommonScaling";
	private static final String KEY_USE_COMMON_SCALING = "transformUseCommonScaling";
	private static final String KEY_SCALING_X = "transformScalingX";
	private static final String KEY_SCALING_Y = "transformScalingY";
	private static final String KEY_IMAGE_MODE_INDEX = "imageModeIndex";
	
	private final Preferences preferences;

	public MoireePreferences() {
		this(Preferences.userNodeForPackage(MoireePreferences.class));
	}

	public MoireePreferences(Preferences preferences) {
		this.preferences = preferences;
	}

	public double getTranslationX() {
		return this.preferences.getDouble(KEY_TRANSLATION_X, DEF_TRANSLATION_X);
	}
	public void setTranslationX(double value) {
		this.preferences.putDouble(KEY_TRANSLATION_X, value);
	}
	public void initTranslationXSetup(DoubleValueSetup setup) {
		setup.setValue(this.getTranslationX());
		setup.setDefaultValue(DEF_TRANSLATION_X);
	}

	public double getTranslationY() {
		return this.preferences.getDouble(KEY_TRANSLATION_Y, DEF_TRANSLATION_Y);
	}
	public void setTranslationY(double value) {
		this.preferences.putDouble(KEY_TRANSLATION_Y, value);
	}
	public void initTranslationYSetup(DoubleValueSetup setup) {
		setup.setValue(this.getTranslationY());
		setup.setDefaultValue(DEF_TRANSLATION_Y);
	}

	public double getScalingX() {
		return this.preferences.getDouble(KEY_SCALING_X, DEF_SCALING_X);
	}
	public void setScalingX(double value) {
		this.preferences.putDouble(KEY_SCALING_X, value);
	}
	public void initScalingXSetup(DoubleValueSetup setup) {
		setup.setValue(this.getScalingX());
		setup.setDefaultValue(DEF_SCALING_X);
	}

	public double getScalingY() {
		return this.preferences.getDouble(KEY_SCALING_Y, DEF_SCALING_Y);
	}
	public void setScalingY(double value) {
		this.preferences.putDouble(KEY_SCALING_Y, value);
	}
	public void initScalingYSetup(DoubleValueSetup setup) {
		setup.setValue(this.getScalingY());
		setup.setDefaultValue(DEF_SCALING_Y);
	}

	public double getRotation() {
		return this.preferences.getDouble(KEY_ROTATION, DEF_ROTATION);
	}
	public void setRotation(double value) {
		this.preferences.putDouble(KEY_ROTATION, value);
	}
	public void initRotationSetup(DoubleValueSetup setup) {
		setup.setValue(this.getRotation());
		setup.setDefaultValue(DEF_ROTATION);
	}

	public double getCommonScaling() {
		return this.preferences.getDouble(KEY_COMMON_SCALING, DEF_COMMON_SCALING);
	}
	public void setCommonScaling(double value) {
		this.preferences.putDouble(KEY_COMMON_SCALING, value);
	}
	public void initCommonScalingSetup(DoubleValueSetup setup) {
		setup.setValue(this.getCommonScaling());
		setup.setDefaultValue(DEF_COMMON_SCALING);
	}

	public boolean isUseCommonScaling() {
		return this.preferences.getBoolean(KEY_USE_COMMON_SCALING, DEF_USE_COMMON_SCALING);
	}
	public void setUseCommonScaling(boolean value) {
		this.preferences.putBoolean(KEY_USE_COMMON_SCALING, value);
	}

	public double getPixelDensity() {
		return this.preferences.getDouble(KEY_PIXEL_DENSITY, DEF_PIXEL_DENSITY);
	}
	public void setPixelDensity(double value) {
		this.preferences.putDouble(KEY_PIXEL_DENSITY, value);
	}
	public void initPixelDensitySetup(DoubleValueSetup setup) {
		setup.setValue(this.getPixelDensity());
		setup.setDefaultValue(DEF_PIXEL_DENSITY);
	}

	public int getPixelSize() {
		return this.preferences.getInt(KEY_PIXEL_SIZE, DEF_PIXEL_SIZE);
	}
	public void setPixelSize(int value) {
		this.preferences.putInt(KEY_PIXEL_SIZE, value);
	}
	public void initPixelSizeSetup(IntValueSetup setup) {
		setup.setValue(this.getPixelSize());
		setup.setDefaultValue(DEF_PIXEL_SIZE);
	}
	
	public int getImageModeIndex() {
		return this.preferences.getInt(KEY_IMAGE_MODE_INDEX, DEF_IMAGE_MODE_INDEX);
	}
	public void setImageModeIndex(int value) {
		this.preferences.putInt(KEY_IMAGE_MODE_INDEX, value);
	}
}
