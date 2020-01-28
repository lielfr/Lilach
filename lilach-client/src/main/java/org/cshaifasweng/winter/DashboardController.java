package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.util.Pair;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.LoginChangeEvent;
import org.cshaifasweng.winter.events.TokenSetEvent;
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
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class DashboardController implements Initializable {
    protected static final Logger log = Logger.getLogger(DashboardController.class.getName());

    @FXML
    private DashboardPane containerPane;

    @FXML
    private Label welcomeLabel;

    private Pair<Parent, Object> currentWindow;

    private Scene scene;

    private void setPage(String page) {
        try {
            if (currentWindow != null) {
                scene = currentWindow.getKey().getScene();
                Refreshable currentController = (Refreshable) currentWindow.getValue();
                currentController.onSwitch();
            }
            containerPane.setContent(null);
            currentWindow = null;
            containerPane.refreshPane();
            Pair<Parent,Object> dataPair = LayoutManager.getInstance().getFXML(page);
            containerPane.setContent(dataPair.getKey());
            containerPane.refreshPane();
            Refreshable controller = (Refreshable) dataPair.getValue();
            controller.refresh();
            currentWindow = dataPair;
        } catch (IOException e) {
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage("catalog");
        EventBus.getDefault().register(this);
        LoggerUtils.setupLogger(log);
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
                        log.severe("Something really weird happened: could not log out.");
                        return;
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
