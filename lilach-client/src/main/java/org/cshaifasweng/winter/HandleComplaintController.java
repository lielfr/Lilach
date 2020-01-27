package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.ComplaintHandleEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;

public class HandleComplaintController implements Refreshable {


    @FXML
    private Label complaintNum;

    @FXML
    private Label customerName;

    @FXML
    private TextArea answerBox;

    @FXML
    private CheckBox finComp;

    @FXML
    private Label compAmuLabel;

    @FXML
    private TextField compAmountField;

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
    private Complaint openedComplaint;
    @FXML
    void cancel(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    void clearText(ActionEvent event) {
        answerBox.setText("");
    }

    @Subscribe
    public void handleEvent(ComplaintHandleEvent event) {
        openedComplaint = event.getComplaint();
        complaintNum.setText(Long.toString(event.getComplaint().getId()));
        String fullName;
        String firstName;
        firstName = event.getComplaint().getOpenedBy().getFirstName();
        String lastName;
        lastName = event.getComplaint().getOpenedBy().getLastName();
        fullName = firstName + lastName;
        customerName.setText(fullName);
        purchaseMadeCheckBox.setSelected(event.getComplaint().isOrdered());
        if(purchaseMadeCheckBox.isSelected()){
            complaintOpDaLabel.setText(event.getComplaint().getOrderNum());
        }else{
            complaintOpDaLabel.setText("");
        }
        complaintOpTiLabel.setText(event.getComplaint().getComplaintOpen().toString());
        complaintBox.setText(event.getComplaint().getDescription());
    }

    private void fillComplaint(){
        openedComplaint.setAnswer(answerBox.getText());
        if(finComp.isSelected()){
            openedComplaint.setCompensation(Double.valueOf(compAmountField.getText()));
        }
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
            if (compAmountField.getText().isEmpty())
            {
                compensationEmpty.setVisible(true);
                retVal = false;
            }
        }
        if (!(isValid(compAmountField)))
        {
            invalidInput.setVisible(true);
            retVal = false;
        }
        return retVal;
    }

    @FXML
    void sendFrom(ActionEvent event) {
       restVisible();
       if(!(inputCheck())){
           return;
       }
       fillComplaint();

        LilachService service = APIAccess.getService();
        service.handleComplaint(openedComplaint.getId(),openedComplaint).enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.code() == 200) {
                    System.out.println("adding the handling success");
                    Platform.runLater(() -> {
                        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
                    });
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable throwable) {

            }
        });
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
        compAmuLabel.setDisable(status);
        compAmountField.setDisable(status);
    }

    @Override
    public void refresh() {

    }
}
