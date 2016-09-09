package de.lennartmeinhardt.moiree;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

/**
 * Core class for managing the Moiré window.
 * 
 * @author Lennart Meinhardt
 */
public class MoireeController implements Initializable {
	
	/**
	 * TODO handle touch input
	 */
	
	@FXML private Node root;
	
	@FXML private StackPane moireePane;
	@FXML private ImageView untransformedView;
	@FXML private ImageView transformedView;
	@FXML private Rectangle moireeBackground;

	@FXML private TitledPane transformationSetup;
	@FXML private TransformationSetupController transformationSetupController;

	@FXML private ColorPicker foregroundColorPicker;
	@FXML private ColorPicker backgroundColorPicker;
	
	@FXML private TitledPane imagePane;
	
	private final Property<WritableImage> imageProperty = new SimpleObjectProperty<>();
	
	private ResourceBundle resources;
	private final ImageSettings imageSettings = new ImageSettings();
	private final PreferencesHelper preferences = new PreferencesHelper();
	private SavingHelper ioHelper;
	
	// mouse event listeners
	private double startX, startY;
	private double lastX, lastY;
	
	
	@Override public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
		this.ioHelper = new SavingHelper(resources);

		initMoireePane();
		initializeTransformationBindings();
		initMouseHandlers();
		loadMoireeOptions();
		
