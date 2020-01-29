package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.LoginChangeEvent;
import org.cshaifasweng.winter.events.TokenSetEvent;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.models.SubscriberType;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;



public class CreateAccountController implements Refreshable{

    @FXML
    private Button finishcreate;

    @FXML
    private Label LFname;

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
    private TextField TFname;

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
    private Label Lheadline;

    @FXML
    private Label LEMembership;

    @FXML
    private Label LEFName;

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
    private Label Lseprate1;

    @FXML
    private Label Lseprate2;

    @FXML
    private TextField TvaildY;

    @FXML
    private Label Tmessage;

    @FXML
    private Label LLname;

    @FXML
    private TextField TLname;

    @FXML
    private Label Ldateofbirth;

    @FXML
    private TextField Tbirthd;

    @FXML
    private TextField Tbirthm;

    @FXML
    private TextField Tbirthy;

    @FXML
    private Label LEFName1;

    @FXML
    private Label LELName;

    @FXML
    private Label LEDateofbirth;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressField;

    @FXML
    private Label addressEmpty;

    @FXML
    private ChoiceBox<Store> storeChoiceBox;

    private Store selectedStore;

    @FXML
    void Fcreate(ActionEvent event) {

        Tmessage.setVisible(false);
        LEFName.setText("");
        LELName.setText("");
        LEDateofbirth.setText("");
        LEEmail.setText("");
        LEId.setText("");
        LEPhone.setText("");
        LECredit.setText("");
        LEVaild.setText("");
        LECvv.setText("");
        LEPassword.setText("");
        addressEmpty.setText("");



        LEMembership.setText("");
        if((MissField()!=0) ){
            Cmembership.setValue("");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Missing field(s)");
            alert.show();
        }
        if(MistakeField()!=0){
            Cmembership.setValue("");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid field(s)");
            alert.show();
        }
        else if(MissField()==0 && MistakeField()==0){
            int choice = Cmembership.getSelectionModel().getSelectedIndex();
            SubscriberType choice1;
            if(choice == 0){
                choice1 = null;
            }
            if(choice == 1){
                choice1= SubscriberType.MONTHLY;
            }
            else
                choice1= SubscriberType.YEARLY;
            Cmembership.setValue("");

            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.MONTH,Integer.parseInt(TvaildM.getText()));
            calendar.set(calendar.YEAR,Integer.parseInt(TvaildY.getText()));
          // calendar.set(Integer.parseInt(TvaildY.getText()),Integer.parseInt(TvaildY.getText()));
            Date date = calendar.getTime();

            Calendar calendarB = Calendar.getInstance();
           // calendarB.set(calendar.MONTH,Integer.parseInt(Tbirthm.getText()));
          //  calendarB.set(calendar.YEAR,Integer.parseInt(Tbirthy.getText()));
            calendarB.set(Integer.parseInt(Tbirthd.getText()), Integer.parseInt(Tbirthm.getText()),Integer.parseInt(Tbirthy.getText()));
            Date datebirth = calendarB.getTime();
            /* *************************************************************************************************************************************************/
            Customer newCustomer = new Customer(Tid.getText(), Temail.getText(),Tpassword.getText(),TFname.getText(),TLname.getText(),
                    Tphone.getText(),Long.valueOf(Tcredit.getText()),date,Integer.parseInt(Tcvv.getText()),datebirth);
            /* *************************************************************************************************************************************************/
            newCustomer.setAddress(addressField.getText());
            newCustomer.setSubscriberType(choice1);
            LilachService service = APIAccess.getService();
            service.newCustomer(newCustomer, selectedStore.getId()).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.code() == 200) {

                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully.");
                            alert.show();
                            Customer createdUser = (Customer) response.body();
                            System.out.println("CREATED USER: " + createdUser.getId());
                            APIAccess.setCurrentUser(createdUser);
                            service.login(newCustomer.getEmail(), newCustomer.getPassword()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() != 200) return;
                                    Platform.runLater(() -> {
                                        String token = Objects.requireNonNull(response.headers().get("Authorization"))
                                                .replace("Bearer ", "");
                                        EventBus.getDefault().post(new TokenSetEvent(token));
                                        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
                                        EventBus.getDefault().post(new LoginChangeEvent());
                                    });

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable throwable) {

                                }
                            });

                        });

                    } else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists");
                            alert.show();
                        });

                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {

                }
            });
        }
    }

    public int MissField(){
        int count=0;
        if (TFname.getText().isEmpty())
        {
            LEFName.setText("Please insert first name");
            count++;
        }
        if (TLname.getText().isEmpty())
        {
            LELName.setText("Please insert last name");
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
        if (Tbirthd.getText().isEmpty())
        {
            LEDateofbirth.setText("Please insert date of birth");
            count++;
        }
        if (Tbirthm.getText().isEmpty())
        {
            LEDateofbirth.setText("Please insert date of birth");
            count++;
        }
        if (Tbirthy.getText().isEmpty())
        {
            LEDateofbirth.setText("Please insert date of birth");
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
        if(addressField.getText().isEmpty() == true){
            addressEmpty.setText("Please insert your address");
            count++;
        }
        return count;
    }
    public int MistakeField() {
        int countMistake = 0;
        String check;
        check = TFname.getText();
        if ((Pattern.matches("[a-zA-Z]+", check) == false) && (check.isEmpty() == false)) {
            LEFName.setText("First name is incorrect");
            countMistake++;
        }//finish check first name
        check = TLname.getText();
        if ((Pattern.matches("[a-zA-Z]+", check) == false) && (check.isEmpty() == false)) {
            LELName.setText("Last name is incorrect");
            countMistake++;
        }//finish check last name
        check = Temail.getText();
        if (check.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])") == false && (check.isEmpty() == false)) {
            LEEmail.setText("Email is incorrect");
        }//finsh check email
        check = Tid.getText();
        if ((check.length() != 9) && (check.isEmpty() == false)) {
            LEId.setText("Id is incorrect");
            countMistake++;
        } else if (check.length() == 9) {
            for (int i = 0; i < check.length(); i++) {
                if ((check.charAt(i) < '0') || (check.charAt(i) > '9')) {
                    LEId.setText("Id is incorrect");
                    countMistake++;
                }
            }
        }//finish check Tid
        check = Tphone.getText();
        if ((Pattern.matches("^((\\+|00)?972\\-?|0)(([23489]|[57]\\d)\\-?\\d{7})$", check) == false) && (check.isEmpty() == false)) {
            LEPhone.setText("Phone number is incorrect");
            countMistake++;
        }//finish check Tphone (maybe check length of tphone???
        check = Tcredit.getText();
        if ((check.length() != 16) && (check.isEmpty() == false)) {

            LECredit.setText("Credit card  is incorrect");
            countMistake++;
        } else if (check.length() == 16) {
            for (int i = 0; i < check.length(); i++) {
                if ((check.charAt(i) < '0') || (check.charAt(i) > '9')) {
                    LECredit.setText("Credit card is incorrect");
                    countMistake++;
                }

            }
        }//finish check Tcredit

        check = Tbirthd.getText();
        if ((check.length() > 2) && (check.isEmpty() == false)) {
            LEDateofbirth.setText("Date of birth is incorrect");
        } else if (check.length() == 1) {
            if (Integer.parseInt(check) > 0 && Integer.parseInt(check) <= 9) {
                switch (Integer.parseInt(check)) {
                    case 1:
                        check = "01";
                        break;
                    case 2:
                        check = "02";
                        break;
                    case 3:
                        check = "03";
                        break;
                    case 4:
                        check = "04";
                        break;
                    case 5:
                        check = "05";
                        break;
                    case 6:
                        check = "06";
                        break;
                    case 7:
                        check = "07";
                        break;
                    case 8:
                        check = "08";
                        break;
                    case 9:
                        check = "09";
                        break;
                }
                Tbirthd.setText(check);
            }
        } else if (check.length() == 2) {
            if (Integer.parseInt(check) > 31) {
                LEDateofbirth.setText("Date of birth is incorrect");
                countMistake++;
            }

        }
            check = Tbirthm.getText();
            if ((check.length() > 2) && (check.isEmpty() == false)) {
                LEDateofbirth.setText("Date of birth is incorrect");
            } else if (check.length() == 1) {
                if (Integer.parseInt(check) > 0 && Integer.parseInt(check) <= 9) {
                    switch (Integer.parseInt(check)) {
                        case 1:
                            check = "01";
                            break;
                        case 2:
                            check = "02";
                            break;
                        case 3:
                            check = "03";
                            break;
                        case 4:
                            check = "04";
                            break;
                        case 5:
                            check = "05";
                            break;
                        case 6:
                            check = "06";
                            break;
                        case 7:
                            check = "07";
                            break;
                        case 8:
                            check = "08";
                            break;
                        case 9:
                            check = "09";
                            break;
                    }
                    Tbirthm.setText(check);

                }
            } else if (check.length() == 2) {

                    if ((Integer.parseInt(check)) >12) {
                        LEDateofbirth.setText("Date of birth is incorrect");
                        countMistake++;
                    }
            }
            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            check = Tbirthy.getText();
            String checkM=Tbirthm.getText();
            String checkD=Tbirthd.getText();
            if ((check.length() != 4) && (check.isEmpty() == false)) {
                LEDateofbirth.setText("Date of birth is incorrect");
            } else if (check.length() == 4) {
           //     for (int i = 0; i < check.length(); i++) {
                    if ((Integer.parseInt(check)) > (YearMonth.now().getYear())) {
                        LEDateofbirth.setText("Date of birth is incorrect");
                        countMistake++;
                    }
                    else if ((Integer.parseInt(check)) == (YearMonth.now().getYear())) {
                        if (((Integer.parseInt(checkM)) > (YearMonth.now().getMonthValue()))){
                            LEDateofbirth.setText("Date of birth is incorrect");
                            countMistake++;
                        }
                        else if ((Integer.parseInt(checkM)) == (YearMonth.now().getMonthValue())){
                            if (((Integer.parseInt(checkD)) > (calendar.get(Calendar.DAY_OF_MONTH)))){
                                LEDateofbirth.setText("Date of birth is incorrect");
                                countMistake++;
                            }
                        }

                    }
               // }
            }

            check = TvaildM.getText();
            if ((check.length() > 2) && (check.isEmpty() == false)) {
                LEVaild.setText("Expiry date of credit card is incorrect");
            } else if (check.length() == 1) {
                if (Integer.parseInt(check) > 0 && Integer.parseInt(check) <= 9) {
                    switch (Integer.parseInt(check)) {
                        case 1:
                            check = "01";
                            break;
                        case 2:
                            check = "02";
                            break;
                        case 3:
                            check = "03";
                            break;
                        case 4:
                            check = "04";
                            break;
                        case 5:
                            check = "05";
                            break;
                        case 6:
                            check = "06";
                            break;
                        case 7:
                            check = "07";
                            break;
                        case 8:
                            check = "08";
                            break;
                        case 9:
                            check = "09";
                            break;
                    }
                    TvaildM.setText(check);
                    if ((Integer.parseInt(check)) < (YearMonth.now().getMonthValue())) {
                        LEVaild.setText("Expiry date of credit card is incorrect");
                        countMistake++;
                    }
                } else {
                    LEVaild.setText("Expiry date of credit card is incorrect");
                    countMistake++;
                }
            } else if (check.length() == 2) {
                for (int i = 0; i < check.length(); i++) {
                    if ((Integer.parseInt(check)) < (YearMonth.now().getMonthValue())) {
                        LEVaild.setText("Expiry date of credit card is incorrect");
                        countMistake++;
                    }
                }
            }
            check = TvaildY.getText();
            if ((check.length() != 4) && (check.isEmpty() == false)) {
                LEVaild.setText("Expiry date of credit card is incorrect");
                countMistake++;
            } else if (check.length() == 4) {
                for (int i = 0; i < check.length(); i++) {
                    if ((Integer.parseInt(check)) < (YearMonth.now().getYear())) {
                        LEVaild.setText("Expiry date of credit card is incorrect");
                        countMistake++;
                    }
                }
            }
            check = Tcvv.getText();
            if ((check.length() != 3) && (check.isEmpty() == false)) {
                LECvv.setText("CVV is incorrect");
                countMistake++;
            } else if (check.length() == 3) {
                for (int i = 0; i < check.length(); i++) {
                    if ((check.charAt(i) < '0') || (check.charAt(i) > '9')) {
                        LECvv.setText("CVV is incorrect");
                        countMistake++;
                    }

                }
            }//finish check Tcvv
            return countMistake;

        }

    private ObservableList<String> dbTypeList =
            FXCollections.observableArrayList(
                    "None",
                    "Monthly (20 NIS per month, 25% discount on each order)",
                    "Yearly (200 NIS per year, 35% discount on each order)"
            );

    @Override
    public void refresh() {
        Cmembership.setItems(dbTypeList);
        Cmembership.setValue("None");

        storeChoiceBox.setConverter(new StringConverter<Store>() {
            @Override
            public String toString(Store store) {
                return store.getName();
            }

            @Override
            public Store fromString(String s) {
                return null;
            }
        });
        LilachService service = APIAccess.getService();
        service.getAllStores().enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.code() != 200) return;
                Platform.runLater(() -> {
                    storeChoiceBox.setItems(FXCollections.observableArrayList(response.body()));
                    storeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, store, t1) -> {
                        selectedStore = storeChoiceBox.getValue();
                    });
                    storeChoiceBox.getSelectionModel().select(0);
                });

            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onSwitch() {

    }
}
