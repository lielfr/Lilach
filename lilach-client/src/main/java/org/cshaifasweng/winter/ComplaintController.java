package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ComplaintController implements Refreshable {

    @FXML
    private Label complaintLable;

    @FXML
    private TextArea complaintBox;

    @FXML
    private CheckBox emailCheckBox;

    @FXML
    private Button sendBox;

    @FXML
    private Button cancleBox;

    @FXML
    private CheckBox pruchasedCheckBox;

    @FXML
    private Button clearcomplaintbox;

    @FXML
    private Label orderNumberLable;

    @FXML
    private TextField orderNumberFild;


    private boolean status = true;

    @FXML
    void cancelComplaint(MouseEvent event) {

    }

    @FXML
    void enableOrderNumber(MouseEvent event) {
        status = !status;
        orderNumberLable.setDisable(status);
        orderNumberFild.setDisable(status);
    }

    @Override
    public void refresh() {

    }
}
