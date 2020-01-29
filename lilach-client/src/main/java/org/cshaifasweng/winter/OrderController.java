package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import org.cshaifasweng.winter.events.BackToCatalogEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.OrderCreateEvent;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

public class OrderController implements Refreshable {

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
    private TableView<Item> itemTable;

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
    private RadioButton sendToStoreAddRadio;

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

    @FXML
    private TextField deliveryAddressField1;

    @FXML
    private TextField recipientMailField1;

    @FXML
    private TextField dateAndTimeShowField;

    @FXML
    private TextField totalPriceShowField;


    private boolean radioStatus = false;
    private String output;

    Order currentOrder = new Order();
    User currentUser = APIAccess.getCurrentUser();

    @Subscribe
    public void handleOrderCreateEvent(OrderCreateEvent event) {
        currentOrder.setItems(new ArrayList<>(event.getCart()));
        currentOrder.setQuantities(event.getQuantities());
        populateTable();

        double price = 0.0;
        for (Item item : currentOrder.getItems()) {
            double itemPrice = item.getPrice();
            // Handling discount
            if (item instanceof CatalogItem) {
                CatalogItem catalogItem = (CatalogItem) item;
                itemPrice -= catalogItem.getDiscountAmount();
                itemPrice *= (100 - catalogItem.getDiscountPercent()) / 100;
            }
            price += itemPrice * currentOrder.getQuantities().get(item.getId());
        }


        // Discount price for subscribers.
        SubscriberType subscriberType = ((Customer) currentUser).getSubscriberType();
        if (subscriberType != null) {
            switch (subscriberType) {
                case MONTHLY:
                    price = price * 0.75;
                    break;
                case YEARLY:
                    price = price * 0.65;
                    break;
            }
        }


        currentOrder.setPrice(price);
    }

    private void resetVisibleTab2() {
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


    private boolean checkFields(Map<TextField, Label> fields) {
        boolean found = false;
        for (Map.Entry<TextField, Label> entry : fields.entrySet()) {
            if (entry.getKey() == null) break;
            if (entry.getKey().getText() == null || entry.getKey().getText().isEmpty()) {
                entry.getValue().setVisible(true);
                found = true;
            }
        }
        return found;
    }

    private boolean isInputEmpty() {
        boolean val = false;

        Map<TextField, Label> fieldsMap = new HashMap<>();
        fieldsMap.put(firstNameVerField, firstNameVerEmptyLabel);
        fieldsMap.put(lastNameVerField, lastNameVerEmptyLabel);
        fieldsMap.put(idNumVerField, idVerEmptyLabel);
        fieldsMap.put(emailVerField, emailVerEmptyLabel);
        fieldsMap.put(phoneNumVerField, phoneVerEmptyLabel);
        fieldsMap.put(addressVerField, addressVerEmptyLabel);

        return checkFields(fieldsMap);
    }

    private boolean isInputEmptyTab4() {
        Map<TextField, Label> fieldsMap = new HashMap<>();
        fieldsMap.put(setDeliveryAddressField, deliveryAddressEmpty);
        fieldsMap.put(setRecipientMailField, recipientMailEmpty);

        return checkFields(fieldsMap);
    }

    private boolean inputCheck() {
        boolean val = true;
        String check;
        check = firstNameVerField.getText();
        if ((Pattern.matches("[a-zA-Z]+", check) == false) && (check.isEmpty() == false)) {
            invalidFirstNameVerLabel.setVisible(true);
            val = false;
        }
        check = lastNameVerField.getText();
        if ((Pattern.matches("[a-zA-Z]+", check) == false) && (check.isEmpty() == false)) {
            invalidLastNameVerLabel.setVisible(true);
            val = false;
        }
        check = idNumVerField.getText();
        if ((check.length() != 9) && (check.isEmpty() == false)) {
            invalidIdVerLabel.setVisible(true);
            val = false;

        }
        check = emailVerField.getText();
        if (check.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-" +
                "\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:" +
                "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\" +
                "x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])") == false &&
                (check.isEmpty() == false)) {
            invalidEmailVerLabel.setVisible(true);
            val = false;
        }
        check = phoneNumVerField.getText();
        if ((Pattern.matches("^((\\+|00)?972\\-?|0)(([23489]|[57]\\d)\\-?\\d{7})$", check) == false) &&
                (check.isEmpty() == false)) {
            invalidPhoneVerLabel.setVisible(true);
            val = false;
        }

        return val;
    }

