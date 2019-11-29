package org.cshaifasweng.winter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = loadFXML("primary");

        scene = new Scene(loader.load());
        stage.setScene(scene);
        PrimaryController controller = (PrimaryController)loader.getController();
        stage.setOnHiding(event -> {
            try {
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stage.show();
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void stop() throws Exception {
        SpringServer.stopServer();
    }
}