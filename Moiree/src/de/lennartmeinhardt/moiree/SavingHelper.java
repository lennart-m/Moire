package de.lennartmeinhardt.moiree;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 * Provides method for choosing a file to save to, and saving images.
 * 
 * @author Lennart Meinhardt
 */
public class SavingHelper {
	
	// file chooser to use for saving
	private final FileChooser exportFileChooser = new FileChooser();
	// localization
	private final ResourceBundle bundle;
	
	// last file of the chooser, use as initial file on re-open
	private File lastFile;
	
	
	/**
	 * Create a new {@link SavingHelper} with given localization.
	 * 
	 * @param bundle the localization to use
	 */
	public SavingHelper(ResourceBundle bundle) {
		this.bundle = bundle;
		ExtensionFilter extensionFilter = new ExtensionFilter(bundle.getString("saveableFilesName"), "*.png");
		exportFileChooser.getExtensionFilters().addAll(extensionFilter);
	}
	
	
	/**
	 * Saves an image. The target file is chosen using a {@link FileChooser}.
	 * 
	 * @param callingWindow the calling window
	 * @param imageToSave the image to save
	 */
	public void saveImage(Window callingWindow, Image imageToSave) {
		Optional<File> exportFile = getExportFile(callingWindow);
		if(exportFile.isPresent()) {
			// set the last file so re-opening is more comfortable
			lastFile = exportFile.get().getParentFile();
			saveImageToFile(imageToSave, exportFile.get());
		}
	}
	
	/**
	 * Make the user choose a file.
	 * 
	 * @param callingWindow the calling window 
	 * @return file to export to, or null
	 */
	private Optional<File> getExportFile(Window callingWindow) {
		// temporary last file
		File tmpLastFile = lastFile;
		// export file to use
		File exportFile = null;
		// re-open the chooser as long as no valid file was selected, or it was aborted
		while(! isValidExportFile(exportFile)) {
			if(tmpLastFile != null)
				exportFileChooser.setInitialDirectory(tmpLastFile);
			exportFile = exportFileChooser.showSaveDialog(callingWindow);
			
			// the file is null if the chooser is aborted
			if(exportFile == null)
				break;
			// if the chooser was not aborted, the chosen file is the new (temporary) last file
			else
				tmpLastFile = exportFile;
		}
		if(exportFile == null)
			return Optional.empty();
		else
			return Optional.of(exportFile);
	}
	
	/**
	 * Check if a file can be used to save an image to.
	 * This is the case if the file is not null or a directory, and if the canWrite method returns true.
	 * 
	 * @param file the file to check 
	 * @return true if the file can be written to, false else
	 */
	private static boolean isValidExportFile(File file) {
		return file != null && ! file.isDirectory() && (file.canWrite() || ! file.exists());
	}
	
	/**
	 * Save an image to a file.
	 * 
	 * @param fxImage the image to save
	 * @param exportFile the file to save to
	 */
	private void saveImageToFile(Image fxImage, File exportFile) {
		// convert to swing image
		BufferedImage awtImage = SwingFXUtils.fromFXImage(fxImage, null);
		
		try {
			ImageIO.write(awtImage, "png", exportFile);
		} catch (Exception e) {
			showSavingErrorDialog(e);
		}
	}
	
	/**
	 * Show a dialog if saving went wrong.
	 * 
	 * @param e the exception to display
	 */
	private void showSavingErrorDialog(Exception e) {
		showExceptionAlert(bundle.getString("error"), bundle.getString("savingErrorMessage"), e);
	}
	
	private static void showExceptionAlert(String title, String content, Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(e.getMessage());
		alert.setContentText(content);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		alert.getDialogPane().setExpandableContent(textArea);

		alert.showAndWait();
	}
}
