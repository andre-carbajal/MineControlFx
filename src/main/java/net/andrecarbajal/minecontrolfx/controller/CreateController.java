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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

        versionChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateCreateButtonState();
        });

        updateCreateButtonState();
    }

    private void updateCreateButtonState() {
        boolean isTextFilled = !nameTextField.getText().isBlank() && !nameTextField.getText().isEmpty();
        boolean isLoaderSelected = loaderListView.getSelectionModel().getSelectedItem() != null;
        createButton.setDisable(!(isLoaderSelected && isTextFilled));
    }

    public void createButtonAction(ActionEvent actionEvent) throws IOException {
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
}