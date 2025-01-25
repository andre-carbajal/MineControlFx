package net.andrecarbajal.minecontrolfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.andrecarbajal.minecontrolfx.Constants;
import net.andrecarbajal.minecontrolfx.util.ServerList;
import net.andrecarbajal.minecontrolfx.util.os.DirectoryCreator;
import net.andrecarbajal.minecontrolfx.util.os.OsChecker;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private GridPane rootGridPane;

    @FXML
    private ListView<ServerList> serverList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Path documentsPath = Path.of(DirectoryCreator.getFileFolder(OsChecker.getOperatingSystemType()));

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(documentsPath)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    Image icon = new Image(Files.newInputStream(path.resolve("icon.png")));
                    serverList.getItems().add(new ServerList(path.getFileName().toString(), icon));
                }
            }
        } catch (IOException e) {
            Constants.LOGGER.error("Error reading the server directory", e);
        }

        serverList.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(ServerList item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(item.icon());
                    imageView.setFitWidth(64);
                    imageView.setFitHeight(64);
                    setText(item.directoryName());
                    setGraphic(imageView);
                }
            }
        });
    }

    @FXML
    private void openServer(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            ServerList selectedItem = serverList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Stage newWindow = new Stage();
                newWindow.initModality(Modality.APPLICATION_MODAL);

                newWindow.setTitle("Detalles del servidor");
                Label label = new Label("Nombre del servidor: " + selectedItem.directoryName());
                Scene scene = new Scene(label, 200, 200);
                newWindow.setScene(scene);

                newWindow.show();
            }
        }
    }

    @FXML
    private void createServer(ActionEvent actionEvent) {
        openServerView("/net/andrecarbajal/minecontrolfx/create-view.fxml");
    }

    @FXML
    private void openServerView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            ScrollPane newScrollPane = loader.load();

            rootGridPane.getChildren().removeIf(node ->
                    GridPane.getColumnIndex(node) != null &&
                            GridPane.getColumnIndex(node) == 1 &&
                            GridPane.getColumnSpan(node) == 3
            );

            rootGridPane.add(newScrollPane, 1, 0, 3, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}