		recalculateImage();
	}
	
	private void initMoireePane() {
		// create the effect in order to be able to colorize images
		Blend blend = new Blend(BlendMode.SRC_ATOP);
		ColorInput imageColorEffect = new ColorInput();
		imageColorEffect.widthProperty().bind(imageSettings.imageWidthProperty());
		imageColorEffect.heightProperty().bind(imageSettings.imageHeightProperty());
		imageColorEffect.paintProperty().bind(foregroundColorPicker.valueProperty());
		blend.setTopInput(imageColorEffect);
		
		untransformedView.setEffect(blend);
		transformedView.setEffect(blend);
		
		untransformedView.imageProperty().bind(imageProperty);
		transformedView.imageProperty().bind(imageProperty);
		
		moireeBackground.fillProperty().bind(backgroundColorPicker.valueProperty());
	}
	
	private void initializeTransformationBindings() {
		transformedView.rotateProperty().bind(transformationSetupController.rotateProperty());
		transformedView.translateXProperty().bind(transformationSetupController.translateXProperty());
		transformedView.translateYProperty().bind(transformationSetupController.translateYProperty());
		transformedView.scaleXProperty().bind(transformationSetupController.autoScaleXBinding());
		transformedView.scaleYProperty().bind(transformationSetupController.autoScaleYBinding());
	}
	
	private void initMouseHandlers() {
		// common scaling on scroll
		moireePane.setOnScroll(ev -> {
			transformationSetupController.setCommonScale(transformationSetupController.getCommonScale() + ev.getTextDeltaY() / 1000);
			transformationSetupController.setUseCommonScale(true);
		});
		// whenever mouse was pressed, remember the start and last coordinates
		moireePane.setOnMousePressed(ev -> {
			this.startX = ev.getX();
			this.startY = ev.getY();
			this.lastX = startX;
			this.lastY = startY;
		});
		// handle mouse dragging. left: translate, right: rotate, middle: individual scalings
		moireePane.setOnMouseDragged(ev -> {
			double x = ev.getX();
			double dx = x - lastX;
			lastX = x;
			
			double y = ev.getY();
			double dy = y - lastY;
			lastY = y;
			
			Image image = imageProperty.getValue();
			
			// left dragging: translations
			if(ev.isPrimaryButtonDown()) {
				transformationSetupController.setTranslateX(transformationSetupController.getTranslateX() + dx / 10);
				transformationSetupController.setTranslateY(transformationSetupController.getTranslateY() + dy / 10);
			}
			// right dragging: rotation
			else if(ev.isSecondaryButtonDown()) {
				double dr = Math.signum(x - untransformedView.getLayoutX() - image.getWidth() / 2) * dy - Math.signum(y - untransformedView.getLayoutY() - image.getHeight() / 2) * dx;
				transformationSetupController.setRotate(transformationSetupController.getRotate() + dr / 60);
			}
			// middle dragging: scalings
			else if(ev.isMiddleButtonDown()) {
				transformationSetupController.setScaleX(transformationSetupController.getScaleX() + dx * .5 / image.getWidth());
				transformationSetupController.setScaleY(transformationSetupController.getScaleY() - dy * .5 / image.getHeight());
				transformationSetupController.setUseCommonScale(false);
			}
		});
	}
	
	private void loadMoireeOptions() {
		transformationSetupController.loadFromPreferences(preferences);

		// image settings
		imageSettings.setImageMode(MoireeImageMode.getInstanceOrDefault(preferences.getImageModeIndex(0)));
		imageSettings.setImageWidth(preferences.getImageWidth(1000));
		imageSettings.setImageHeight(preferences.getImageHeight(1000));
		imageSettings.setPixelSize(preferences.getPixelSize(1));
		imageSettings.setPixelDensity(preferences.getPixelDensity(20));

		transformationSetup.setExpanded(preferences.isTransformationSetupExpanded(true));
		imagePane.setExpanded(preferences.isImagePaneExpanded(true));

		foregroundColorPicker.setValue(preferences.getForegroundColor(Color.BLACK));
		backgroundColorPicker.setValue(preferences.getBackgroundColor(Color.WHITE));
	}
	
	/**
	 * Create a new image.
	 */
	private void recalculateImage() {
		WritableImage image = imageProperty.getValue();
		
		int width = imageSettings.getImageWidth();
		int height = imageSettings.getImageHeight();
		
		if(image == null || image.getWidth() != width || image.getHeight() != height) {
			image = new WritableImage(width, height);
			imageProperty.setValue(image);
		}
		
		imageSettings.getImageMode().getImageDrawer().drawToImage(imageProperty.getValue(), imageSettings);
	}
	

	@FXML private void onNewImageClicked(ActionEvent ev) {
		Node root;
		ImageSetupController controller;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageSetup.fxml"));
			loader.setResources(ResourceBundle.getBundle("de.lennartmeinhardt.moiree.bundles.Moiree"));
			root = loader.load();
			controller = loader.getController();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		
		Dialog<ButtonType> imageSetupDialog = new Dialog<>();
		imageSetupDialog.setTitle(resources.getString("imageSetupTitle"));
		imageSetupDialog.setHeaderText(resources.getString("imageSetupTitle"));
		imageSetupDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		imageSetupDialog.getDialogPane().setContent(root);
		imageSetupDialog.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		imageSetupDialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(controller.allInputValidBinding().not());
		
		controller.loadFromImageSettings(imageSettings);
		
		Optional<ButtonType> result = imageSetupDialog.showAndWait();
		if(result.get() == ButtonType.OK) {
			controller.storeToImageSettings(imageSettings);
			recalculateImage();
		}
	}
	
	@FXML private void onSaveClicked(ActionEvent e) {
		Window window = root.getScene().getWindow();
		Image imageToSave = getSaveImage();
		root.setDisable(true);
		ioHelper.saveImage(window, imageToSave);
		root.setDisable(false);
	}
	
	private Image getSaveImage() {
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(backgroundColorPicker.getValue());
		WritableImage img = moireePane.snapshot(params, null);
		return img;
	}
	
	public void onDispose() {
		storeMoireeOptions();
	}
	
	private void storeMoireeOptions() {
		transformationSetupController.storeToPreferences(preferences);
		
		// image settings
		preferences.setImageModeIndex(imageSettings.getImageMode().ordinal());
		preferences.setImageWidth(imageSettings.getImageWidth());
		preferences.setImageHeight(imageSettings.getImageHeight());
		preferences.setPixelSize(imageSettings.getPixelSize());
		preferences.setPixelDensity(imageSettings.getPixelDensity());

		preferences.setTransformationSetupExpanded(transformationSetup.isExpanded());
		preferences.setImagePaneExpanded(imagePane.isExpanded());

		preferences.setBackgroundColor(backgroundColorPicker.getValue());
		preferences.setForegroundColor(foregroundColorPicker.getValue());
	}
}
