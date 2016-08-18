package de.lennartmeinhardt.moiree;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;

/**
 * A control for setting up a double value via a {@link Slider}.
 * 
 * @author Lennart Meinhardt
 */
public class DoubleValueSetup extends TitledPane implements Initializable {
	
	@FXML private Button resetButton;
	@FXML private Label valueLabel;
	@FXML private Slider valueSlider;
	@FXML private Label titleLabel;

	private final DoubleProperty valueProperty = new SimpleDoubleProperty();
	private final DoubleProperty defaultValueProperty = new SimpleDoubleProperty();
	private final DoubleProperty minimumValueProperty = new SimpleDoubleProperty();
	private final DoubleProperty maximumValueProperty = new SimpleDoubleProperty();

	
	public DoubleValueSetup() {
		try {
			FXMLLoader loader = new FXMLLoader(MoireeController.class.getResource("DoubleValueSetup.fxml"));
			loader.setResources(ResourceBundle.getBundle("de.lennartmeinhardt.moiree.bundles.ValueSetup"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override public void initialize(URL location, ResourceBundle resources) {
		valueLabel.textProperty().bind(Bindings.format("%.3f", valueProperty));
		valueSlider.valueProperty().bindBidirectional(valueProperty);
		valueSlider.minProperty().bindBidirectional(minimumValueProperty);
		valueSlider.maxProperty().bindBidirectional(maximumValueProperty);
		resetButton.disableProperty().bind(valueProperty.isEqualTo(defaultValueProperty, .001));
	}
	
	public void resetToDefault() {
		valueProperty.set(getDefaultValue());
	}
	
	@FXML private void resetButtonClicked(ActionEvent ev) {
		resetToDefault();
	}
	
	public DoubleProperty valueProperty() { return valueProperty; }
	public void setValue(double value) { valueProperty.set(value); }
	public double getValue() { return valueProperty.get(); }
	
	public DoubleProperty defaultValueProperty() { return defaultValueProperty; }
	public void setDefaultValue(double value) { defaultValueProperty.set(value); }
	public double getDefaultValue() { return defaultValueProperty.get(); }
	
	public DoubleProperty minValueProperty() { return minimumValueProperty; }
	public void setMinValue(double value) { minimumValueProperty.set(value); }
	public double getMinValue() { return minimumValueProperty.get(); }
	
	public DoubleProperty maxValueProperty() { return maximumValueProperty; }
	public void setMaxValue(double value) { maximumValueProperty.set(value); }
	public double getMaxValue() { return maximumValueProperty.get(); }
	
	public void setBlockIncrement(double value) { valueSlider.setBlockIncrement(value); }
	public DoubleProperty blockIncrementProperty() { return valueSlider.blockIncrementProperty(); }
	public double getBlockIncrement() { return valueSlider.getBlockIncrement(); }
}
