package de.lennartmeinhardt.moiree;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;

/**
 * Core class for managing the transformation setup.
 * 
 * @author Lennart Meinhardt
 */
public class TransformationSetupController implements Initializable {
	
	@FXML private Node root;
	
	@FXML private TitledPane transformationSetupPane;
	@FXML private DoubleValueSetup rotateSetup;
	@FXML private DoubleValueSetup translateXSetup;
	@FXML private DoubleValueSetup translateYSetup;
	@FXML private DoubleValueSetup commonScaleSetup;
	@FXML private CheckBox useCommonScaleCheckBox;
	@FXML private DoubleValueSetup scaleXSetup;
	@FXML private DoubleValueSetup scaleYSetup;
	@FXML private CheckBox enableAnimationsCheckbox;

	private NumberBinding autoScaleXBinding;
	private NumberBinding autoScaleYBinding;

	
	@Override public void initialize(URL location, ResourceBundle resources) {
		// bind transformation setup controls
		useCommonScaleCheckBox.selectedProperty().addListener(e -> {
			if(useCommonScaleCheckBox.isSelected()) {
				commonScaleSetup.setExpandedNonCollapsible(true);
				scaleXSetup.setExpandedNonCollapsible(false);
				scaleYSetup.setExpandedNonCollapsible(false);
			} else {
				commonScaleSetup.setExpandedNonCollapsible(false);
				scaleXSetup.setExpandedNonCollapsible(true);
				scaleYSetup.setExpandedNonCollapsible(true);
			}
		});
		
		scaleXSetup.disableProperty().bind(useCommonScaleProperty());
		scaleYSetup.disableProperty().bind(useCommonScaleProperty());
		commonScaleSetup.disableProperty().bind(useCommonScaleProperty().not());

		autoScaleXBinding = Bindings.when(useCommonScaleProperty()).then(commonScaleProperty()).otherwise(scaleXProperty());
		autoScaleYBinding = Bindings.when(useCommonScaleProperty()).then(commonScaleProperty()).otherwise(scaleYProperty());

		rotateSetup.animateChangesProperty().bind(enableAnimationsCheckbox.selectedProperty());
		translateXSetup.animateChangesProperty().bind(enableAnimationsCheckbox.selectedProperty());
		translateYSetup.animateChangesProperty().bind(enableAnimationsCheckbox.selectedProperty());
		commonScaleSetup.animateChangesProperty().bind(enableAnimationsCheckbox.selectedProperty());
		scaleXSetup.animateChangesProperty().bind(enableAnimationsCheckbox.selectedProperty());
		scaleYSetup.animateChangesProperty().bind(enableAnimationsCheckbox.selectedProperty());
	}
		
	
	/**
	 * Load the transform data from given {@link PreferencesHelper}.
	 * 
	 * @param preferences the preferences to load from
	 */
	public void loadFromPreferences(PreferencesHelper preferences) {
		rotateSetup.setValue(preferences.getRotation(rotateSetup.getDefaultValue()));
		translateXSetup.setValue(preferences.getTranslationX(translateXSetup.getDefaultValue()));
		translateYSetup.setValue(preferences.getTranslationY(translateYSetup.getDefaultValue()));
		scaleXSetup.setValue(preferences.getScalingX(scaleXSetup.getDefaultValue()));
		scaleYSetup.setValue(preferences.getScalingY(scaleYSetup.getDefaultValue()));
		commonScaleSetup.setValue(preferences.getCommonScaling(commonScaleSetup.getDefaultValue()));
		useCommonScaleCheckBox.setSelected(preferences.isUseCommonScaling(true));
		
		enableAnimationsCheckbox.setSelected(preferences.isEnableAnimations(enableAnimationsCheckbox.isSelected()));
	}
	
	/**
	 * Store the transformation data to the given {@link PreferencesHelper}.
	 * 
	 * @param preferences the preferences to store to
	 */
	public void storeToPreferences(PreferencesHelper preferences) {
		preferences.setRotation(rotateSetup.getValue());
		preferences.setTranslationX(translateXSetup.getValue());
		preferences.setTranslationY(translateYSetup.getValue());
		preferences.setScalingX(scaleXSetup.getValue());
		preferences.setScalingY(scaleYSetup.getValue());
		preferences.setCommonScaling(commonScaleSetup.getValue());
		preferences.setUseCommonScaling(useCommonScaleCheckBox.isSelected());
		
		preferences.setEnableAnimations(enableAnimationsCheckbox.isSelected());
	}
	
	@FXML private void onResetTransformationClicked(ActionEvent ev) {
		resetTransformationToDefault();
	}
	
	/**
	 * Resets the transformation to identity.
	 */
	public void resetTransformationToDefault() {
		rotateSetup.resetToDefault();
		translateXSetup.resetToDefault();
		translateYSetup.resetToDefault();
		scaleXSetup.resetToDefault();
		scaleYSetup.resetToDefault();
		commonScaleSetup.resetToDefault();
	}

	public DoubleProperty rotateProperty() { return rotateSetup.valueProperty(); }
	public double getRotate() { return rotateProperty().get(); }
	public void setRotate(double value) { rotateProperty().set(value); }
	
	public DoubleProperty translateXProperty() { return translateXSetup.valueProperty(); }
	public double getTranslateX() { return translateXProperty().get(); }
	public void setTranslateX(double value) { translateXProperty().set(value); }
	
	public DoubleProperty translateYProperty() { return translateYSetup.valueProperty(); }
	public double getTranslateY() { return translateYProperty().get(); }
	public void setTranslateY(double value) { translateYProperty().set(value); }

	public BooleanProperty useCommonScaleProperty() { return useCommonScaleCheckBox.selectedProperty(); }	
	public boolean isUseCommonScale() { return useCommonScaleProperty().get(); }
	public void setUseCommonScale(boolean value) { useCommonScaleProperty().set(value); }
	
	public DoubleProperty commonScaleProperty() { return commonScaleSetup.valueProperty(); }
	public double getCommonScale() { return commonScaleProperty().get(); }
	public void setCommonScale(double value) { commonScaleProperty().set(value); }
	
	public DoubleProperty scaleXProperty() { return scaleXSetup.valueProperty(); }
	public double getScaleX() { return scaleXProperty().get(); }
	public void setScaleX(double value) { scaleXProperty().set(value); }
	
	public DoubleProperty scaleYProperty() { return scaleYSetup.valueProperty(); }
	public double getScaleY() { return scaleYProperty().get(); }
	public void setScaleY(double value) { scaleYProperty().set(value); }
	
	public NumberBinding autoScaleXBinding() { return autoScaleXBinding; }
	public Number getAutoScaleX() { return autoScaleXBinding.getValue(); }
	
	public NumberBinding autoScaleYBinding() { return autoScaleYBinding; }
	public Number getAutoScaleY() { return autoScaleYBinding.getValue(); }
}
