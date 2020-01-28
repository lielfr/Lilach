package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.cshaifasweng.winter.events.CustomerSendEvent;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.SubscriberType;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EditEmployeeController implements Initializable {

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
    private Label passwordLabel;

    @FXML
    private DatePicker employedSincePicker;

    @FXML
    private Label employedLable;

    @FXML
    private Label customerIdLabel;

    String empty = "Can't stay empty";
    String invalid = "Invalid entry";
    boolean editPressed = false;
    private User user;
    private Employee employee;
    Stage stage;

    private void turnOnFields() {
        firsNameField.setDisable(false);
        lastNameField.setDisable(false);
        idNumField.setDisable(false);
        phoneField.setDisable(false);
        emailField.setDisable(false);
        passwordField.setDisable(false);
        employedSincePicker.setDisable(false);
    }

    private void turnLabelsOff() {
        firstNameLabel.setVisible(false);
        lastNameLabel.setVisible(false);
        idNumLabel.setVisible(false);
        phoneNumLabel.setVisible(false);
        emailLabel.setVisible(false);
        passwordLabel.setVisible(false);
        customerIdLabel.setVisible(false);
        employedLable.setVisible(false);
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


        if(!(checkDates())){
            val = false;
        }

        return val;
    }

    private boolean checkDates() {
        boolean val = true;
        LocalDate employedSincePickerDate = employedSincePicker.getValue();


        if (!(isPast(employedSincePickerDate))) {
            employedLable.setVisible(true);
            employedLable.setText(invalid);
            val = false;
        }
        return val;
    }

    private void fillFields(Employee employee){
        firsNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        idNumField.setText(employee.getMisparZehut());
        phoneField.setText(employee.getPhone());
        emailField.setText(employee.getEmail());
        passwordField.setText(employee.getPassword());
        Date input = employee.getEmployedSince();
        LocalDate bDate = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());
        employedSincePicker.setValue(bDate);
    }

    private void updateFields(){
        employee.setFirstName(firsNameField.getText());
        employee.setLastName((lastNameField.getText()));
        employee.setMisparZehut(idNumField.getText());
        employee.setPhone(phoneField.getText());
        employee.setEmail(emailField.getText());
        employee.setPassword(passwordField.getText());
        employee.setEmployedSince(convertToDateViaSqlDate(employedSincePicker.getValue()));
    }

    private Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }


    @FXML
    void edit(ActionEvent event) {
        editPressed = true;
        turnOnFields();
    }

    @FXML
    void exit(ActionEvent event) {
        stage.close();
    }

    @FXML
    void saveChanges(ActionEvent event) {
        if(editPressed){
            turnLabelsOff();
            if(emptyCheck()){
                return;
            }
            if(inputCheck()){
                return;
            }
            updateFields();
            LilachService service = APIAccess.getService();
            service.updateEmployee(employee.getId(),employee).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.code() == 200) {
                        System.out.println("adding the handling success");
                        Platform.runLater(() -> {
                            stage.close();
                        });
                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable throwable) {

                }
            });

        }
        return;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
        customerIdLabel.setText(Long.toString(employee.getId()));
    }

    @Subscribe
    public void handleEvent(CustomerSendEvent event) {
        employee = event.getEmployee();
        fillFields(employee);
        stage = event.getStage();

    }
}
