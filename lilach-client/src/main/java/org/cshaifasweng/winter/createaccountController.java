package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.YearMonth;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class createaccountController implements Initializable{

    @FXML
    private Button finishcreate;

    @FXML
    private Label Lname;

    @FXML
    private Label Lid;

    @FXML
    private Label Lemail;

    @FXML
    private Label Lphone;

    @FXML
    private Label Lcredit;

    @FXML
    private Label Lvaild;

    @FXML
    private Label Lcvv;

    @FXML
    private Label Lpassword;

    @FXML
    private PasswordField Tpassword;

    @FXML
    private TextField Tname;

    @FXML
    private TextField Temail;

    @FXML
    private TextField Tid;

    @FXML
    private TextField Tphone;

    @FXML
    private TextField Tcredit;

    @FXML
    private TextField TvaildM;

    @FXML
    private TextField Tcvv;

    @FXML
    private ChoiceBox<String> Cmembership;

    @FXML
    private Label Lmembership;
    @FXML
    private Label Tmessage;
    @FXML
    private Label Lheadline;

    @FXML
    private Label LEMembership;

    @FXML
    private Label LEName;

    @FXML
    private Label LEEmail;

    @FXML
    private Label LEId;

    @FXML
    private Label LEPhone;

    @FXML
    private Label LECredit;

    @FXML
    private Label LEVaild;

    @FXML
    private Label LECvv;

    @FXML
    private Label LEPassword;
    @FXML
    private Label Lseprate;

    @FXML
    private TextField TvaildY;

    @FXML
    void Fcreate(ActionEvent event) {
        Tmessage.setVisible(false);
        LEName.setText("");
        LEEmail.setText("");
        LEId.setText("");
        LEPhone.setText("");
        LECredit.setText("");
        LEVaild.setText("");
        LECvv.setText("");
        LEPassword.setText("");
        Cmembership.setValue("");
        LEMembership.setText("");
        if((MissField()!=0) ){
            Tmessage.setText("Cant create account");
            Tmessage.setVisible(true);
        }
        if(MistakeField()!=0){
            Tmessage.setText("Cant create account");
            Tmessage.setVisible(true);
        }
        else if(MissField()==0 && MistakeField()==0){

            //need to add send details to server.

            //clean all the page only message on the page
            Tname.setVisible(false);
            Lname.setVisible(false);
            Temail.setVisible(false);
            Lemail.setVisible(false);
            Tid.setVisible(false);
            Lid.setVisible(false);
            Tphone.setVisible(false);
            Lphone.setVisible(false);
            Tcredit.setVisible(false);
            Lcredit.setVisible(false);
            TvaildY.setVisible(false);
            Lvaild.setVisible(false);
            TvaildM.setVisible(false);
            Tcvv.setVisible(false);
            Lcvv.setVisible(false);
            Tpassword.setVisible(false);
            Lpassword.setVisible(false);
            Cmembership.setVisible(false);
            Tmessage.setText("Create account completed");
            Tmessage.setVisible(true);
            finishcreate.setVisible(false);
            Lseprate.setVisible(false);
            Lmembership.setVisible(false);
            Lheadline.setVisible(false);

        }
    }

    public int MissField(){
        int count=0;
        if (Tname.getText().isEmpty())
        {
            LEName.setText("Please insert name");
            count++;
        }
        if (Temail.getText().isEmpty())
        {
            LEEmail.setText("Please insert email");
            count++;
        }
        if (Tid.getText().isEmpty())
        {
            LEId.setText("Please insert id");
            count++;
        }
        if (Tphone.getText().isEmpty())
        {
            LEPhone.setText("Please insert phone number");
            count++;
        }
        if (Tcredit.getText().isEmpty())
        {
            LECredit.setText("Please insert credit card number");
            count++;
        }
        if (TvaildM.getText().isEmpty())
        {
            LEVaild.setText("Please insert expiry date of credit card");
            count++;
        }
        if (TvaildY.getText().isEmpty())
        {
            LEVaild.setText("Please insert expiry date of credit card");
            count++;
        }
        if (Tcvv.getText().isEmpty())
        {
            LECvv.setText("Please Insert cvv");
            count++;
        }
        if (Tpassword.getText().isEmpty()) {
            LEPassword.setText("Please Insert password");
            count++;
        }
        if(Cmembership.getSelectionModel().isEmpty()==true){
            LEMembership.setText("Please chose membership length");
            count++;
        }
        return count;
    }
    public int MistakeField(){
        int countMistake=0;
        String check;
        check = Tname.getText();
        if ((Pattern.matches("[a-zA-Z]+",check)==false) && (check.isEmpty()==false))
        {
            LEName.setText("Name is incorrect");
            countMistake++;
        }//finish check Tname

        check=Temail.getText();
        if(check.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")==false  && (check.isEmpty()==false)){
            LEEmail.setText("Email is incorrect");
        }//finsh check email
        check=Tid.getText();
        if ((check.length()!=9) && (check.isEmpty()==false))
        {
            LEId.setText("Id is incorrect");
            countMistake++;
        }
        else if(check.length()==9){
            for(int i=0;i<check.length();i++) {
                if ((check.charAt(i) < '0') || (check.charAt(i) > '9'))
                {
                    LEId.setText("Id is incorrect");
                    countMistake++;
                }
            }
        }//finish check Tid
        check=Tphone.getText();
        if ((Pattern.matches("[a-zA-Z]+",check)==true) && (check.isEmpty()==false))
        {
            LEPhone.setText("Phone number is incorrect");
            countMistake++;
        }//finish check Tphone (maybe check length of tphone???
        check=Tcredit.getText();
        if ((check.length()!=16) && (check.isEmpty()==false))
        {

            LECredit.setText("Credit card  is incorrect");
            countMistake++;
        }
        else if(check.length()==16) {
            for(int i=0;i<check.length();i++)
            {
                if ((check.charAt(i)<'0') || (check.charAt(i)>'9')){
                    LECredit.setText("Credit card is incorrect");
                    countMistake++;
                }

            }
        }//finish check Tcredit

        check=TvaildM.getText();
        if((check.length()>2)&&(check.isEmpty()==false))
        {
            LEVaild.setText("Expiry date of credit card is incorrect");
        }
        else if(check.length()==1){
            if (Integer.parseInt(check)>0 &&Integer.parseInt(check)<=9){
                switch ( Integer.parseInt(check)){
                    case 1:
                        check="01";
                        break;
                    case 2:
                        check="02";
                        break;
                    case 3:
                        check="03";
                        break;
                    case 4:
                        check="04";
                        break;
                    case 5:
                        check="05";
                        break;
                    case 6:
                        check="06";
                        break;
                    case 7:
                        check="07";
                        break;
                    case 8:
                        check="08";
                        break;
                    case 9:
                        check="09";
                        break;
                }
                TvaildM.setText(check);
                if ((Integer.parseInt(check))<(YearMonth.now().getMonthValue()) ) {
                    LEVaild.setText("Expiry date of credit card is incorrect");
                    countMistake++;
                }
            }
            else{
                LEVaild.setText("Expiry date of credit card is incorrect");
                countMistake++;
            }
        }
        else if(check.length()==2) {
            for (int i = 0; i < check.length(); i++) {
                if ((Integer.parseInt(check))<(YearMonth.now().getMonthValue()) ) {
                    LEVaild.setText("Expiry date of credit card is incorrect");
                    countMistake++;
                }
            }
        }
        check=TvaildY.getText();
        if((check.length()!=4)&&(check.isEmpty()==false))
        {
            LEVaild.setText("Expiry date of credit card is incorrect");
        }
        else if(check.length()==4) {
            for (int i = 0; i < check.length(); i++) {
                if ((Integer.parseInt(check))<(YearMonth.now().getYear()) ) {
                    LEVaild.setText("Expiry date of credit card is incorrect");
                    countMistake++;
                }
            }
        }
        check=Tcvv.getText();
        if ((check.length()!=3) && (check.isEmpty()==false))
        {
            LECvv.setText("Cvv is incorrect");
            countMistake++;
        }
        else if(check.length()==3){
            for(int i=0;i<check.length();i++)
            {
                if ((check.charAt(i)<'0') || (check.charAt(i)>'9')){
                    LECvv.setText("Cvv is incorrect");
                    countMistake++;
                }

            }
        }//finish check Tcvv
        return countMistake;

    }

    private ObservableList<String> dbTypeList = FXCollections.observableArrayList("None","Month","Year");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Cmembership.setItems(dbTypeList);
    }
}
