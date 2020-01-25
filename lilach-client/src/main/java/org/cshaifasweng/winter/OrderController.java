package org.cshaifasweng.winter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.OrderCreateEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

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
    private Label deliveryAddressLabel;

    @FXML
    private Label recipientMailLabel;

    @FXML
    private TextField setDeliveryAddressField;

    @FXML
    private TextField setRecipientMailField;

    @FXML
    private Label deliveryAddressEmpty;

    @FXML
    private Label recipientMailEmpty;

    @FXML
    private Label invalidEmailAddressLabel;

    @FXML
    private ChoiceBox<String> hourChooseBox;

    @FXML
    private ChoiceBox<String> minuteChooseBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label invalidDate;

    @FXML
    private Label invalidHour;


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

    private boolean radioStatus = false;

    @Subscribe
    public void handleOrderCreateEvent(OrderCreateEvent event) {
        // TODO: Use the event
    }

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

    private void setDisable(boolean status){
        if (setDeliveryAddressField != null)
            setDeliveryAddressField.setDisable(!status);
        if (deliveryAddressLabel != null)
            deliveryAddressLabel.setDisable(!status);
        if (recipientMailLabel != null)
            recipientMailLabel.setDisable(!status);
        if (setRecipientMailField != null)
            setRecipientMailField.setDisable(!status);
    }

    private void setSelected(boolean status){
        if (sendToAnotherAddRadio != null){
        sendToAnotherAddRadio.setSelected(status);
        sendToMyAddRadio.setSelected(!status);
        }
    }

    @FXML
    private void selectMyAddRadio() {
        radioStatus = false;
        setSelected(false);
        setDisable(radioStatus);

    }

    @FXML
    private void selectOtherAddRadio() {
        radioStatus = true;
        setSelected(true);
        setDisable(radioStatus);
    }

    @FXML
    void cancel(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    /**
     * allow change in the personal details.
     * @param event entered event
     */
    @FXML
    void changeDetails(ActionEvent event) {
        firstNameVerField.setDisable(false);
        lastNameVerField.setDisable(false);
        idNumVerField.setDisable(false);
        emailVerField.setDisable(false);
        phoneNumVerField.setDisable(false);
        addressVerField.setDisable(false);
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
        refresh();

    }

    boolean finalTab (){
        return tab5.isSelected();
    }

    boolean firstTab(){
        return tab1.isSelected();
    }

    @FXML
    void next(ActionEvent event) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        // If the current tub is the final tub. send the order and go back to the main screen.
        if(finalTab()){
            EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
        }
        else{
            // move to the next tab and enable it.
            selectionModel.selectNext();
            selectionModel.getSelectedItem().setDisable(false);
        }
        refresh();
    }


    private ObservableList<String> hourList = FXCollections.observableArrayList(""+
                    "00","01","02","03","04","05","06","07","08","09",
                    "10","11","12","13","14","15","16","17","18","19",
                    "20","21","22","23");

    private ObservableList<String> minuteList = FXCollections.observableArrayList("" +
                    "00","01","02","03","04","05","06","07","08","09",
                    "10","11","12","13","14","15","16","17","18","19",
                    "20","21","22","23","24","25","26","27","28","29",
                    "30","31","32","33","34","35","36","37","38","39",
                    "40","41","42","43","44","45","46","47","48","49",
                    "50","51","52","53","54","55","56","57","58","59");

    private boolean isPast(){
        boolean flag = true;
        Calendar now = Calendar.getInstance();
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourChooseBox.getValue()));
        now.set(Calendar.MINUTE, Integer.parseInt((minuteChooseBox.getValue())));

        return (now.getTime().before(new Date()));
    }

    @Override
    public void refresh() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        //setting the default to 'send to my address'.
        selectMyAddRadio();
        hourChooseBox.setItems(hourList);
        hourChooseBox.setValue("00");
        minuteChooseBox.setItems(minuteList);
        minuteChooseBox.setValue("00");

        System.out.println(selectionModel.getSelectedIndex());
        //if(selectionModel.getSelectedIndex() == 0){
        if(firstTab()){
            backButton.setText("Exit");
            cancelButton.setVisible(false);
        }
        else{
            backButton.setText("Back");
            cancelButton.setVisible(true);
        }

        if(selectionModel.getSelectedIndex() == 4){
            nextButton.setText("Send order");
        }
        else{
            nextButton.setText("Next");
        }
        EventBus.getDefault().register(this);
    }
}
