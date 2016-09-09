package de.lennartmeinhardt.moiree;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.lennartmeinhardt.moiree.ParserTextField.Parser;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/**
 * A control for setting up a double value via a {@link Slider} and {@link ParserTextField}.
 * 
 * @author Lennart Meinhardt
 */
public class DoubleValueSetup extends TitledPane implements Initializable {
	
	@FXML private Button resetButton;
	@FXML private Label valueLabel;
	@FXML private Slider valueSlider;
	@FXML private ParserTextField<Double> valueInput;
	
	private ReadOnlyObjectWrapper<ParserTextField<Double>> editor;

	private final DoubleProperty valueProperty = new SimpleDoubleProperty();
	private final DoubleProperty defaultValueProperty = new SimpleDoubleProperty();
	private final DoubleProperty minimumValueProperty = new SimpleDoubleProperty();
	private final DoubleProperty maximumValueProperty = new SimpleDoubleProperty();
	private final BooleanProperty animateChangesProperty = new SimpleBooleanProperty();
	private final StringProperty valueFormatterProperty = new SimpleStringProperty("%.3f");
	
	
	/**
	 * Create a new {@link DoubleValueSetup}.
	 */
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
		
		// use a basic double parser
		valueInput.setParser(Parser.DOUBLE_PARSER);
		// enter on text input stores the value
		valueInput.setOnAction(ev -> {
			if(valueInput.isParseSuccessful())
				setToValueAutoAnimate(valueInput.getParsedValue());
		});

		// escape resets to default
		setOnKeyPressed(ev -> {
			if(ev.getCode() == KeyCode.ESCAPE) {
				resetToDefault();
				ev.consume();
			}
		});
	}
	
	public void setExpandedNonCollapsible(boolean value) {
		setCollapsible(true);
		setExpanded(value);
		setCollapsible(false);
	}
	
	/**
	 * Reset to the default value.
	 */
	public void resetToDefault() {
		setToValueAutoAnimate(getDefaultValue());
	}
	
	public ParserTextField<Double> getEditor() {
		return valueInput;
	}
	public ReadOnlyObjectProperty<ParserTextField<Double>> editorProperty() {
		if(editor == null) {
			editor = new ReadOnlyObjectWrapper<>(this, "editor", valueInput);
		}
		return editor.getReadOnlyProperty();
	}
	
	/**
	 * Set the value, check if animate or not.
	 * 
	 * @param target the target value
	 */
	private void setToValueAutoAnimate(double target) {
		if(animateChangesProperty.get())
			animateToValue(target);
		else
			valueProperty.set(target);
	}
	
	/**
	 * Set the value, animated.
	 * 
	 * @param target the target value
	 */
	private void animateToValue(double target) {
		Timeline t = new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(valueProperty, target, Interpolator.EASE_BOTH)));
		t.setAutoReverse(false);
		t.play();
	}
	
	@FXML private void onResetButtonClicked(ActionEvent ev) {
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
	
	public void setAnimateChanges(boolean value) { animateChangesProperty.set(value); }
	public BooleanProperty animateChangesProperty() { return animateChangesProperty; }
	public boolean isAnimateChanges() { return animateChangesProperty.get(); }
	
	public void setValueFormatter(String value) { valueFormatterProperty.set(value); }
	public StringProperty valueFormatterProperty() { return valueFormatterProperty; }
	public String getValueFormatter() { return valueFormatterProperty.get(); }
}
