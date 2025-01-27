package net.andrecarbajal.minecontrolfx.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.andrecarbajal.minecontrolfx.Constants;
import net.andrecarbajal.minecontrolfx.download.ServerDownload;
import net.andrecarbajal.minecontrolfx.loader.ILoader;
import net.andrecarbajal.minecontrolfx.loader.Loaders;
import net.andrecarbajal.minecontrolfx.util.os.DirectoryCreator;
import net.andrecarbajal.minecontrolfx.util.os.OsChecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class CreateController implements Initializable {
    @FXML
    private TextField nameTextField;

    @FXML
    private ListView<ILoader> loaderListView;

    @FXML
    private ChoiceBox<String> versionChoiceBox;

    @FXML
    private Button createButton;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loaderListView.getItems().addAll(Loaders.getLoaders());

        loaderListView.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(ILoader item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getLoaderName());
                }
            }
        });

        loaderListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                var versions = newValue.getVersions();
                versionChoiceBox.getItems().clear();
                versionChoiceBox.setValue(versions.get(0));
                versionChoiceBox.getItems().addAll(versions);
            }
            updateCreateButtonState();
        });

        updateCreateButtonState();
    }

    private void updateCreateButtonState() {
        boolean isTextFilled = !nameTextField.getText().isBlank() && !nameTextField.getText().isEmpty();
        boolean isLoaderSelected = loaderListView.getSelectionModel().getSelectedItem() != null;
        createButton.setDisable(!(isLoaderSelected && isTextFilled));
    }

    public void createButtonAction(ActionEvent actionEvent) {
        Path serverFolder = DirectoryCreator.getServerFolder(OsChecker.getOperatingSystemType()).resolve(nameTextField.getText());
        if (Files.notExists(serverFolder)) {
            try {
                Files.createDirectories(serverFolder);
            } catch (Exception e) {
                Constants.LOGGER.error("Failed to create server folder: {}", serverFolder, e);
            }
        }

        Task<Void> downloadTask = getVoidTask(serverFolder);

        statusLabel.textProperty().bind(downloadTask.messageProperty());

        downloadTask.setOnSucceeded(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("Download complete");
            copyIconToServerFolder(serverFolder);
        });
        downloadTask.setOnFailed(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("Download failed");
        });

        new Thread(downloadTask).start();
    }

    private Task<Void> getVoidTask(Path serverFolder) {
        ServerDownload serverDownload = new ServerDownload();
        ILoader selectedLoader = loaderListView.getSelectionModel().getSelectedItem();
        String selectedVersion = versionChoiceBox.getValue();

        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Downloading...");
                updateProgress(0, 1);
                serverDownload.download(selectedLoader, selectedVersion, serverFolder);
                updateProgress(1, 1);
                updateMessage("Download complete");
                return null;
            }
        };
    }

    private void copyIconToServerFolder(Path serverFolder) {
        ILoader selectedLoader = loaderListView.getSelectionModel().getSelectedItem();
        if (selectedLoader == null) {
            Constants.LOGGER.warn("No loader selected to copy icon");
            return;
        }

        try (InputStream iconStream = getClass().getResourceAsStream(selectedLoader.getLoaderIcon().getUrl())) {
            if (iconStream == null) {
                Constants.LOGGER.error("Icon resource not found for loader: {}", selectedLoader.getLoaderIcon().getUrl());
                return;
            }

            Path iconPath = serverFolder.resolve("icon.png");
            Files.copy(iconStream, iconPath, StandardCopyOption.REPLACE_EXISTING);
            Constants.LOGGER.info("Successfully copied icon to: {}", iconPath);

        } catch (IOException e) {
            Constants.LOGGER.error("Failed to copy icon to server folder: {}", serverFolder, e);
        }
    }
}