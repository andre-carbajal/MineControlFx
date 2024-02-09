package net.anvian.minecontrolfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.anvian.minecontrolfx.util.ServerList;
import net.anvian.minecontrolfx.util.os.DirectoryCreator;
import net.anvian.minecontrolfx.util.os.OsChecker;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MainController implements Initializable {
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
            e.printStackTrace();
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


    public void openServer(MouseEvent mouseEvent) {
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
}