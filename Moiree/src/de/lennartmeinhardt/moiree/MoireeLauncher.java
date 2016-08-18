package de.lennartmeinhardt.moiree;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Launches the Moiré window and manages saving and restoring window dimensions.
 * 
 * @author Lennart Meinhardt
 */
public class MoireeLauncher extends Application {
	
	// window preferences to store dimensions to
	private final WindowPreferences windowPreferences = new WindowPreferences();
	// hold the controller in order to save moiré settings on stop
	private MoireeController controller;
	// hold the stage in order to access dimensions on stop
	private Stage primaryStage;

	
	@Override public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(MoireeController.class.getResource("Moiree.fxml"));
			// load the localization
			loader.setResources(ResourceBundle.getBundle("de.lennartmeinhardt.moiree.bundles.Moiree"));
			BorderPane root = (BorderPane) loader.load();
			this.controller = loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Moiré");
			primaryStage.show();
			primaryStage.centerOnScreen();
			
			// restore and set window dimensions
			boolean maximized = windowPreferences.getWindowMaximized(false);
			primaryStage.setMaximized(maximized);
			if(! maximized) {
				primaryStage.setWidth(windowPreferences.getWindowWidth(primaryStage.getWidth()));
				primaryStage.setHeight(windowPreferences.getWindowHeight(primaryStage.getHeight()));
			}
			
			primaryStage.show();
			primaryStage.centerOnScreen();
			this.primaryStage = primaryStage;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override public void stop() throws Exception {
		// save window dimensions
		windowPreferences.setWindowWidth(primaryStage.getWidth());
		windowPreferences.setWindowHeight(primaryStage.getHeight());
		windowPreferences.setWindowMaximized(primaryStage.isMaximized());
		
		// save moiré settings
		controller.storeToPreferences();
		
		super.stop();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
