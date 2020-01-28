package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.cshaifasweng.winter.events.CustomerSendEvent;
import org.cshaifasweng.winter.events.UserEditedEvent;
import org.cshaifasweng.winter.models.Customer;
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
import java.util.*;
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
    private ChoiceBox<SubscriberType> subscriptionChoice;

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
    Stage stage;



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
        subscriptionChoice.setDisable(false);
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

        List<Pair<TextField, Label>> testPairs = new ArrayList<>();
        testPairs.add(new Pair<>(firsNameField, firstNameLabel));
        testPairs.add(new Pair<>(lastNameField, lastNameLabel));
        testPairs.add(new Pair<>(idNumField, idNumLabel));
        testPairs.add(new Pair<>(phoneField, phoneNumLabel));
        testPairs.add(new Pair<>(emailField, emailLabel));
        testPairs.add(new Pair<>(addressField, addressLabel));
        testPairs.add(new Pair<>(creditcardField, creditcardLabel));
        testPairs.add(new Pair<>(cvvField, cvvLabel));

        for (Pair<TextField, Label> pair : testPairs) {
            val = val & Utils.emptyOrNullCheckField(pair.getKey(), pair.getValue(), empty);
        }

        if(!(checkDates())){
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

        if (!(isPast(dateOfBirth))) {
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

    private void fillFields(Customer customer){
        customerIdLabel.setText(Long.toString(customer.getId()));
        firsNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        idNumField.setText(customer.getMisparZehut());
        phoneField.setText(customer.getPhone());
        emailField.setText(customer.getEmail());
        addressField.setText(customer.getAddress());
        creditcardField.setText(Long.toString(customer.getCreditCard()));
        cvvField.setText(Long.toString(customer.getCvv()));
        Date inputBDate = customer.getDateOfBirth();
        dateOfBirthPicker.setValue(Utils.toLocalDate(inputBDate));
        Date inputExpDate = customer.getExpireDate();
        expirationPicker.setValue(Utils.toLocalDate(inputExpDate));
        Date inputEndDate = customer.getExpireDate();
        subscriptionEndPicker.setValue(Utils.toLocalDate(inputEndDate));
        if (customer.getSubscriberType() == null)
            subscriptionChoice.getSelectionModel().select(0);
        else
            subscriptionChoice.setValue(customer.getSubscriberType());
    }

    private void updateFields(){
        customer.setFirstName(firsNameField.getText());
        customer.setLastName((lastNameField.getText()));
        customer.setMisparZehut(idNumField.getText());
        customer.setPhone(phoneField.getText());
        customer.setEmail(emailField.getText());
        if (passwordField.getText() != null && !passwordField.getText().isEmpty())
            customer.setPassword(passwordField.getText());
        customer.setAddress(addressField.getText());
        customer.setCreditCard(Long.parseLong(creditcardField.getText()));
        customer.setCvv(Integer.parseInt(cvvField.getText()));
        customer.setDateOfBirth(convertToDateViaSqlDate(dateOfBirthPicker.getValue()));
        customer.setExpireDate(convertToDateViaSqlDate(expirationPicker.getValue()));
        SubscriberType choice1 = subscriptionChoice.getValue();
        customer.setSubscriberType(choice1);
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
            if(!emptyCheck()){
                System.out.println("EMPTY CHECK FAILED!");
                return;
            }
            if(!inputCheck()){
                System.out.println("INPUT CHECK FAILED!");
                return;
            }
            updateFields();
            LilachService service = APIAccess.getService();
            service.updateCustomer(customer.getId(),customer).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.code() == 200) {
                        System.out.println("adding the handling success");
                        Platform.runLater(() -> {
                           stage.close();
                           EventBus.getDefault().post(new UserEditedEvent());
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

        subscriptionChoice.setConverter(new StringConverter<SubscriberType>() {
            @Override
            public String toString(SubscriberType subscriberType) {
                if (subscriberType == null) return "None";
                return subscriberType.toString();
            }

            @Override
            public SubscriberType fromString(String s) {
                return null;
            }
        });

        List<SubscriberType> values = new ArrayList<>();
        values.add(null);
        values.addAll(Arrays.asList(SubscriberType.values()));

        subscriptionChoice.setItems(FXCollections.observableArrayList(values));
    }

    @Subscribe
    public void handleEvent(CustomerSendEvent event) {
        if (event.getCustomer() == null) return;
        customer = event.getCustomer();

        fillFields(customer);
        stage = event.getStage();

    }
}
