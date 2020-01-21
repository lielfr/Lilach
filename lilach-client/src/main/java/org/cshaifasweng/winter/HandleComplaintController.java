package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;

import javax.swing.*;

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
    private Label empty;

    @FXML
    private Label compensationEmpty;

    @FXML
    private Label invalidInput;

    @FXML
    private TextArea complaintBox;

    @FXML
    private Label complaintOpDaLabel;

    @FXML
    private Label complaintOpTiLabel;

    @FXML
    private CheckBox purchaseMadeCheckBox;


    private boolean status = true;

    @FXML
    void cancel(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    void clearText(ActionEvent event) {
        answerBox.setText("");
    }
    private boolean isValid (TextField fieldA){
        int countDot = 0;
        for (int i = 0; i<fieldA.getText().length() ; i++){
            char temp = fieldA.getText().charAt(i);
            if (temp == '.') {
                countDot++;
                if (countDot > 1){
                    return false;
                }
            }
            if(!((temp>='0' && temp<='9') || (temp == '.'))){
                return false;
            }
        }
        return true;
    }

    private boolean inputCheck (){
        boolean retVal = true;
        if (answerBox.getText().isEmpty())
        {
            empty.setVisible(true);
            retVal = false;
        }
        if (finComp.isSelected())
        {
            if (compAmountFild.getText().isEmpty())
            {
                compensationEmpty.setVisible(true);
                retVal = false;
            }
        }
        if (!(isValid(compAmountFild)))
        {
            invalidInput.setVisible(true);
            retVal = false;
        }
        return retVal;
    }

    @FXML
    void sendFrom(ActionEvent event) {
       restVisible();
       inputCheck();

    }

    private void restVisible()
    {
        empty.setVisible(false);
        compensationEmpty.setVisible(false);
        invalidInput.setVisible(false);
    }

    @FXML
    void toggleCompensation(MouseEvent event) {
        status = !status;
        compAmuLable.setDisable(status);
        compAmountFild.setDisable(status);
    }

    @Override
    public void refresh() {

    }
}
