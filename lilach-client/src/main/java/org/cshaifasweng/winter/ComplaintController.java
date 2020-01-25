package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private boolean isValidOrderNum (TextField fieldA){
        for (int i = 0; i<fieldA.getText().length() ; i++){
            char temp = fieldA.getText().charAt(i);
            if(!((temp >= '0') && (temp <= '9'))){
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
            else{
                if (!(isValidOrderNum(orderNumberFild))){
                    invalidInputOrNum.setVisible(true);
                    retVal = false;
                }
            }
        }
        return retVal;
    }

    private void restVisible()
    {
        orderNumEmpty.setVisible(false);
        compEmpty.setVisible(false);
        invalidInputOrNum.setVisible(false);
    }

    private void fillComplaint(Complaint complaint){
        complaint.setDescription(complaintBox.getText());
        complaint.setOrdered(pruchasedCheckBox.isSelected());
        complaint.setEmail(emailCheckBox.isSelected());
        if (pruchasedCheckBox.isSelected()){
            complaint.setOrderNum(orderNumberFild.getText());
        }
        complaint.setOpen(true);


    }

    @FXML
    void sendComplaint(ActionEvent event) {
        restVisible();

        //TODO: Add a popup here.
        if (!inputCheck()) return;

        // TODO: Actually instantiate the complaint (using new and all the fields).
        Complaint complaint;
        complaint = new Complaint();

        LilachService service = APIAccess.getService();
        service.newComplaint(complaint).enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                Complaint received = response.body();

                // TODO: Replace with something meaningful
                Platform.runLater(() -> complaintBox.setText("DONE"));
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void refresh() {

    }
}
