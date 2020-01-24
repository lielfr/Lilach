package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class OrderController {

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button nextButton;

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

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void changeDetails(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }

    @FXML
    void next(ActionEvent event) {

    }

}
