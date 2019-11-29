package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {


    private boolean isServerRunning = false;

    @FXML
    private Button primaryButton;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField serverPort;

    private void updateStatus() {
        statusLabel.setText("Server is " + (isServerRunning?"Running": "not running"));
        primaryButton.setText((isServerRunning?"Stop": "Start") + " Server");
    }

    @FXML
    private void toggleServer() throws IOException {
        SpringServer.properties.put("server.port", serverPort.getText());
        if (isServerRunning) {
            SpringServer.stopServer();
            isServerRunning = false;
        } else {
            isServerRunning = true;
            SpringServer.runServer();
        }
        updateStatus();
    }

    @FXML
    private void switchToSecondary() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
