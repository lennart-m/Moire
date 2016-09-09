package de.lennartmeinhardt.moiree;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import de.lennartmeinhardt.moiree.ParserTextField.Parser;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

/**
 * Core class for managing the image setup.
 * 
 * @author Lennart Meinhardt
 */
public class ImageSetupController implements Initializable {
	
	@FXML private GridPane imageSetupGrid;
	@FXML private ComboBox<MoireeImageMode> moireeModeCombobox;
	@FXML private ParserTextField<Integer> widthInput;
	@FXML private ParserTextField<Integer> heightInput;
	@FXML private ParserTextField<Integer> pixelSizeInput;
	@FXML private ParserTextField<Double> pixelDensityInput;
	@FXML private Label pixelDensityLabel;

	// determines if all input is valid
	private BooleanBinding allInputValidBinding;
	
	
	@Override public void initialize(URL location, ResourceBundle resources) {
		initializeImageSetupBindings(resources);
	}
	
	private void initializeImageSetupBindings(ResourceBundle resources) {
		// setup image mode combo box
		moireeModeCombobox.getItems().addAll(
				MoireeImageMode.RANDOM,
				MoireeImageMode.SQUARES,
				MoireeImageMode.TRIANGLES,
				MoireeImageMode.HORIZONTAL_LINES
		);
		moireeModeCombobox.setConverter(new StringConverter<MoireeImageMode>() {
			@Override public String toString(MoireeImageMode object) {
				return object.getName(resources);
			}
			
			@Override public MoireeImageMode fromString(String string) {
				throw new RuntimeException();
			}
		});
		
		Predicate<Integer> pixelSizeChecker = i -> i > 0;
		pixelSizeInput.setParser(Parser.INT_PARSER.withVeto(pixelSizeChecker));
		
		Predicate<Double> pixelDensityChecker = i -> i >= 0 && i <= 100;
		pixelDensityInput.setParser(Parser.DOUBLE_PARSER.withVeto(pixelDensityChecker));

		Predicate<Integer> dimensionChecker = i -> i >= 100 && i <= 10000;
		Parser<Integer> dimensionParser = (Parser.INT_PARSER).withVeto(dimensionChecker);
		widthInput.setParser(dimensionParser);
		heightInput.setParser(dimensionParser);
		
		// disable density setup if random isn't selected
		BooleanBinding modeIsNotRandom = moireeModeCombobox.valueProperty().isNotEqualTo(MoireeImageMode.RANDOM);
		pixelDensityLabel.disableProperty().bind(modeIsNotRandom);
		pixelDensityInput.disableProperty().bind(modeIsNotRandom);
		
		// create the binding that checks if all input is correct
		allInputValidBinding = moireeModeCombobox.getSelectionModel().selectedItemProperty().isNotNull()
				.and(widthInput.parseSuccessfulProperty())
				.and(heightInput.parseSuccessfulProperty())
				.and(pixelSizeInput.parseSuccessfulProperty())
				.and(pixelDensityInput.parseSuccessfulProperty());
	}
	
	
	/**
	 * Load the UI data from given {@link ImageSettings}.
	 * 
	 * @param settings the settings to load from
	 */
	public void loadFromImageSettings(ImageSettings settings) {
		pixelDensityInput.setText("" + settings.getPixelDensity());
		pixelSizeInput.setText("" + settings.getPixelSize());
		moireeModeCombobox.getSelectionModel().select(settings.getImageMode().ordinal());
		widthInput.setText("" + settings.getImageWidth());
		heightInput.setText("" + settings.getImageHeight());
	}
	
	/**
	 * Store the UI data to the given {@link ImageSettings}.
	 * 
	 * @param settings the settings to store to
	 */
	public void storeToImageSettings(ImageSettings settings) {
		settings.setPixelDensity(pixelDensityInput.getParsedValue());
		settings.setPixelSize(pixelSizeInput.getParsedValue());
		settings.setImageMode(MoireeImageMode.getInstance(moireeModeCombobox.getSelectionModel().getSelectedIndex()));
		settings.setImageWidth(widthInput.getParsedValue());
		settings.setImageHeight(heightInput.getParsedValue());
	}
	
	public BooleanBinding allInputValidBinding() {
		return allInputValidBinding;
	}
	public boolean isAllInputValid() {
		return allInputValidBinding.get();
	}
}
