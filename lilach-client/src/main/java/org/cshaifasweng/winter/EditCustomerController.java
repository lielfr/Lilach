package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditCustomerController {

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

    String canNotBeEmpty = "Can't stay empty";
    String invalidVal = "Invalid entry";

    private void turnOnFieldsCustomer(){
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
    private void turnOnFieldsManger(){
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
        subscriptionChoice.setDisable(false);
    }

    @FXML
    void edit(ActionEvent event) {
//        if()
        turnOnFieldsManger();
    }

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void saveChanges(ActionEvent event) {

    }

}