    private boolean inputCheckTab4() {
        boolean val = true;
        String check;

        check = setRecipientMailField.getText();
        if (check.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-" +
                "\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:" +
                "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\" +
                "x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])") == false &&
                (check.isEmpty() == false)) {
            invalidEmailAddressLabel.setVisible(true);
            val = false;
        }


        return val;
    }

    private boolean dateCheckTab4(LocalDateTime localDateTime) {
        resetVisibleTab4();
        boolean isPast1 = isPast(localDateTime);
        if (isPast1) {
            invalidDate.setVisible(true);
            System.out.println("current date is newer than the entered date");
        }
        return isPast1;
    }


    private void updateFieldsTab2() {
        Customer temp;
        temp = (Customer) currentUser.copy();
        temp.setFirstName(firstNameVerField.getText());
        temp.setLastName(lastNameVerField.getText());
        temp.setMisparZehut(idNumVerField.getText());
        temp.setEmail(emailVerField.getText());
        temp.setPhone(phoneNumVerField.getText());
        temp.setAddress(addressVerField.getText());

        currentOrder.setOrderedBy(temp);

    }

    private void updateFieldsTab4() {
       /* Customer temp;
        temp = (Customer)currentUser.copy();
        temp.setEmail(setRecipientMailField.getText());
        temp.setAddress(setDeliveryAddressField.getText());

        currentOrder.setOrderedBy(temp);*/
        currentOrder.setDeliveryAddress(setDeliveryAddressField.getText());
        currentOrder.setRecipientMail(setRecipientMailField.getText());
    }

    private void resetVisibleTab4() {
        deliveryAddressEmpty.setVisible(false);
        recipientMailEmpty.setVisible(false);
        invalidEmailAddressLabel.setVisible(false);
        invalidDate.setVisible(false);
    }

    private void setDisable(boolean status) {
        if (setDeliveryAddressField != null)
            setDeliveryAddressField.setDisable(!status);
        if (deliveryAddressLabel != null)
            deliveryAddressLabel.setDisable(!status);
        if (recipientMailLabel != null)
            recipientMailLabel.setDisable(!status);
        if (setRecipientMailField != null)
            setRecipientMailField.setDisable(!status);
    }


    private void setSelected(int status) {
        if (sendToAnotherAddRadio != null) {
            switch (status) {
                case 0:
                    sendToMyAddRadio.setSelected(true);
                    sendToAnotherAddRadio.setSelected(false);
                    sendToStoreAddRadio.setSelected(false);
                    break;
                case 1:
                    sendToMyAddRadio.setSelected(false);
                    sendToAnotherAddRadio.setSelected(true);
                    sendToStoreAddRadio.setSelected(false);
                    break;
                case 2:
                    sendToMyAddRadio.setSelected(false);
                    sendToAnotherAddRadio.setSelected(false);
                    sendToStoreAddRadio.setSelected(true);
                    break;
            }
        }
    }

    @FXML
    private void selectMyAddRadio() {
        radioStatus = false;
        setSelected(0);
        setDisable(radioStatus);

    }

    @FXML
    private void selectOtherAddRadio() {
        radioStatus = true;
        setSelected(1);
        setDisable(radioStatus);
    }

    @FXML
    void selectStoreRadioButton() {
        radioStatus = true;
        setSelected(2);
        setDisable(!radioStatus);


    }

    void backToCatalog() {
        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
        EventBus.getDefault().post(new BackToCatalogEvent(currentOrder.getItems(), currentOrder.getQuantities()));
    }

    @FXML
    void cancel(ActionEvent event) {
        backToCatalog();
    }


    /**
     * allow change in the personal details.
     *
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

       /* boolean val = true;
        resetVisibleTab2();
        if(isInputEmpty()) {
            val = false;
        }
        if(!(inputCheck())){
            val = false;
        }
        if (val == true){
            updateShownFieldsTab2();
        }
*/
    }

    @FXML
    void goBack(ActionEvent event) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        if (selectionModel.getSelectedIndex() == 0) {
            backToCatalog();
        } else {
            selectionModel.selectPrevious();
        }
        updateButtons();

    }

    boolean finalTab() {
        return tab5.isSelected();
    }

    boolean firstTab() {
        return tab1.isSelected();
    }

    @FXML
    void next(ActionEvent event) {
        boolean val = true;
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        if (tab2.isSelected()) {
            if (!(firstNameVerField.isDisabled())) {
                resetVisibleTab2();
                boolean firstCheck = isInputEmpty();
                boolean secondCheck = inputCheck();
                if (firstCheck || !secondCheck) {
                    return;
                }

//                fillOrder();
            }
            updateFieldsTab2();
        }
        if (tab3.isSelected()) {
            currentOrder.setGreeting(greetingEntryTextArea.getText());
        }
        if (tab4.isSelected()) {
            resetVisibleTab4();
            if (sendToAnotherAddRadio.isSelected()) {
                resetVisibleTab4();
                boolean firstCheck = isInputEmptyTab4();
                boolean secondCheck = inputCheckTab4();
                if (firstCheck || !secondCheck) {
                    return;
                }
                currentOrder.setDeliveryMethod(DeliveryMethod.DELIVER_TO_ADDRESS);
                updateFieldsTab4();
//                fillOrder();
            } else {
                if (sendToMyAddRadio.isSelected()) {
                    currentOrder.setDeliveryAddress(currentOrder.getOrderedBy().getAddress());
                    currentOrder.setRecipientMail(currentOrder.getOrderedBy().getEmail());
                    currentOrder.setDeliveryMethod(DeliveryMethod.SELF_COLLECTION);
                }
                else{
                    if(sendToStoreAddRadio.isSelected()){
                        String storeName = currentOrder.getStore().getName();
                        System.out.println(storeName);
                        currentOrder.setDeliveryAddress(storeName);
                        currentOrder.setRecipientMail(currentOrder.getOrderedBy().getEmail());
                        currentOrder.setDeliveryMethod(DeliveryMethod.DELIVER_TO_ADDRESS);
                    }
                }

            }
            LocalDate localDate = datePicker.getValue();
//            boolean a = localDate.isBefore(LocalDate.now());
            LocalTime localTime = LocalTime.of(Integer.parseInt(hourChooseBox.getValue()), Integer.parseInt(minuteChooseBox.getValue()), 0);
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

            if (dateCheckTab4(localDateTime)) {
                return;
            } else {
                ZoneId zoneId = ZoneId.of("Asia/Jerusalem");

                Date date = Date.from(localDateTime.atZone(zoneId).toInstant());
                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s = formatter.format(date);
                currentOrder.setSupplyDate(date);
                output = formatter.format(currentOrder.getSupplyDate());
            }

        }

        updateShownFieldsTab5();


        // If the current tub is the final tub. send the order and go back to the main screen.
        if (finalTab()) {
            sendOrder();
        } else {
            // move to the next tab and enable it.
            selectionModel.selectNext();
            selectionModel.getSelectedItem().setDisable(false);
        }
        updateButtons();

    }


    private ObservableList<String> hourList = FXCollections.observableArrayList("" +
                    "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23");

    private ObservableList<String> minuteList = FXCollections.observableArrayList("" +
                    "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59");

    private boolean isPast(LocalDateTime localDateTime) {
        boolean flag = true;
//        Calendar now = Calendar.getInstance();
//        LocalDate localDate = datePicker.getValue();
//        boolean a = localDate.isBefore(LocalDate.now());
//        LocalTime localTime = LocalTime.of(Integer.parseInt(hourChooseBox.getValue()), Integer.parseInt(minuteChooseBox.getValue()), 0);
//
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return localDateTime.isBefore(LocalDateTime.now());

      /*  Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourChooseBox.getValue()));
        now.set(Calendar.MINUTE, Integer.parseInt((minuteChooseBox.getValue())));

        return (now.getTime().before(new Date()));*/
    }

    private void updateShownFieldsTab2() {
        firstNameVerField.setText(currentOrder.getOrderedBy().getFirstName());
        lastNameVerField.setText(currentOrder.getOrderedBy().getLastName());
        idNumVerField.setText(currentOrder.getOrderedBy().getMisparZehut());
        emailVerField.setText(currentOrder.getOrderedBy().getEmail());
        phoneNumVerField.setText(currentOrder.getOrderedBy().getPhone());
        addressVerField.setText(currentOrder.getOrderedBy().getAddress());
    }

    private void updateShownFieldsTab5() {
        if (firstNameField != null) {
            firstNameField.setText(currentOrder.getOrderedBy().getFirstName());
        }
        if (lastNameField != null) {
            lastNameField.setText(currentOrder.getOrderedBy().getLastName());
        }
        if (idNumField != null) {
            idNumField.setText(currentOrder.getOrderedBy().getMisparZehut());
        }
        if (emailField != null) {
            emailField.setText(currentOrder.getOrderedBy().getEmail());
        }
        if (phoneNumberField != null) {
            phoneNumberField.setText(currentOrder.getOrderedBy().getPhone());
        }
        if (addressField != null) {
            addressField.setText(currentOrder.getOrderedBy().getAddress());
        }
        if (greetingTextArea != null) {
            greetingTextArea.setText(currentOrder.getGreeting());
        }
        if (deliveryAddressField1 != null) {
            deliveryAddressField1.setText(currentOrder.getDeliveryAddress());
        }
        if (recipientMailField1 != null) {
            recipientMailField1.setText(currentOrder.getRecipientMail());
        }
        if (dateAndTimeShowField != null) {
            dateAndTimeShowField.setText(output);
        }
        if (totalPriceShowField != null) {
            totalPriceShowField.setText(Double.toString(currentOrder.getPrice()));
        }
    }

    @FXML
    private void updateButtons() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        if (firstTab()) {
            backButton.setText("Continue shopping");
            cancelButton.setVisible(false);
        } else {
            backButton.setText("Back");
            cancelButton.setVisible(true);
        }

        if (selectionModel.getSelectedIndex() == 4) {
            nextButton.setText("Checkout");
        } else {
            nextButton.setText("Next");
        }
        if (tab2 != null && tab2.isSelected()) {
            updateShownFieldsTab2();
        }
        if (tab5 != null && tab5.isSelected()) {
            updateShownFieldsTab5();
        }
    }

    private void populateTable() {
        itemTable.getColumns().clear();
        TableColumn<Item, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory((cellFeatures) -> {
            if (cellFeatures.getValue() instanceof CustomItem) {
                CustomItem customItem = (CustomItem) cellFeatures.getValue();
                return new SimpleObjectProperty<>("Custom item");
            }
            return new SimpleObjectProperty<>(((CatalogItem) cellFeatures.getValue()).getDescription());
        });
        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Item, Pair<Item, Long>> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellFeatures ->
                new SimpleObjectProperty<Pair<Item, Long>>(new Pair<>(
                        cellFeatures.getValue(),
                        currentOrder.getQuantities().get(cellFeatures.getValue().getId()))));
        quantityColumn.setCellFactory((column) -> {
            HBox hBox = new HBox();
            Button minusButton = new Button("-");
            Label quantityLabel = new Label("0");
            Button plusButton = new Button("+");
            HBox.setMargin(quantityLabel, new Insets(0, 5, 0, 5));
            return new TableCell<>() {
                private void refreshPair(Pair<Item, Long> longLongPair) {
                    updateItem(new Pair<>(longLongPair.getKey(),
                            currentOrder.getQuantities().get(longLongPair.getKey().getId())), true);
                }

                @Override
                protected void updateItem(Pair<Item, Long> longLongPair, boolean b) {
                    if (longLongPair != null) {
                        minusButton.setOnAction(actionEvent -> {
                            if (longLongPair.getValue() > 1) {
                                currentOrder.getQuantities().replace(longLongPair.getKey().getId(), longLongPair.getValue() - 1);
                                refreshPair(longLongPair);
                            } else {
                                if (longLongPair.getKey() instanceof CustomItem) {
                                    LilachService service = APIAccess.getService();
                                    try {
                                        service.deleteCustomItem(longLongPair.getKey().getId()).execute();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                currentOrder.getItems().remove(longLongPair.getKey());
                                currentOrder.getQuantities().remove(longLongPair.getKey().getId());
                                populateTable();
                            }
                        });
                        plusButton.setOnAction(actionEvent -> {
                                currentOrder.getQuantities()
                                        .replace(longLongPair.getKey().getId(), longLongPair.getValue() + 1);
                                refreshPair(longLongPair);
                        });
                        System.out.println("KEY: " + longLongPair.getKey());
                        quantityLabel.setText(currentOrder.getQuantities().get(longLongPair.getKey().getId()).toString());
                        hBox.getChildren().clear();
                        hBox.getChildren().addAll(minusButton, quantityLabel, plusButton);
                        setGraphic(hBox);
                    }
                }
            };
        });

        itemTable.getColumns().addAll(descriptionColumn, priceColumn, quantityColumn);
        itemTable.setItems(FXCollections.observableList(currentOrder.getItems()));
    }

    private void sendOrder() {
        LilachService service = APIAccess.getService();

        // TODO: Add delivery method and replace the line below
        currentOrder.setDeliveryMethod(DeliveryMethod.DELIVER_TO_ADDRESS);

        service.newOrder(currentOrder).enqueue((new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                System.out.println("CODE: " + response.code());
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        Utils.showInfo("Thank you for your order.");
                        EventBus.getDefault().post(new DashboardSwitchEvent("order_list_view"));
                    });
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable throwable) {
                throwable.printStackTrace();
                Utils.showError("Network failure");
            }
        }));
    }

    @Override
    public void refresh() {
        EventBus.getDefault().register(this);
        currentOrder.setOrderedBy((Customer) currentUser);
        LocalTime currentTime = LocalTime.now();
        //SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        //setting the default to 'send to my address'.
        selectMyAddRadio();
        if (hourChooseBox != null) {
            hourChooseBox.setItems(hourList);
//            int time  = currentTime.getHour();
            String hour = Integer.toString(currentTime.getHour());
            if (hour.length() == 1) {
                hour = "0" + hour;
            }
            hourChooseBox.setValue(hour);


        }

        if (minuteChooseBox != null) {
            minuteChooseBox.setItems(minuteList);
            String minute = Integer.toString(currentTime.getMinute());
            if (minute.length() == 1) {
                minute = "0" + minute;
            }
            minuteChooseBox.setValue(minute);
        }

        datePicker.setValue(LocalDate.now());


        //System.out.println(selectionModel.getSelectedIndex());
        //if(selectionModel.getSelectedIndex() == 0){

        updateButtons();

        LilachService service = APIAccess.getService();
        service.getStoresByCustomer(currentOrder.getOrderedBy().getId()).enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.code() != 200) {
                    Utils.showError("Error code: " + response.code());
                }

                List<Store> stores = response.body();
                currentOrder.setStore(stores.get(0));
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable throwable) {
                Utils.showError("Network failure");
            }
        });

    }

    @Override
    public void onSwitch() {
        EventBus.getDefault().unregister(this);
    }
}



