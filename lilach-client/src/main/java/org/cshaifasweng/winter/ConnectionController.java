package org.cshaifasweng.winter;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionController implements Initializable {
    @FXML
    private TextField serverIP;

    @FXML
    private TextField serverPort;

    @FXML
    private Button connectButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverIP.setText("127.0.0.1");
        serverPort.setText("8080");

        connectButton.setOnAction(actionEvent -> {
            APIAccess.setAddress(serverIP.getText(), serverPort.getText());
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
