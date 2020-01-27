package org.cshaifasweng.winter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(LayoutManager.getInstance().getFXML("connection").getKey());
        scene.getStylesheets().add(String.valueOf(App.class.getResource("strikethrough.css")));
        scene.getStylesheets().add(String.valueOf(App.class.getResource("lilach.css")));
        stage.setScene(scene);
        // We have to set it manually in order to make it actually close on Mac
        stage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (APIAccess.getCurrentUser() != null) {
                LilachService service = APIAccess.getService();
                try {
                    service.logout().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(LayoutManager.getInstance().getFXML(fxml).getKey());
        scene.getWindow().sizeToScene();
    }

    public static void main(String[] args) {
        launch();
    }

}