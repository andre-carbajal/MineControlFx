package net.anvian.minecontrolfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 854, 480);
        scene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("MineControl " + Constants.VERSION);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.setMinWidth(854);
        stage.setMinHeight(480);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}