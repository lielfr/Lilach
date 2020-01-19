package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;

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

    @FXML
    private Label orderNumEmpty;

    @FXML
    private Label compEmpty;

    @FXML
    private Label invalidInputOrNum;


    private boolean status = true;

    @FXML
    void cancelComplaint(MouseEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    void clearText(ActionEvent event) {
        complaintBox.setText("");

    }

    @FXML
    void enableOrderNumber(MouseEvent event) {
        status = !status;
        orderNumberLable.setDisable(status);
        orderNumberFild.setDisable(status);
    }

    private boolean isValid (TextField fieldA){
        int countDot = 0;
        for (int i = 0; i<fieldA.getText().length() ; i++){
            char temp = fieldA.getText().charAt(i);
            if(!(temp >= '0') && (temp <= '9')){
                return false;
            }
        }
        return true;
    }

    private boolean inputCheck (){
        boolean retVal = true;

        if (complaintBox.getText().isEmpty())
        {
            compEmpty.setVisible(true);
            retVal = false;
        }
        if (pruchasedCheckBox.isSelected())
        {
            if (orderNumberFild.getText().isEmpty())
            {
                orderNumEmpty.setVisible(true);
                retVal = false;
            }
        }
        if (!(isValid(orderNumberFild)))
        {
            invalidInputOrNum.setVisible(true);
            retVal = false;
        }
        return retVal;
    }

    private void restVisible()
    {
        orderNumEmpty.setVisible(false);
        compEmpty.setVisible(false);
    }

    @FXML
    void sendComplaint(ActionEvent event) {
        restVisible();
        inputCheck();
    }

    @Override
    public void refresh() {

    }
}
