package net.andrecarbajal.minecontrolfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.andrecarbajal.minecontrolfx.loader.ILoader;
import net.andrecarbajal.minecontrolfx.loader.Loaders;

import java.net.URL;
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
        boolean isVersionSelected = versionChoiceBox.getSelectionModel().getSelectedItem() != null;
        createButton.setDisable(!(isLoaderSelected && isVersionSelected && isTextFilled));
    }
}
