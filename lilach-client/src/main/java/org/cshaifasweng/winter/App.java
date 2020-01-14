package org.cshaifasweng.winter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(LayoutManager.getInstance().getFXML("connection"));
        stage.setScene(scene);
        // We have to set it manually in order to make it actually close on Mac
        stage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(LayoutManager.getInstance().getFXML(fxml));
        scene.getWindow().sizeToScene();
    }

    public static void main(String[] args) {
        launch();
    }

}