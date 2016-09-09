package de.lennartmeinhardt.moiree;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
		// load the localization
//		Locale.setDefault(new Locale(""));
		ResourceBundle resources = ResourceBundle.getBundle("de.lennartmeinhardt.moiree.bundles.Moiree");
		
		Parent root;
		// load the moiree UI
		try {
			FXMLLoader loader = new FXMLLoader(MoireeController.class.getResource("Moiree.fxml"));
			loader.setResources(resources);
			root = loader.load();
			this.controller = loader.getController();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(MoireeLauncher.class.getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(resources.getString("moiree"));
		primaryStage.centerOnScreen();
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.show();
		
		// restore and set window dimensions
		boolean maximized = windowPreferences.isWindowMaximized(false);
		primaryStage.setMaximized(maximized);
		if(! maximized) {
			primaryStage.setWidth(windowPreferences.getWindowWidth(primaryStage.getWidth()));
			primaryStage.setHeight(windowPreferences.getWindowHeight(primaryStage.getHeight()));
		}
		
		primaryStage.show();
		primaryStage.centerOnScreen();
		this.primaryStage = primaryStage;
	}
	
	@Override public void stop() throws Exception {
		// save window dimensions
		windowPreferences.setWindowWidth(primaryStage.getWidth());
		windowPreferences.setWindowHeight(primaryStage.getHeight());
		windowPreferences.setWindowMaximized(primaryStage.isMaximized());
		
		// save moiré settings
		controller.onDispose();
		
		super.stop();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
