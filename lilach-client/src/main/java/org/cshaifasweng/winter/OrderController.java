package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;

public class OrderController implements Refreshable{

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button nextButton;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab1;

    @FXML
    private TableView<?> itemTable;

    @FXML
    private Tab tab2;

    @FXML
    private TextField firstNameVerField;

    @FXML
    private TextField lastNameVerField;

    @FXML
    private TextField idNumVerField;

    @FXML
    private TextField emailVerField;

    @FXML
    private TextField phoneNumVerField;

    @FXML
    private TextField addressVerField;

    @FXML
    private Label firstNameVerEmptyLabel;

    @FXML
    private Label lastNameVerEmptyLabel;

    @FXML
    private Label idVerEmptyLabel;

    @FXML
    private Label emailVerEmptyLabel;

    @FXML
    private Label phoneVerEmptyLabel;

    @FXML
    private Label addressVerEmptyLabel;

    @FXML
    private Label invalidFirstNameVerLabel;

    @FXML
    private Label invalidLastNameVerLabel;

    @FXML
    private Label invalidIdVerLabel;

    @FXML
    private Label invalidEmailVerLabel;

    @FXML
    private Label invalidPhoneVerLabel;

    @FXML
    private Label invalidAddressVerLabel;

    @FXML
    private Button changeDetailsButton;

    @FXML
    private Tab tab3;

    @FXML
    private TextArea greetingEntryTextArea;

    @FXML
    private Tab tab4;

    @FXML
    private RadioButton sendToMyAddRadio;

    @FXML
    private RadioButton sendToAnotherAddRadio;

    @FXML
    private TextField deliveryAddressField;

    @FXML
    private TextField recipientMailField;

    @FXML
    private Label deliveryAddressEmpty;

    @FXML
    private Label recipientMailEmpty;

    @FXML
    private Label invalidEmailAddressLabel;

    @FXML
    private Tab tab5;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField idNumField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private TextArea greetingTextArea;

    private void restVisibleTab2()
    {
        //empty labels
        firstNameVerEmptyLabel.setVisible(false);
        lastNameVerEmptyLabel.setVisible(false);
        idVerEmptyLabel.setVisible(false);
        emailVerEmptyLabel.setVisible(false);
        phoneVerEmptyLabel.setVisible(false);
        addressVerEmptyLabel.setVisible(false);

        //invalid values labels
        invalidFirstNameVerLabel.setVisible(false);
        invalidLastNameVerLabel.setVisible(false);
        invalidIdVerLabel.setVisible(false);
        invalidEmailVerLabel.setVisible(false);
        invalidPhoneVerLabel.setVisible(false);
        invalidAddressVerLabel.setVisible(false);
    }

    private void resetVisibleTab4(){
        deliveryAddressEmpty.setVisible(false);
        recipientMailEmpty.setVisible(false);
        invalidEmailAddressLabel.setVisible(false);
    }

    @FXML
    void cancel(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    void changeDetails(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        if(selectionModel.getSelectedIndex()==0){
            EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
        }
        else{
            selectionModel.selectPrevious();
        }

    }

    @FXML
    void next(ActionEvent event) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        if(tab5.isSelected()){
            nextButton.setText("Send order");
            EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
        }
        else{
            nextButton.setText("Next");
            selectionModel.selectNext();
        }
    }

    @Override
    public void refresh() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        System.out.println(selectionModel.toString());
        if(selectionModel.getSelectedIndex()==0){
            backButton.setText("Exit");
            cancelButton.setVisible(false);
        }
        else{
            backButton.setText("Back");
            selectionModel.selectPrevious();
            cancelButton.setVisible(true);
        }
    }
}
