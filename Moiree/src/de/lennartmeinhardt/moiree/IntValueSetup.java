package de.lennartmeinhardt.moiree;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

/**
 * A control for setting up a 
 * 
 * @author Lennart Meinhardt
 */
public class IntValueSetup extends TitledPane implements Initializable {
	
	@FXML private Button resetButton;
	@FXML private Button plusButton;
	@FXML private Button minusButton;
	@FXML private Label valueLabel;
	@FXML private Label titleLabel;

	private final IntegerProperty valueProperty = new SimpleIntegerProperty();
	private final IntegerProperty defaultValueProperty = new SimpleIntegerProperty();
	private final IntegerProperty minimumValueProperty = new SimpleIntegerProperty();
	private final IntegerProperty maximumValueProperty = new SimpleIntegerProperty();

	
	public IntValueSetup() {
		try {
			FXMLLoader loader = new FXMLLoader(MoireeController.class.getResource("IntValueSetup.fxml"));
			loader.setResources(ResourceBundle.getBundle("de.lennartmeinhardt.moiree.bundles.ValueSetup"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override public void initialize(URL location, ResourceBundle resources) {
		valueLabel.textProperty().bind(valueProperty.asString());
		minusButton.disableProperty().bind(valueProperty.lessThanOrEqualTo(minimumValueProperty));
		plusButton.disableProperty().bind(valueProperty.greaterThanOrEqualTo(maximumValueProperty));
		resetButton.disableProperty().bind(valueProperty.isEqualTo(defaultValueProperty));
	}
	
	public void resetToDefault() {
		valueProperty.set(getDefaultValue());
	}
	
	@FXML private void resetButtonClicked(ActionEvent ev) {
		resetToDefault();
	}
	
	@FXML private void minusButtonClicked(ActionEvent ev) {
		if(valueProperty.get() > minValueProperty().get())
			valueProperty.set(valueProperty.get() - 1);
	}
	
	@FXML private void plusButtonClicked(ActionEvent ev) {
		if(valueProperty.get() < maxValueProperty().get())
			valueProperty.set(valueProperty.get() + 1);
	}
	
	public IntegerProperty valueProperty() { return valueProperty; }
	public void setValue(int value) { valueProperty.set(value); }
	public int getValue() { return valueProperty.get(); }
	
	public IntegerProperty defaultValueProperty() { return defaultValueProperty; }
	public void setDefaultValue(int value) { defaultValueProperty.set(value); }
	public int getDefaultValue() { return defaultValueProperty.get(); }
	
	public IntegerProperty minValueProperty() { return minimumValueProperty; }
	public void setMinValue(int value) { minimumValueProperty.set(value); }
	public int getMinValue() { return minimumValueProperty.get(); }
	
	public IntegerProperty maxValueProperty() { return maximumValueProperty; }
	public void setMaxValue(int value) { maximumValueProperty.set(value); }
	public int getMaxValue() { return maximumValueProperty.get(); }
}
