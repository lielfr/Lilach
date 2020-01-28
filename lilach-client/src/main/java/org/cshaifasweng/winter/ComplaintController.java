package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

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

    @FXML
    private ComboBox<Store> storeComboBox;


    private boolean status = true;
    Complaint complaint = new Complaint();
    User currentUser;



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

    private void restVisible() {
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
        complaint.setOpenedBy((Customer)currentUser);
    }
    @FXML
    void sendComplaint(ActionEvent event) throws IOException {
        restVisible();

        //TODO: Add a popup here.
        if (!inputCheck()) return;

        // TODO: Actually instantiate the complaint (using new and all the fields).
        fillComplaint(complaint);
        LilachService service = APIAccess.getService();
        service.newComplaint(complaint).enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
//                Complaint received = response.body();
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        EventBus.getDefault().post(new DashboardSwitchEvent("complaint_list"));
                    });
                }

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
        currentUser = APIAccess.getCurrentUser();
        storeComboBox.setConverter(new StringConverter<Store>() {
            @Override
            public String toString(Store store) {
                return store.getName();
            }

            @Override
            public Store fromString(String s) {
                return null;
            }
        });
        Customer customer = (Customer) currentUser;
        storeComboBox.setItems(FXCollections.observableList(customer.getStores()));
        storeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Store>() {
            @Override
            public void changed(ObservableValue<? extends Store> observableValue, Store store, Store t1) {
                complaint.setStore(customer.getStores().get(storeComboBox.getSelectionModel().getSelectedIndex()));
            }
        });
        storeComboBox.getSelectionModel().select(0);
    }

    @Override
    public void onSwitch() {

    }
}
