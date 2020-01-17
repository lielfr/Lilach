package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class HandleComplaintController implements Refreshable {


    @FXML
    private Label complatinNum;

    @FXML
    private Label customerName;

    @FXML
    private TextArea answerBox;

    @FXML
    private CheckBox finComp;

    @FXML
    private Label compAmuLable;

    @FXML
    private TextField compAmountFild;

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void clearText(ActionEvent event) {

    }

    @FXML
    void sendFrom(ActionEvent event) {

    }

    @FXML
    void toggleCompensation(MouseEvent event) {

    }

    @Override
    public void refresh() {

    }
}
