package de.lennartmeinhardt.moiree;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Window;

/**
 * Core class for managing the Moiré window.
 * 
 * @author Lennart Meinhardt
 */
public class MoireeController implements Initializable {
	
	private static final int IMAGE_WIDTH = 1000;
	private static final int IMAGE_HEIGHT = 1000;

	@FXML private StackPane moireePane;
	
	@FXML private Pane xyScalingsControlsPane;
	
	@FXML private ImageView backgroundView;
	@FXML private ImageView transformedView;

	@FXML private DoubleValueSetup densitySetup;
	@FXML private DoubleValueSetup rotationSetup;
	@FXML private DoubleValueSetup translationXSetup;
	@FXML private DoubleValueSetup translationYSetup;
	@FXML private DoubleValueSetup commonScalingSetup;
	@FXML private DoubleValueSetup scalingXSetup;
	@FXML private DoubleValueSetup scalingYSetup;
	
	@FXML private IntValueSetup pixelSizeSetup;
	
	@FXML private CheckBox commonScalingCheckBox;
	
	@FXML private ComboBox<NamedMoireeImageMode> moireeModeCombobox;
	
	private final BooleanProperty commonScalingProperty = new SimpleBooleanProperty();
	private final DoubleProperty densityProperty = new SimpleDoubleProperty();
	private final IntegerProperty pixelSizeProperty = new SimpleIntegerProperty();
	private final WritableImage image = new WritableImage(IMAGE_WIDTH, IMAGE_HEIGHT);
	
	private final MoireePreferences preferences = new MoireePreferences();
	
	private SavingHelper ioHelper;
	
	// mouse event listeners
	private double startX, startY;
	private double lastX, lastY;
	

	private static final String KEY_MODE_RANDOM = "modeRandom";
	private static final String KEY_MODE_SQUARES = "modeSquares";
	private static final String KEY_MODE_TRIANGLES = "modeTriangles";
	private static final String KEY_MODE_HORIZONAL_LINES = "modeHorizontalLines";
	
	
	@FXML private void newImageClicked(ActionEvent ev) {
		recalculateImage();
	}
	
	/**
	 * Create a new image.
	 */
	private void recalculateImage() {
		drawToImage();
		backgroundView.setImage(image);
		transformedView.setImage(image);
	}
	
	@Override public void initialize(URL location, ResourceBundle resources) {
		this.ioHelper = new SavingHelper(resources);
		
		// bind transformation setup controls
		commonScalingCheckBox.selectedProperty().addListener(e -> {
			if(commonScalingCheckBox.isSelected())
				commonScalingSetup.toFront();
			else
				xyScalingsControlsPane.toFront();
		});
		
		scalingXSetup.managedProperty().bind(scalingXSetup.visibleProperty());
		scalingYSetup.managedProperty().bind(scalingYSetup.visibleProperty());
		
		commonScalingCheckBox.selectedProperty().bindBidirectional(commonScalingProperty);
		scalingXSetup.visibleProperty().bind(commonScalingProperty.not());
		scalingYSetup.visibleProperty().bind(commonScalingProperty.not());
		commonScalingSetup.visibleProperty().bind(commonScalingProperty);

		NumberBinding scaleXBinding = Bindings.when(commonScalingProperty).then(commonScalingSetup.valueProperty()).otherwise(scalingXSetup.valueProperty());
		NumberBinding scaleYBinding = Bindings.when(commonScalingProperty).then(commonScalingSetup.valueProperty()).otherwise(scalingYSetup.valueProperty());
		
		transformedView.rotateProperty().bind(rotationSetup.valueProperty());
		transformedView.translateXProperty().bind(translationXSetup.valueProperty());
		transformedView.translateYProperty().bind(translationYSetup.valueProperty());
		transformedView.scaleXProperty().bind(scaleXBinding);
		transformedView.scaleYProperty().bind(scaleYBinding);
		
		densitySetup.valueProperty().bindBidirectional(densityProperty);
		pixelSizeSetup.valueProperty().bindBidirectional(pixelSizeProperty);
		
		// setup image mode combo box
		NamedMoireeImageMode randomMode = new NamedMoireeImageMode(KEY_MODE_RANDOM, resources);
		moireeModeCombobox.getItems().addAll(
				randomMode,
				new NamedMoireeImageMode(KEY_MODE_SQUARES, resources),
				new NamedMoireeImageMode(KEY_MODE_TRIANGLES, resources),
				new NamedMoireeImageMode(KEY_MODE_HORIZONAL_LINES, resources)
		);
		
		// disable density setup if random isn't selected
		densitySetup.disableProperty().bind(moireeModeCombobox.valueProperty().isNotEqualTo(randomMode));
		// collapse density setup if random isn't selected
		moireeModeCombobox.valueProperty().addListener(e -> {
			densitySetup.setCollapsible(true);
			densitySetup.setExpanded(moireeModeCombobox.getValue().equals(randomMode));
			densitySetup.setCollapsible(false);
		});

		loadMoireeOptions();
		initMouseHandlers();
		recalculateImage();
	}

