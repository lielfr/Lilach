package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;

import java.net.URL;
import java.util.ResourceBundle;

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
    private User user = APIAccess.getCurrentUser();
    private Customer customer = (Customer)user;


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

    private boolean emptyCheck(){
        boolean val =true;
        if(firsNameField.getText().isEmpty()){
            firstNameLabel.setVisible(true);
            firstNameLabel.setText(empty);
            val = false;
        }

        if( lastNameField.getText().isEmpty()){
            lastNameLabel.setVisible(true);
            lastNameLabel.setText(empty);
            val = false;
        }

        if( idNumField.getText().isEmpty()){
            idNumLabel.setVisible(true);
            idNumLabel.setText(empty);
            val = false;
        }
        phoneField.getText().isEmpty()
        if( phoneField.getText().isEmpty()){
            phoneNumLabel.setVisible(true);
            phoneNumLabel.setText(empty);
            val = false;
        }

        if( emailField.getText().isEmpty()){
            emailLabel.setVisible(true);
            emailLabel.setText(empty);
            val = false;
        }

        if( passwordField.getText().isEmpty()){
            passwordLabel.setVisible(true);
            passwordLabel.setText(empty);
            val = false;
        }
        if( addressField.getText().isEmpty()){
            addressLabel.setVisible(true);
            addressLabel.setText(empty);
            val = false;
        }
        if(creditcardField.getText().isEmpty()){
            creditcardLabel.setVisible(true);
            creditcardLabel.setText(empty);
            val = false;
        }
        if(cvvField.getText().isEmpty()){
            cvvLabel.setVisible(true);
            cvvLabel.setText(empty);
            val = false;
        }
        return val;
    }


    @FXML
    void edit(ActionEvent event) {
        if(user instanceof Employee){

        }

        turnOnFieldsManger();
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
}
