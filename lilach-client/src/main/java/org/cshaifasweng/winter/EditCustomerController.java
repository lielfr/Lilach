package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.cshaifasweng.winter.events.ComplaintHandleEvent;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EditCustomerController implements Initializable {

    @FXML
    private Button exitButton;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField firsNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField idNumField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField creditcardField;

    @FXML
    private TextField cvvField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private DatePicker expirationPicker;

    @FXML
    private DatePicker subscriptionEndPicker;

    @FXML
    private ChoiceBox<?> subscriptionChoice;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label idNumLabel;

    @FXML
    private Label phoneNumLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label creditcardLabel;

    @FXML
    private Label cvvLabel;

    @FXML
    private Label expirationLabel;

    @FXML
    private Label subscriptionEndLabel;

    @FXML
    private Label customerIdLabel;

    String empty = "Can't stay empty";
    String invalid = "Invalid entry";
    boolean editPressed = false;
    private User user;
    private Customer customer;


    private void turnOnFields() {
        firsNameField.setDisable(false);
        lastNameField.setDisable(false);
        idNumField.setDisable(false);
        phoneField.setDisable(false);
        emailField.setDisable(false);
        passwordField.setDisable(false);
        addressField.setDisable(false);
        creditcardField.setDisable(false);
        cvvField.setDisable(false);
        dateOfBirthPicker.setDisable(false);
        expirationPicker.setDisable(false);
        subscriptionEndPicker.setDisable(false);
    }

    private void turnLabelsOff() {
        firstNameLabel.setVisible(false);
        lastNameLabel.setVisible(false);
        idNumLabel.setVisible(false);
        phoneNumLabel.setVisible(false);
        emailLabel.setVisible(false);
        dateOfBirthLabel.setVisible(false);
        passwordLabel.setVisible(false);
        addressLabel.setVisible(false);
        creditcardLabel.setVisible(false);
        cvvLabel.setVisible(false);
        expirationLabel.setVisible(false);
        subscriptionEndLabel.setVisible(false);
        customerIdLabel.setVisible(false);
    }

//    private void turnOnFieldsManger(){
//        firsNameField.setDisable(false);
//        lastNameField.setDisable(false);
//        idNumField.setDisable(false);
//        phoneField.setDisable(false);
//        emailField.setDisable(false);
//        passwordField.setDisable(false);
//        addressField.setDisable(false);
//        creditcardField.setDisable(false);
//        cvvField.setDisable(false);
//        dateOfBirthPicker.setDisable(false);
//        expirationPicker.setDisable(false);
//        subscriptionEndPicker.setDisable(false);
//        subscriptionChoice.setDisable(false);
//    }

    private boolean emptyCheck() {
        boolean val = true;

        if (firsNameField.getText().isEmpty()) {
            firstNameLabel.setVisible(true);
            firstNameLabel.setText(empty);
            val = false;
        }

        if (lastNameField.getText().isEmpty()) {
            lastNameLabel.setVisible(true);
            lastNameLabel.setText(empty);
            val = false;
        }

        if (idNumField.getText().isEmpty()) {
            idNumLabel.setVisible(true);
            idNumLabel.setText(empty);
            val = false;
        }

        if (phoneField.getText().isEmpty()) {
            phoneNumLabel.setVisible(true);
            phoneNumLabel.setText(empty);
            val = false;
        }

        if (emailField.getText().isEmpty()) {
            emailLabel.setVisible(true);
            emailLabel.setText(empty);
            val = false;
        }

        if (passwordField.getText().isEmpty()) {
            passwordLabel.setVisible(true);
            passwordLabel.setText(empty);
            val = false;
        }
        if (addressField.getText().isEmpty()) {
            addressLabel.setVisible(true);
            addressLabel.setText(empty);
            val = false;
        }
        if (creditcardField.getText().isEmpty()) {
            creditcardLabel.setVisible(true);
            creditcardLabel.setText(empty);
            val = false;
        }
        if (cvvField.getText().isEmpty()) {
            cvvLabel.setVisible(true);
            cvvLabel.setText(empty);
            val = false;
        }
        return val;
    }

    private boolean isPast(LocalDate localDate) {
        boolean flag = true;
//        Calendar now = Calendar.getInstance();
//        LocalDate localDate = datePicker.getValue();
//        boolean a = localDate.isBefore(LocalDate.now());
//        LocalTime localTime = LocalTime.of(Integer.parseInt(hourChooseBox.getValue()), Integer.parseInt(minuteChooseBox.getValue()), 0);
//
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return localDate.isBefore(LocalDate.now());

      /*  Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourChooseBox.getValue()));
        now.set(Calendar.MINUTE, Integer.parseInt((minuteChooseBox.getValue())));

        return (now.getTime().before(new Date()));*/
    }

    private boolean inputCheck() {
        boolean val = true;

        String check;
        check = firsNameField.getText();
        if ((Pattern.matches("[a-zA-Z]+", check) == false) && (check.isEmpty() == false)) {
            firstNameLabel.setVisible(true);
            firstNameLabel.setText(invalid);
            val = false;
        }
        check = lastNameField.getText();
        if ((Pattern.matches("[a-zA-Z]+", check) == false) && (check.isEmpty() == false)) {
            lastNameLabel.setVisible(true);
            lastNameLabel.setText(invalid);
            val = false;
        }
        check = idNumField.getText();
        if ((check.length() != 9) && (check.isEmpty() == false)) {
            idNumLabel.setVisible(true);
            idNumLabel.setText(invalid);
            val = false;

        }
        check = emailField.getText();
        if (check.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-" +
                "\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:" +
                "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\" +
                "x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])") == false &&
                (check.isEmpty() == false)) {
            emailLabel.setVisible(true);
            emailLabel.setText(invalid);
            val = false;
        }
        check = phoneField.getText();
        if ((Pattern.matches("^((\\+|00)?972\\-?|0)(([23489]|[57]\\d)\\-?\\d{7})$", check) == false) &&
                (check.isEmpty() == false)) {
            phoneNumLabel.setVisible(true);
            phoneNumLabel.setText(invalid);
            val = false;
        }

        check = creditcardField.getText();
        if ((check.length() != 16) && (check.isEmpty() == false)) {
            creditcardLabel.setVisible(true);
            creditcardLabel.setText(invalid);
            val = false;
        } else {
            if (check.length() == 16) {
                for (int i = 0; i < check.length(); i++) {
                    if ((check.charAt(i) < '0') || (check.charAt(i) > '9')) {
                        creditcardLabel.setVisible(true);
                        creditcardLabel.setText(invalid);
                        val = false;
                    }
                }
            }//finish
        }

        if(!(checkDates())){
            val = false;
        }

        check = cvvField.getText();
        if ((check.length() != 3) && (check.isEmpty() == false)) {
            cvvLabel.setVisible(true);
            cvvLabel.setText(invalid);
            val = false;
        } else if (check.length() == 3) {
            for (int i = 0; i < check.length(); i++) {
                if ((check.charAt(i) < '0') || (check.charAt(i) > '9')) {
                    cvvLabel.setVisible(true);
                    cvvLabel.setText(invalid);
                    val = false;
                }
            }
        }//finish check cvv

        return val;
    }

    private boolean checkDates() {
        boolean val = true;
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        LocalDate expDate = expirationPicker.getValue();

        if (!(isPast(expDate))) {
            dateOfBirthLabel.setVisible(true);
            dateOfBirthLabel.setText(invalid);
            val = false;
        }

        if (isPast(expDate)) {
            expirationLabel.setVisible(true);
            expirationLabel.setText(invalid);
            val = false;
        }
        return val;
    }

    private void updateFields(){

    }


    @FXML
    void edit(ActionEvent event) {
        editPressed = true;
        turnOnFields();
    }

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void saveChanges(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Subscribe
    public void handleEvent(CustomerSendEvent event) {
        customer = event.getCustomer;
    }
}
