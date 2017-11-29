package com.erricson.tttool.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.erricson.tttool.controllers.Controller;
import com.erricson.tttool.controllers.ControllerImp;
import com.erricson.tttool.controllers.NotificationMessage;
import com.erricson.tttool.view.api.View;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Launcher extends Application implements View {
	// ------------------||
	// class variables --||
	// ------------------||
	private static final Logger LOG = Logger.getLogger(Launcher.class);
	static {
		BasicConfigurator.configure();
	}
	private static final String MAIN_VIEW_PATH = "views/MainView.fxml";
	private static final String MAIN_STYLE_SHEET = "application.css";
	private static final String ICON_PATH = "icon3.png";

	// ---------------------||
	// instance variables --||
	// ---------------------||
	private Stage primaryStage;

	@FXML
	private Button startButton;

	@FXML
	private Button browseSourcePath;

	@FXML
	private Button browseCriteriaPath;

	@FXML
	private Button browseDestinationPath;

	@FXML
	private TextField sourcePath;

	@FXML
	private TextField criteriaPath;

	@FXML
	private TextField destinationPath;

	@FXML
	private HBox progressHB;

	@FXML
	private Label outputPathLabel;

	@FXML
	private CheckBox exportOption;

	@FXML
	private CheckBox overrideOption;

	private ProgressBar pa;
	private File rootDirectory;
	private boolean overwriteOriginal = true;
	private boolean writeToOuter = false;

	private List<NotificationMessage> notificationMessages = new ArrayList<>();
	private List<String> messages = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource(MAIN_VIEW_PATH));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource(MAIN_STYLE_SHEET).toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_PATH)));
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			LOG.info("failed to load the main view", e);
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public String getSourcePath() {
		return sourcePath.getText();
	}

	@Override
	public String getCriteriaPath() {
		return criteriaPath.getText();
	}

	@Override
	public String getDestinationPath() {
		return destinationPath.getText();
	}

	@Override
	public void showMessage(String message) {
		this.messages.add(message);
	}

	private void showMessageOnUI(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	@Override
	public void showErrorMessage(NotificationMessage message) {
		this.notificationMessages.add(message);
	}

	private void showNotificationMessageOnUI(NotificationMessage message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Message");
		alert.setHeaderText(message.getMessage());
		alert.setContentText(message.getCommonErrors());
		alert.showAndWait();
	}

	private String getFileFromSystem() {
		FileChooser fileChooser = new FileChooser();
		if (rootDirectory != null) {
			fileChooser.setInitialDirectory(rootDirectory);
		}
		ExtensionFilter extFilter = new ExtensionFilter("Excel files", "*.xlsx", "*.xls", "*.xlsb", "*.xltx");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(primaryStage);
		String result = "";
		if (file != null) {
			result = file.getAbsolutePath();
			rootDirectory = file.getParentFile();
		}
		return result;
	}

	public void startHandler(ActionEvent event) {
		Controller controller = new ControllerImp();
		pa = new ProgressBar();
		progressHB.getChildren().add(pa);
		progressHB.setAlignment(Pos.CENTER);
		pa.autosize();
		pa.setMinWidth(300);
		startButton.setDisable(true);
		Launcher self = this;
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				if (isCancelled())
					return null;
				controller.start(self, overwriteOriginal, writeToOuter);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						progressHB.getChildren().remove(pa);
						messages.forEach(self::showMessageOnUI);
						messages.clear();
						notificationMessages.forEach(self::showNotificationMessageOnUI);
						notificationMessages.clear();
						startButton.setDisable(false);
					}
				});
				return null;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	public void browseSourcePathHandler() {
		String filePath = this.getFileFromSystem();
		if (!filePath.isEmpty()) {
			sourcePath.setText(filePath);
		}
	}

	public void browseCriteriaPathHandler() {
		String filePath = this.getFileFromSystem();
		if (!filePath.isEmpty()) {
			criteriaPath.setText(filePath);
		}
	}

	public void changeOutputerExportState(ActionEvent event) {
		if (event.getSource() instanceof CheckBox) {
			CheckBox ch = (CheckBox) event.getSource();
			writeToOuter = ch.isSelected();
			boolean state = !ch.isSelected();
			outputPathLabel.setDisable(state);
			destinationPath.setDisable(state);
			browseDestinationPath.setDisable(state);
			overrideOption.setDisable(state);
		}
		overrideOption.setSelected(false);
	}
	
	public void changeOverwrite(ActionEvent event) {
		if (event.getSource() instanceof CheckBox) {
			CheckBox ch = (CheckBox) event.getSource();
			overwriteOriginal = ch.isSelected();
		}
	}

	public void browseDestinationPathHandler() {
		FileChooser fileChooser = new FileChooser();
		if (rootDirectory != null) {
			fileChooser.setInitialDirectory(rootDirectory);
		}
		ExtensionFilter extFilter = new ExtensionFilter("Excel files", "*.xlsx", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(primaryStage);
		String result = "";
		if (file != null) {
			result = file.getAbsolutePath();
			rootDirectory = file.getParentFile();
		}
		if (!result.isEmpty()) {
			destinationPath.setText(result);
		}
	}

	@Override
	public void setProgress(Double progress) {
		pa.setProgress(progress);
	}
}
