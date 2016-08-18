package de.lennartmeinhardt.moiree;

import java.util.prefs.Preferences;

/**
 * Helper for loading and saving window dimensions.
 * 
 * @author Lennart Meinhardt
 */
public class WindowPreferences {
	
	private static final String KEY_WINDOW_WIDTH = "windowWidth";
	private static final String KEY_WINDOW_HEIGHT = "windowHeight";
	private static final String PREF_WINDOW_MAXIMIZED = "windowMaximized";

	private final Preferences preferences;
	
	
	public WindowPreferences() {
		this(Preferences.userNodeForPackage(WindowPreferences.class));
	}
	
	public WindowPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
	
	public double getWindowWidth(double def) {
		return preferences.getDouble(KEY_WINDOW_WIDTH, def);
	}
	public void setWindowWidth(double value) {
		preferences.putDouble(KEY_WINDOW_WIDTH, value);
	}
	
	public double getWindowHeight(double def) {
		return preferences.getDouble(KEY_WINDOW_HEIGHT, def);
	}
	public void setWindowHeight(double value) {
		preferences.putDouble(KEY_WINDOW_HEIGHT, value);
	}
	
	public boolean getWindowMaximized(boolean def) {
		return preferences.getBoolean(PREF_WINDOW_MAXIMIZED, def);
	}
	public void setWindowMaximized(boolean value) {
		preferences.putBoolean(PREF_WINDOW_MAXIMIZED, value);
	}
}
