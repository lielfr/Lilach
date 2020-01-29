package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.cshaifasweng.winter.events.CustomerSendEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.LoginChangeEvent;
import org.cshaifasweng.winter.events.TokenSetEvent;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class DashboardController implements Initializable {
    protected static final Logger log = Logger.getLogger(DashboardController.class.getName());

    @FXML
    private ScrollPane containerPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox menuBox;

    private Pair<Parent, Object> currentWindow;

    private List<Pair<Node, EventHandler<ActionEvent>>> menu = Collections.synchronizedList(new ArrayList<>());

    private Scene scene;

    Button buttonCatalog = new Button("Catalog");
    Button customerServiceButton = new Button("Customer service");
    Button loginButton = new Button("Login");
    Button createAccountButton = new Button("Create account");
    Button cartButton = new Button("Cart");
    Button myOrdersButton = new Button("My orders");
    Button complaintsButton = new Button("Complaints");
    Button catalogEditButton = new Button("Edit catalog");
    Button changeDetailsButton = new Button("Change details");
    Button userListButton = new Button("Users list");

    EventHandler<ActionEvent> callbackCatalog = actionEvent -> {
        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
    };

    EventHandler<ActionEvent> loginCallback = actionEvent -> {
        if (APIAccess.getCurrentUser() == null) {
            EventBus.getDefault().post(new DashboardSwitchEvent("login_screen"));
        } else {
            logout(actionEvent);
        }
    };

    EventHandler<ActionEvent> createAccountCallback = actionEvent -> {
        EventBus.getDefault().post(new DashboardSwitchEvent("createaccount"));
    };

    EventHandler<ActionEvent> changeDetailsCallback = actionEvent -> {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(LayoutManager.getInstance().getFXML("edit_customer").getKey()));
            stage.show();
            EventBus.getDefault().post(new CustomerSendEvent(APIAccess.getCurrentUser(), stage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    private void populateMenu() {
        User user = APIAccess.getCurrentUser();
        menu.clear();
        if (user == null) {
            menu.add(new Pair<>(buttonCatalog, callbackCatalog));
            loginButton.setText("Login");
            menu.add(new Pair<>(loginButton, loginCallback));
            menu.add(new Pair<>(createAccountButton, createAccountCallback));
        } else {
            loginButton.setText("Logout");

            if (user instanceof Customer) {
                menu.add(new Pair<>(buttonCatalog, callbackCatalog));
                menu.add(new Pair<>(loginButton, loginCallback));
                menu.add(new Pair<>(myOrdersButton, (actionEvent -> {
                    EventBus.getDefault().post(new DashboardSwitchEvent("order_list_view"));
                })));
                menu.add(new Pair<>(changeDetailsButton, changeDetailsCallback));
                menu.add(new Pair<>(customerServiceButton, actionEvent -> {
                    EventBus.getDefault().post(new DashboardSwitchEvent("complaint_filing"));
                }));
            } else if (user instanceof Employee) {
                menu.add(new Pair<>(catalogEditButton, actionEvent -> {
                    EventBus.getDefault().post(new DashboardSwitchEvent("edit_catalog_list"));
                }));

                menu.add(new Pair<>(loginButton, loginCallback));
                menu.add(new Pair<>(complaintsButton, actionEvent -> {
                    EventBus.getDefault().post(new DashboardSwitchEvent("complaint_list"));
                }));
                menu.add(new Pair<>(userListButton, actionEvent -> {
                    EventBus.getDefault().post(new DashboardSwitchEvent("user_display"));
                }));

            }
        }

        menu.forEach(nodeCallbackPair -> {
            Button button = (Button) nodeCallbackPair.getKey();
            button.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(nodeCallbackPair.getKey(), Priority.ALWAYS);
            VBox.setMargin(nodeCallbackPair.getKey(), new Insets(5, 0, 5, 0));
            button.setOnAction(nodeCallbackPair.getValue());
        });
        menuBox.getChildren().clear();
        menuBox.getChildren().addAll(menu.stream()
        .map(Pair::getKey).collect(Collectors.toList()));
    }

    private void setPage(String page) {
        try {
            if (currentWindow != null) {
                scene = currentWindow.getKey().getScene();
                Refreshable currentController = (Refreshable) currentWindow.getValue();
                currentController.onSwitch();
            }
            containerPane.setContent(null);
            currentWindow = null;
            Pair<Parent,Object> dataPair = LayoutManager.getInstance().getFXML(page);
            containerPane.setContent(dataPair.getKey());
            Refreshable controller = (Refreshable) dataPair.getValue();
            controller.refresh();
            currentWindow = dataPair;
        } catch (IOException e) {
            Utils.showError(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handlePageChange(DashboardSwitchEvent event) throws IOException {
        setPage(event.pageName);
    }

    @Subscribe
    public void handleUserLogin(LoginChangeEvent event) {
        User user = APIAccess.getCurrentUser();
        if (user == null)
            welcomeLabel.setText("Welcome, Guest");
        else
            welcomeLabel.setText("Welcome, "+user.getFirstName());
        populateMenu();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage("catalog");
        EventBus.getDefault().register(this);
        LoggerUtils.setupLogger(log);
        populateMenu();
    }
    @FXML
    void fileComplaint(ActionEvent event) {
        setPage("complaint_filing");
    }

    @FXML
    void handleComplaint(ActionEvent event) {
        setPage("complaint_handling");
    }

    @FXML
    void createaccount(ActionEvent event) {setPage("createaccount");}

    @FXML
    void showLoginScreen(ActionEvent event) {
        setPage("login_screen");
    }

    @FXML
    void logout(ActionEvent event) {
        if (APIAccess.getCurrentUser() != null) {
            LilachService service = APIAccess.getService();
            service.logout().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() != 200) {
                        Utils.showError("Error code: " + response.code());
                    }

                    Platform.runLater(() -> {
                        APIAccess.setCurrentUser(null);
                        EventBus.getDefault().post(new TokenSetEvent(""));
                        EventBus.getDefault().post(new LoginChangeEvent());
                        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Utils.showError("Network failure");
                }
            });
        }

    }


    @FXML
    void myOrders(ActionEvent event) {
        setPage("order_list_view");
    }

    @FXML
    void openComplaints(ActionEvent event) {
        setPage("complaint_list");
    }

    @FXML
     void cartButton(ActionEvent event) {
        setPage("order");


    }

    @FXML
    void backToCatalog(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
    }

    @FXML
    void addItem(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("add_item"));
    }

    @FXML
    void openPrimary() {
        EventBus.getDefault().post(new DashboardSwitchEvent("edit_catalog_list"));
    }

    @FXML
    public void listUsers(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("user_display"));
    }
}
