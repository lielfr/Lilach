package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.cshaifasweng.winter.events.CustomerSendEvent;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.models.UserType;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserDisplayController implements Refreshable, Initializable {

    @FXML
    private ChoiceBox<Store> storeSelect;

    @FXML
    private ChoiceBox<UserType> typeSelect;

    @FXML
    private Button filterButton;

    @FXML
    private TableView<User> userTable;

    @FXML
    private Button backToCatalogButton;

    @FXML
    private Button refreshButton;

    private LilachService service;

    private Store selectedStore;
    private UserType selectedType = UserType.ALL;
    private List<User> usersList;

    @FXML
    void backToCatalog(ActionEvent event) {

    }

    @FXML
    void filter(ActionEvent event) {

    }

    @FXML
    void refreshPage(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void populateTable() {

        TableColumn<User, Long> idColumn = new TableColumn<>("Lilach ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, Long> misparZehutColumn = new TableColumn<>("ID (Mispar Zehut)");
        misparZehutColumn.setCellValueFactory(new PropertyValueFactory<>("misparZehut"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> firstNameColumn = new TableColumn<>("First name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, Boolean> isLoggedInColumn = new TableColumn<>("Logged in");
        isLoggedInColumn.setCellValueFactory(cellFeatures ->
                new SimpleBooleanProperty(cellFeatures.getValue().getLoggedIn()));

        ContextMenu userContextMenu = new ContextMenu();

        MenuItem menuItemEdit = new MenuItem("Edit user");
        MenuItem menuItemUnlock = new MenuItem("Unlock user (if logged in)");

        userContextMenu.getItems().addAll(menuItemEdit, menuItemUnlock);

        userContextMenu.setOnAction((actionEvent -> {
            User user = userTable.getSelectionModel().getSelectedItem();
            if (menuItemEdit.equals(actionEvent.getTarget())) {
                try {
                    Stage stage = new Stage();
                    Scene scene;

                    if (user instanceof Customer) {
                        scene = new Scene(LayoutManager.getInstance().getFXML("edit_customer").getKey());
                    } else {
                        scene = new Scene(LayoutManager.getInstance().getFXML("edit_employee").getKey());
                    }

                    stage.setScene(scene);
                    stage.show();

                    EventBus.getDefault().post(new CustomerSendEvent(user, stage));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }));

        Callback<List<User>> usersListCB = new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() != 200) {
                    try {
                        System.out.println("STATUS CODE: " + response.code() + ", error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                Platform.runLater(() -> {
                    usersList = response.body();
                    userTable.getColumns().clear();
                    userTable.getColumns().addAll(idColumn, misparZehutColumn, emailColumn,
                            firstNameColumn, lastNameColumn, isLoggedInColumn);

                    userTable.setItems(FXCollections.observableArrayList(usersList));
                    Utils.addContextMenu(userTable, userContextMenu);
                });


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        if (selectedStore == null)
            service.getUsers(selectedType).enqueue(usersListCB);
        else
            service.getUsers(selectedStore.getId(), selectedType).enqueue(usersListCB);
    }

    @Override
    public void refresh() {
        storeSelect.setConverter(new StringConverter<>() {
            @Override
            public String toString(Store store) {
                if (store == null) return "All";
                return store.getName();
            }

            @Override
            public Store fromString(String s) {
                return null;
            }
        });

        typeSelect.setConverter(new StringConverter<>() {
            @Override
            public String toString(UserType userType) {
                switch (userType) {
                    case ALL:
                        return "All";
                    case CUSTOMER:
                        return "Customers";
                    case EMPLOYEE:
                        return "Employees";
                }
                return null;
            }

            @Override
            public UserType fromString(String s) {
                return null;
            }
        });

        service = APIAccess.getService();
        service.getAllStores().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.code() != 200 || response.body() == null) {
                    try {
                        System.out.println("STATUS CODE: " + response.code() + ", error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                Platform.runLater(() -> {
                    List<Store> tempList = new ArrayList<>();
                    tempList.add(null);
                    tempList.addAll(response.body());
                    storeSelect.setItems(FXCollections.observableArrayList(tempList));
                    typeSelect.setItems(FXCollections.observableArrayList(Arrays.asList(UserType.values())));

                    storeSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, store, t1) -> {
                        selectedStore = storeSelect.getValue();
                        Platform.runLater(UserDisplayController.this::populateTable);
                    });
                    typeSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, userType, t1) -> {
                        selectedType = typeSelect.getValue();
                        Platform.runLater(UserDisplayController.this::populateTable);
                    });

                    storeSelect.getSelectionModel().select(0);
                    typeSelect.getSelectionModel().select(UserType.ALL);


                });
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void onSwitch() {

    }
}