	private void initMouseHandlers() {
		// common scaling on scroll
		moireePane.setOnScroll(e -> {
			if(commonScalingProperty.get()) {
				double scale = Math.max(commonScalingSetup.getValue() + e.getTextDeltaY() / 600, 0);
				commonScalingSetup.setValue(scale);
			}
		});
		// whenever mouse was pressed, remember the start and last coordinates
		moireePane.setOnMousePressed(e -> {
			this.startX = e.getX();
			this.startY = e.getY();
			this.lastX = startX;
			this.lastY = startY;
		});
		// handle mouse dragging. left: translate, right: rotate, middle: individual scalings
		moireePane.setOnMouseDragged(e -> {
			double x = e.getX();
			double dx = x - lastX;
			lastX = x;
			
			double y = e.getY();
			double dy = y - lastY;
			lastY = y;
			
			// left dragging: translations
			if(e.isPrimaryButtonDown()) {
				translationXSetup.setValue(translationXSetup.getValue() + dx);
				translationYSetup.setValue(translationYSetup.getValue() + dy);
			}
			// right dragging: rotation
			else if(e.isSecondaryButtonDown()) {
				double dr = Math.signum(x - backgroundView.getLayoutX() - image.getWidth() / 2) * dy - Math.signum(y - backgroundView.getLayoutY() - image.getHeight() / 2) * dx;
				double r = rotationSetup.getValue() + dr / 60;
				rotationSetup.setValue(r);
			}
			// middle dragging: scalings
			else if(e.isMiddleButtonDown()) {
				if(! commonScalingProperty.get()) {
					double sx = Math.max(scalingXSetup.getValue() + dx / image.getWidth(), 0);
					scalingXSetup.setValue(sx);
	
					double sy = Math.max(scalingYSetup.getValue() - dy / image.getHeight(), 0);
					scalingYSetup.setValue(sy);
				}
			}
		});
	}
	
	private void loadMoireeOptions() {
		preferences.initRotationSetup(rotationSetup);
		preferences.initTranslationXSetup(translationXSetup);
		preferences.initTranslationYSetup(translationYSetup);
		preferences.initScalingXSetup(scalingXSetup);
		preferences.initScalingYSetup(scalingYSetup);
		preferences.initPixelDensitySetup(densitySetup);
		preferences.initPixelSizeSetup(pixelSizeSetup);
		preferences.initCommonScalingSetup(commonScalingSetup);
		commonScalingProperty.set(preferences.isUseCommonScaling());
		moireeModeCombobox.getSelectionModel().select(preferences.getImageModeIndex());
	}
	
	private void storeMoireeOptions() {
		preferences.setRotation(rotationSetup.getValue());
		preferences.setTranslationX(translationXSetup.getValue());
		preferences.setTranslationY(translationYSetup.getValue());
		preferences.setScalingX(scalingXSetup.getValue());
		preferences.setScalingY(scalingYSetup.getValue());
		preferences.setPixelDensity(densityProperty.get());
		preferences.setPixelSize(pixelSizeProperty.get());
		preferences.setUseCommonScaling(commonScalingProperty.get());
		preferences.setCommonScaling(commonScalingSetup.getValue());
		preferences.setImageModeIndex(moireeModeCombobox.getSelectionModel().getSelectedIndex());
	}
	
	public void storeToPreferences() {
		storeMoireeOptions();
	}
	
	private Rectangle2D getImageBounds() {
		Bounds backgroundBounds = backgroundView.getBoundsInParent();
		Bounds transformedBounds = transformedView.getBoundsInParent();
		double minX = Math.min(backgroundBounds.getMinX(), transformedBounds.getMinX());
		double minY = Math.min(backgroundBounds.getMinY(), transformedBounds.getMinY());
		double maxX = Math.max(backgroundBounds.getMaxX(), transformedBounds.getMaxX());
		double maxY = Math.max(backgroundBounds.getMaxY(), transformedBounds.getMaxY());
		double width = maxX - minX + 1;
		double height = maxY - minY + 1;
		
		return new Rectangle2D(minX, minY, width, height);
	}
	
	private Image getSaveImage() {
		WritableImage img = new WritableImage(image.getPixelReader(), IMAGE_WIDTH, IMAGE_HEIGHT);
		SnapshotParameters params = new SnapshotParameters();
		params.setViewport(getImageBounds());
		params.setFill(Color.WHITE);
		img = moireePane.snapshot(params, null);
		return img;
	}
	
	@FXML private void saveClicked(ActionEvent e) {
		Scene scene = moireePane.getScene();
		scene.getRoot().setDisable(true);
		Window window = moireePane.getScene().getWindow();
		Image imageToSave = getSaveImage();
		ioHelper.saveImage(window, imageToSave);
		scene.getRoot().setDisable(false);
	}
	
	
	private void drawToImage() {
		switch(moireeModeCombobox.getValue().key) {
		case KEY_MODE_RANDOM:
			ImageDrawer.drawRandomPixelsToImage(image, densityProperty.get(), pixelSizeProperty.get());
			break;
		case KEY_MODE_SQUARES:
			ImageDrawer.drawCheckerboardToImage(image, pixelSizeProperty.get());
			break;
		case KEY_MODE_TRIANGLES:
			ImageDrawer.drawTrianglesToImage(image, pixelSizeProperty.get());
			break;
		case KEY_MODE_HORIZONAL_LINES:
			ImageDrawer.drawHorizontalLinesToImage(image, pixelSizeProperty.get());
			break;
		}
	}
	
	
	/**
	 * Simple object to use in combo box. "key" is the internal entry's name, "name" is the entry's name from a resource bundle.
	 * 
	 * @author Lennart Meinhardt
	 */
	private class NamedMoireeImageMode {
		public final String key;
		public final String name;
		
		public NamedMoireeImageMode(String key, String name) {
			this.key = key;
			this.name = name;
		}
		
		public NamedMoireeImageMode(String key, ResourceBundle resources) {
			this(key, resources.getString(key));
		}
		
		@Override public String toString() {
			return name;
		}
		
		@Override public boolean equals(Object obj) {
			if(! (obj instanceof NamedMoireeImageMode))
				return false;
			else
				return this.key.equals(((NamedMoireeImageMode) obj).key);
		}
		
		@Override public int hashCode() {
			return key.hashCode();
		}
	}
}
